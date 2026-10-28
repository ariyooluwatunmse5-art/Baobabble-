const https = require('https');
const crypto = require('crypto');
const fs = require('fs');
const path = require('path');

const ENGINE_VERSION = 'v3_gemini38_tone';
const CACHE_DIR = (process.env.VERCEL || process.env.AWS_LAMBDA_FUNCTION_NAME || !fs.existsSync(__dirname))
  ? '/tmp/tts-cache'
  : path.join(__dirname, 'tts-cache');
if (!fs.existsSync(CACHE_DIR)) {
  try {
    fs.mkdirSync(CACHE_DIR, { recursive: true });
  } catch (e) {
    console.warn('Could not create CACHE_DIR:', e.message);
  }
}

// In-memory audio buffer cache
const memoryCache = new Map();

// Candidate models in strict order of acoustic priority
const CANDIDATE_MODELS = [
  'gemini-3.8-flash-tts',
  'gemini-3.8-flash-lite-tts',
  'gemini-3.1-flash-tts-preview',
  'gemini-2.5-flash-preview-tts'
];

// High-fidelity African language prosodic and tonal instructions
const VOICE_CONFIG = {
  yoruba: {
    id: 'yoruba',
    name: 'Yoruba',
    langCode: 'yo-NG',
    voice: 'Puck',
    model: 'gemini-3.8-flash-tts',
    style: 'Style: Natural Nigerian Yoruba pronunciation. Authentic tonal contour with distinct High (Mí), Mid (Re), and Low (Dò) pitch registers. Clear learner-friendly articulation. Natural conversational rhythm. Strictly preserve all vowel marks and sub-dots (ẹ, ọ, ṣ). Do not anglicize pronunciation.',
    label: 'Yoruba (Nigeria)',
    testPhrase: 'Ẹ káàárọ̀'
  },
  igbo: {
    id: 'igbo',
    name: 'Igbo',
    langCode: 'ig-NG',
    voice: 'Charon',
    model: 'gemini-3.8-flash-tts',
    style: 'Style: Authentic Nigerian Igbo pronunciation. Preserving appropriate tones and vowel harmony between light and dotted -ATR vowels (ị, ọ, ụ). Natural Igbo cadence and consonant clarity (gb, kp, nw, ny). Do not anglicize pronunciation.',
    label: 'Igbo (Nigeria)',
    testPhrase: 'Ndewo'
  },
  hausa: {
    id: 'hausa',
    name: 'Hausa',
    langCode: 'ha-NG',
    voice: 'Fenrir',
    model: 'gemini-3.8-flash-tts',
    style: 'Style: Natural Northern Nigerian Hausa pronunciation. Clear articulation of glottalized and implosive hooked consonants (Ƙ/ƙ, Ɓ/ɓ, Ɗ/ɗ). Natural Hausa rhythm, vowel length distinctions, and dignified cadence. Do not anglicize pronunciation.',
    label: 'Hausa (Northern Nigeria)',
    testPhrase: 'Sannu'
  },
  swahili: {
    id: 'swahili',
    name: 'Swahili',
    langCode: 'sw-KE',
    voice: 'Aoede',
    model: 'gemini-3.8-flash-tts',
    style: 'Style: Authentic East African Kiswahili pronunciation. Natural East African rhythm and cadence with standard penultimate syllable stress. Pure, un-diphthongized vowels. Do not anglicize pronunciation.',
    label: 'Swahili (East Africa)',
    testPhrase: 'Habari'
  },
  twi: {
    id: 'twi',
    name: 'Akan/Twi',
    langCode: 'ak-GH',
    voice: 'Kore',
    model: 'gemini-3.8-flash-tts',
    style: 'Style: Authentic Ghanaian Asante Akan/Twi pronunciation. Natural Ghanaian regional characteristics, warm cadence, and accurate tonal contour. Clear learner-friendly articulation. Do not anglicize pronunciation.',
    label: 'Akan / Twi (Ghana)',
    testPhrase: 'Maakye'
  },
  akan: {
    id: 'akan',
    name: 'Akan/Twi',
    langCode: 'ak-GH',
    voice: 'Kore',
    model: 'gemini-3.8-flash-tts',
    style: 'Style: Authentic Ghanaian Asante Akan/Twi pronunciation. Natural Ghanaian regional characteristics, warm cadence, and accurate tonal contour. Clear learner-friendly articulation. Do not anglicize pronunciation.',
    label: 'Akan / Twi (Ghana)',
    testPhrase: 'Maakye'
  },
  zulu: {
    id: 'zulu',
    name: 'Zulu',
    langCode: 'zu-ZA',
    voice: 'Fenrir',
    model: 'gemini-3.8-flash-tts',
    style: 'Style: Authentic South African isiZulu pronunciation. Natural Nguni rhythm with clear click consonant articulation (dental c, alveolar q, lateral x). Ubuntu conversational cadence. Do not anglicize pronunciation.',
    label: 'Zulu (South Africa)',
    testPhrase: 'Sawubona'
  },
  amharic: {
    id: 'amharic',
    name: 'Amharic',
    langCode: 'am-ET',
    voice: 'Aoede',
    model: 'gemini-3.8-flash-tts',
    style: "Style: Authentic Ethiopian Amharic pronunciation with native Fidel Ge'ez cadence and explosive ejective consonants. Do not anglicize.",
    label: 'Amharic (Ethiopia)',
    testPhrase: 'ሰላም (Selam)'
  },
  lingala: {
    id: 'lingala',
    name: 'Lingala',
    langCode: 'ln-CD',
    voice: 'Charon',
    model: 'gemini-3.8-flash-tts',
    style: 'Style: Authentic Congolese Lingala pronunciation with native Central African Bantu musicality and tones. Do not anglicize.',
    label: 'Lingala (Congo)',
    testPhrase: 'Mbote'
  }
};

