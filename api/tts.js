const { getOrSynthesizeSpeech, VOICE_CONFIG, ENGINE_VERSION } = require('../tts-engine');

module.exports = async (req, res) => {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Accept');
  res.setHeader('X-Content-Type-Options', 'nosniff');

  if (req.method === 'OPTIONS') {
    res.status(204).end();
    return;
  }

  const query = req.query || {};
  let text = query.text;
  let language = query.lang || query.language || 'yoruba';
  let isSlow = query.slow === 'true';
  let speedParam = query.speed;
  let speed = speedParam ? parseFloat(speedParam) : (isSlow ? 0.70 : 1.00);
  let format = query.format || '';

  if (req.method === 'POST' && req.body) {
    const body = typeof req.body === 'string' ? JSON.parse(req.body) : req.body;
    if (body.text) text = body.text;
    if (body.lang || body.language) language = body.lang || body.language;
    if (body.speed !== undefined) speed = parseFloat(body.speed);
    else if (body.slow !== undefined || body.isSlow !== undefined) isSlow = !!(body.slow || body.isSlow);
    if (body.format) format = body.format;
  }

  if (!text || !text.trim()) {
    res.status(400).json({ error: 'Text parameter is required for TTS' });
    return;
  }

  try {
    const speech = await getOrSynthesizeSpeech(text, language, speed);

    if (format === 'wav' || req.headers?.accept?.includes('audio/wav')) {
      res.setHeader('Content-Type', 'audio/wav');
      res.setHeader('Content-Length', speech.wav.length);
      res.setHeader('Cache-Control', 'public, max-age=86400');
      res.setHeader('X-TTS-Provider', speech.provider);
      res.setHeader('X-TTS-Model', speech.model);
      res.setHeader('X-TTS-Voice', speech.voice);
      res.setHeader('X-TTS-Cached', speech.cached ? 'true' : 'false');
      res.setHeader('X-TTS-Version', ENGINE_VERSION);
      res.status(200).send(speech.wav);
      return;
    }

    const base64Audio = `data:audio/wav;base64,${speech.wav.toString('base64')}`;
    res.setHeader('Content-Type', 'application/json; charset=utf-8');
    res.setHeader('Cache-Control', 'public, max-age=3600');
    res.status(200).json({
      success: true,
      audio: base64Audio,
      provider: speech.provider,
      model: speech.model,
      targetLanguage: speech.language,
      voice: speech.voice,
      style: speech.style,
      speed: speed,
      cached: speech.cached,
      version: ENGINE_VERSION,
      fallback: 'None (en-US fallback strictly disallowed)'
    });
  } catch (err) {
    console.error('[Vercel Serverless TTS Error]', err.message);
    res.status(500).json({
      success: false,
      error: err.message,
      provider: 'Google Gemini',
      targetLanguage: language,
      fallback: 'None (en-US fallback strictly disallowed)'
    });
  }
};
