// Baobabble Service Worker for Offline Caching
const CACHE_NAME = 'baobabble-v1';
const ASSETS_TO_CACHE = [
  '/',
  '/index.html',
  '/style.css',
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

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(ASSETS_TO_CACHE);
    })
  );
  self.skipWaiting();
});

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) => {
      return Promise.all(
        keys.filter((key) => key !== CACHE_NAME).map((key) => caches.delete(key))
      );
    })
  );
  self.clients.claim();
});

self.addEventListener('fetch', (event) => {
  event.respondWith(
    caches.match(event.request).then((cachedResponse) => {
      return cachedResponse || fetch(event.request).catch(() => {
        // Fallback to cached index for navigation
        if (event.request.mode === 'navigate') {
          return caches.match('/index.html');
        }
      });
    })
  );
});