function getCacheKey(language, text, voice, model) {
  const normLang = (language || 'yoruba').toLowerCase().trim();
  const normText = (text || '').toLowerCase().trim();
  const hash = crypto.createHash('sha256').update(`${ENGINE_VERSION}_${normLang}_${voice}_${model}_${normText}`).digest('hex');
  return {
    hash,
    filename: `${ENGINE_VERSION}_${normLang}_${voice}_${hash.slice(0, 16)}.wav`
  };
}

function pcmToWav(pcmBuffer, sampleRate = 24000, numChannels = 1, bitsPerSample = 16) {
  const byteRate = sampleRate * numChannels * bitsPerSample / 8;
  const blockAlign = numChannels * bitsPerSample / 8;
  const dataSize = pcmBuffer.length;
  const header = Buffer.alloc(44);

  header.write('RIFF', 0);
  header.writeUInt32LE(36 + dataSize, 4);
  header.write('WAVE', 8);
  header.write('fmt ', 12);
  header.writeUInt32LE(16, 16);
  header.writeUInt16LE(1, 20); // PCM format
  header.writeUInt16LE(numChannels, 22);
  header.writeUInt32LE(sampleRate, 24);
  header.writeUInt32LE(byteRate, 28);
  header.writeUInt16LE(blockAlign, 32);
  header.writeUInt16LE(bitsPerSample, 34);
  header.write('data', 36);
  header.writeUInt32LE(dataSize, 40);

  return Buffer.concat([header, pcmBuffer]);
}

function callGeminiTTSModel(model, stylePrompt, cleanText, voiceName) {
  const apiKey = process.env.GEMINI_API_KEY;
  if (!apiKey) {
    return Promise.reject(new Error('GEMINI_API_KEY environment variable is missing'));
  }

  // Delivery style in part 1, pure target-language text in part 2
  const postData = JSON.stringify({
    contents: [{
      parts: [
        { text: stylePrompt },
        { text: cleanText }
      ]
    }],
    generationConfig: {
      responseModalities: ['AUDIO'],
      speechConfig: {
        voiceConfig: {
          prebuiltVoiceConfig: {
            voiceName: voiceName || 'Puck'
          }
        }
      }
    }
  });

  return new Promise((resolve, reject) => {
    const req = https.request({
      hostname: 'generativelanguage.googleapis.com',
      path: `/v1beta/models/${model}:generateContent?key=${apiKey}`,
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Content-Length': Buffer.byteLength(postData)
      }
    }, (res) => {
      let body = '';
      res.on('data', chunk => body += chunk);
      res.on('end', () => {
        if (res.statusCode !== 200) {
          return reject(new Error(`Model ${model} returned HTTP ${res.statusCode}: ${body.slice(0, 150)}`));
        }
        try {
          const data = JSON.parse(body);
          const part = data.candidates?.[0]?.content?.parts?.[0];
          if (!part || !part.inlineData?.data) {
            return reject(new Error('No inline audio returned from Gemini'));
          }
          const pcm = Buffer.from(part.inlineData.data, 'base64');
          const wav = pcmToWav(pcm);
          resolve({ wav, model });
        } catch (e) {
          reject(e);
        }
      });
    });

    req.on('error', reject);
    req.write(postData);
    req.end();
  });
}

async function getOrSynthesizeSpeech(text, language = 'yoruba', speed = 1.0) {
  const langKey = (language || 'yoruba').toLowerCase().trim();
  const cleanText = (text || '').trim();
  if (!cleanText) {
    throw new Error('Empty text provided for TTS');
  }

  const cfg = VOICE_CONFIG[langKey] || VOICE_CONFIG.yoruba;
  const voice = cfg.voice;
  const preferredModel = cfg.model || 'gemini-3.8-flash-tts';

  const { hash, filename } = getCacheKey(langKey, cleanText, voice, preferredModel);
  const filePath = path.join(CACHE_DIR, filename);

  // 1. Check in-memory cache
  if (memoryCache.has(hash)) {
    return {
      wav: memoryCache.get(hash),
      cached: true,
      provider: 'Google Gemini (Memory Cache)',
      model: preferredModel,
      language: langKey,
      voice: voice,
      style: cfg.style,
      version: ENGINE_VERSION
    };
  }

  // 2. Check persistent disk cache
  if (fs.existsSync(filePath)) {
    const wav = fs.readFileSync(filePath);
    memoryCache.set(hash, wav);
    return {
      wav,
      cached: true,
      provider: 'Google Gemini (Disk Cache)',
      model: preferredModel,
      language: langKey,
      voice: voice,
      style: cfg.style,
      version: ENGINE_VERSION
    };
  }

  // 3. Synthesize via flagship Gemini 3.8 Flash TTS with intelligent fallback
  let lastError = null;
  for (const model of CANDIDATE_MODELS) {
    try {
      const result = await callGeminiTTSModel(model, cfg.style, cleanText, voice);
      // Cache to disk and memory
      fs.writeFileSync(filePath, result.wav);
      memoryCache.set(hash, result.wav);

      return {
        wav: result.wav,
        cached: false,
        provider: 'Google Gemini',
        model: result.model,
        language: langKey,
        voice: voice,
        style: cfg.style,
        version: ENGINE_VERSION
      };
    } catch (err) {
      lastError = err;
      console.warn(`[TTS Failover] ${model} unavailable (${err.message.slice(0, 60)}). Trying next candidate...`);
    }
  }

  throw new Error(`All Google Gemini TTS models exhausted. Last error: ${lastError?.message}`);
}

module.exports = {
  getOrSynthesizeSpeech,
  VOICE_CONFIG,
  ENGINE_VERSION,
  CACHE_DIR
};
