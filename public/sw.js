// Baobabble Service Worker v3 - Gemini 3.8 Flash TTS & PWA Support
const CACHE_NAME = 'baobabble-v3-gemini38-tone';

const ASSETS_TO_CACHE = [
  '/',
  '/index.html',
  '/style.css',
  '/manifest.json',
  '/og-image.png',
  '/icon-512.png',
  '/robots.txt',
  '/sitemap.xml',
  '/about.html',
  '/contact.html',
  '/privacy.html',
  '/terms.html',
  '/learn-yoruba.html',
  '/learn-swahili.html',
  '/learn-igbo.html',
  '/learn-hausa.html',
  '/learn-twi.html',
  '/learn-zulu.html',
  '/african-language-pronunciation.html',
  '/african-language-learning.html',
  '/yoruba-for-beginners.html',
  '/igbo-for-beginners.html',
  '/hausa-for-beginners.html'
];

// Precache essential assets on install
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(ASSETS_TO_CACHE);
    }).then(() => self.skipWaiting())
  );
});

// Clean up old caches on activate
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames.map((cache) => {
          if (cache !== CACHE_NAME) {
            console.log('[ServiceWorker] Invalidating old cache:', cache);
            return caches.delete(cache);
          }
        })
      );
    }).then(() => self.clients.claim())
  );
});

// Fetch with Network-First strategy for dynamic API / TTS, Stale-While-Revalidate for static assets
self.addEventListener('fetch', (event) => {
  const url = new URL(event.request.url);

  // Bypass service worker cache for API requests (/api/tts)
  if (url.pathname.startsWith('/api/')) {
    event.respondWith(fetch(event.request));
    return;
  }

  event.respondWith(
    caches.match(event.request).then((cachedResponse) => {
      if (cachedResponse) {
        // Return cached and revalidate in background
        fetch(event.request).then((networkResponse) => {
          if (networkResponse && networkResponse.status === 200) {
            caches.open(CACHE_NAME).then((cache) => cache.put(event.request, networkResponse));
          }
        }).catch(() => {});
        return cachedResponse;
      }
      return fetch(event.request);
    })
  );
});
