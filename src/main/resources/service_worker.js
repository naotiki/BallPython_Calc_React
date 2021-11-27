// Service Worker のバージョンとキャッシュする App Shell を定義する

const NAME = 'BallPython_Morph';
const VERSION = '004';
const CACHE_NAME = NAME + VERSION;
const urlsToCache = [
    './index.html',
    './untitled1.js',
    './GeneticPairs.json',
    './Genetics.json',
    './calc.js',
    '/',
];

// Service Worker へファイルをインストール

self.addEventListener('install', function (event) {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(function (cache) {
                console.log('Opened cache');
                return cache.addAll(urlsToCache);
            })
    );
});

// リクエストされたファイルが Service Worker にキャッシュされている場合
// キャッシュからレスポンスを返す

self.addEventListener('fetch', function (e) {
    console.log('fetch', e.request.url);
    e.respondWith(
        caches.match(e.request, {
            ignoreSearch:true
        })
            .then(response => {
                return response || fetch(e.request);
            })
    );

});

// Cache Storage にキャッシュされているサービスワーカーのkeyに変更があった場合
// 新バージョンをインストール後、旧バージョンのキャッシュを削除する
// (このファイルでは CACHE_NAME をkeyの値とみなし、変更を検知している)

self.addEventListener('activate', event => {
    event.waitUntil(
        caches.keys().then(keys => Promise.all(
            keys.map(key => {
                if (!CACHE_NAME.includes(key)) {
                    return caches.delete(key);
                }
            })
        )).then(() => {
            console.log(CACHE_NAME + "activated");
        })
    );
});
