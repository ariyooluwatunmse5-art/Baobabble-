const { getOrSynthesizeSpeech } = require('./tts-engine');

const corePhrases = [
  { lang: 'yoruba', text: 'Ẹ ń lẹ́ o!', slow: false },
  { lang: 'yoruba', text: 'Ẹ ń lẹ́ o!', slow: true },
  { lang: 'yoruba', text: 'Ẹ káàárọ̀', slow: false },
  { lang: 'yoruba', text: 'Ẹ káàárọ̀', slow: true },
  { lang: 'yoruba', text: 'Bawo ni?', slow: false },
  { lang: 'yoruba', text: 'E se pupo', slow: false },
  { lang: 'yoruba', text: 'O dabo', slow: false },

  { lang: 'igbo', text: 'Ndewo', slow: false },
  { lang: 'igbo', text: 'Ndewo', slow: true },
  { lang: 'igbo', text: 'Nnọọ / Kèdú!', slow: false },
  { lang: 'igbo', text: 'Dalu', slow: false },

  { lang: 'hausa', text: 'Sannu', slow: false },
  { lang: 'hausa', text: 'Sannu', slow: true },
  { lang: 'hausa', text: 'Sannu da zuwa!', slow: false },
  { lang: 'hausa', text: 'Na gode', slow: false },

  { lang: 'swahili', text: 'Habari', slow: false },
  { lang: 'swahili', text: 'Habari', slow: true },
  { lang: 'swahili', text: 'Hujambo / Karibu!', slow: false },
  { lang: 'swahili', text: 'Asante sana', slow: false },

  { lang: 'twi', text: 'Maakye', slow: false },
  { lang: 'twi', text: 'Maakye', slow: true },
  { lang: 'twi', text: 'Akwaaba / Mema wo akye!', slow: false },
  { lang: 'twi', text: 'Medaase', slow: false },

  { lang: 'zulu', text: 'Sawubona', slow: false },
  { lang: 'zulu', text: 'Sawubona', slow: true },
  { lang: 'zulu', text: 'Sawubona!', slow: false },
  { lang: 'zulu', text: 'Ngiyabonga', slow: false },

  { lang: 'amharic', text: 'ሰላም (Selam)', slow: false },
  { lang: 'amharic', text: 'እንደምን አለህ (Endemen alleh)!', slow: false },

  { lang: 'lingala', text: 'Mbote', slow: false },
  { lang: 'lingala', text: 'Mbote na yo!', slow: false }
];

async function sleep(ms) {
  return new Promise(r => setTimeout(r, ms));
}

async function run() {
  console.log(`Starting pre-cache for ${corePhrases.length} phrases...`);
  for (let i = 0; i < corePhrases.length; i++) {
    const item = corePhrases[i];
    try {
      const res = await getOrSynthesizeSpeech(item.text, item.lang, item.slow);
      console.log(`[${i+1}/${corePhrases.length}] Cached ${item.lang} "${item.text}" (slow: ${item.slow}) -> ${res.wav.length} bytes (cached: ${res.cached})`);
    } catch (e) {
      console.warn(`[WARN] Failed to cache ${item.lang} "${item.text}":`, e.message);
    }
    // Sleep 4 seconds between calls to stay well within free tier TPM/RPM limits
    await sleep(4000);
  }
  console.log("Pre-cache complete!");
}

run();
