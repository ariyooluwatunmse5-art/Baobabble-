const http = require('http');
const fs = require('fs');
const path = require('path');
const { getOrSynthesizeSpeech, VOICE_CONFIG, ENGINE_VERSION } = require('./tts-engine');

const PORT = 3000;
const PUBLIC_DIR = path.join(__dirname, 'web');

const MIME_TYPES = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon',
  '.xml': 'application/xml; charset=utf-8',
  '.txt': 'text/plain; charset=utf-8',
  '.webp': 'image/webp',
  '.wav': 'audio/wav',
  '.mp3': 'audio/mpeg'
};

const server = http.createServer(async (req, res) => {
  // CORS & caching headers
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Accept');
  res.setHeader('X-Content-Type-Options', 'nosniff');

  if (req.method === 'OPTIONS') {
    res.writeHead(204);
    res.end();
    return;
  }

  const parsedUrl = new URL(req.url, `http://${req.headers.host || 'localhost'}`);

  // --- API ENDPOINT: /api/tts ---
  if (parsedUrl.pathname === '/api/tts') {
    let text = parsedUrl.searchParams.get('text');
    let language = parsedUrl.searchParams.get('lang') || parsedUrl.searchParams.get('language') || 'yoruba';
    let isSlow = parsedUrl.searchParams.get('slow') === 'true';
    let speedParam = parsedUrl.searchParams.get('speed');
    let speed = speedParam ? parseFloat(speedParam) : (isSlow ? 0.70 : 1.00);
    let format = parsedUrl.searchParams.get('format') || '';

    // If POST request, parse body
    if (req.method === 'POST') {
      try {
        const bodyStr = await new Promise((resolve, reject) => {
          let chunks = [];
          req.on('data', c => chunks.push(c));
          req.on('end', () => resolve(Buffer.concat(chunks).toString()));
          req.on('error', reject);
        });
        if (bodyStr) {
          const body = JSON.parse(bodyStr);
          if (body.text) text = body.text;
          if (body.lang || body.language) language = body.lang || body.language;
          if (body.speed !== undefined) speed = parseFloat(body.speed);
          else if (body.slow !== undefined || body.isSlow !== undefined) isSlow = !!(body.slow || body.isSlow);
          if (body.format) format = body.format;
        }
      } catch (err) {
        console.warn('POST body parse error:', err.message);
      }
    }

    if (!text || !text.trim()) {
      res.writeHead(400, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify({ error: 'Text parameter is required for TTS' }));
      return;
    }

    try {
      const speech = await getOrSynthesizeSpeech(text, language, speed);

      if (format === 'wav' || req.headers.accept?.includes('audio/wav')) {
        res.writeHead(200, {
          'Content-Type': 'audio/wav',
          'Content-Length': speech.wav.length,
          'Cache-Control': 'public, max-age=86400',
          'X-TTS-Provider': speech.provider,
          'X-TTS-Model': speech.model,
          'X-TTS-Voice': speech.voice,
          'X-TTS-Cached': speech.cached ? 'true' : 'false',
          'X-TTS-Version': ENGINE_VERSION
        });
        res.end(speech.wav);
        return;
      }

      // Return JSON with base64 data URL and detailed debug info
      const base64Audio = `data:audio/wav;base64,${speech.wav.toString('base64')}`;
      const responseData = {
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
      };

      res.writeHead(200, {
        'Content-Type': 'application/json; charset=utf-8',
        'Cache-Control': 'public, max-age=3600'
      });
      res.end(JSON.stringify(responseData));
      return;
    } catch (err) {
      console.error('[TTS Error]', err.message);
      res.writeHead(500, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({
        success: false,
        error: err.message,
        provider: 'Google Gemini',
        targetLanguage: language,
        fallback: 'None (en-US fallback strictly disallowed)'
      }));
      return;
    }
  }

  // --- STATIC FILE SERVING ---
  let cleanPath = decodeURIComponent(parsedUrl.pathname);

  if (cleanPath === '/') {
    cleanPath = '/index.html';
  }

  let filePath = path.join(PUBLIC_DIR, cleanPath);

  // If path is a directory, check for index.html inside
  if (fs.existsSync(filePath) && fs.statSync(filePath).isDirectory()) {
    filePath = path.join(filePath, 'index.html');
  }

  // If file doesn't exist, check if appending .html works
  if (!fs.existsSync(filePath) && fs.existsSync(filePath + '.html')) {
    filePath = filePath + '.html';
  }

  // If still not found, check if it's an SPA route like /learn/... -> serve web/index.html
  if (!fs.existsSync(filePath)) {
    filePath = path.join(PUBLIC_DIR, 'index.html');
  }

  fs.stat(filePath, (err, stats) => {
    if (err || !stats.isFile()) {
      res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
      res.end('404 Not Found');
      return;
    }

    const ext = path.extname(filePath).toLowerCase();
    const contentType = MIME_TYPES[ext] || 'application/octet-stream';

    res.writeHead(200, {
      'Content-Type': contentType,
      'Content-Length': stats.size
    });

    const stream = fs.createReadStream(filePath);
    stream.pipe(res);
  });
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`Baobabble web server listening on port ${PORT} with Gemini 3.8 Flash TTS (${ENGINE_VERSION})`);
});
