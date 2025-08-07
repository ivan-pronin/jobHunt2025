package com.ivan.pronin.job.hunt.senior.netflix;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Ivan Pronin
 * @since 24.07.2025
 */
public class URLShortener {

    private static class Base62Shortener {

        private static final String BASE_HOST = "http://short.url/";

        private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        private Map<Integer, String> idToUrl = new HashMap<>();

        private int id = 1;

        public String encode(String longUrl) {
            idToUrl.put(id, longUrl);
            return BASE_HOST + toBase62(id++);
        }

        public String decode(String shortUrl) {
            String code = shortUrl.replace(BASE_HOST, "");
            int decodedId = fromBase62(code);
            return idToUrl.get(decodedId);
        }

        private String toBase62(int num) {
            StringBuilder sb = new StringBuilder();
            while (num > 0) {
                sb.append(BASE62.charAt(num % 62));
                num /= 62;
            }
            return sb.reverse().toString();
        }

        private int fromBase62(String s) {
            int num = 0;
            for (char c : s.toCharArray()) {
                num *= 62;
                if ('0' <= c && c <= '9') num += c - '0';
                else if ('a' <= c && c <= 'z') num += c - 'a' + 10;
                else num += c - 'A' + 36;
            }
            return num;
        }

    }

    private static class MapShortener {

        private static final String BASE_HOST = "http://short.url/";

        private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        private static final int CODE_LENGTH = 6;

        private Map<String, String> codeToUrl = new HashMap<>();

        private Random rand = new Random();

        public String encode(String longUrl) {
            String code = generateCode();
            while (codeToUrl.containsKey(code)) {
                code = generateCode(); // Ensure uniqueness
            }
            codeToUrl.put(code, longUrl);
            return BASE_HOST + code;
        }

        public String decode(String shortUrl) {
            String code = shortUrl.replace(BASE_HOST, "");
            return codeToUrl.get(code);
        }

        private String generateCode() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CHARSET.charAt(rand.nextInt(CHARSET.length())));
            }
            return sb.toString();
        }

    }

    @Test
    void testBase62ShortenerEncodeDecode() {
        URLShortener.Base62Shortener shortener = new URLShortener.Base62Shortener();
        String longUrl = "https://example.com/page";
        String shortUrl = shortener.encode(longUrl);
        assertNotNull(shortUrl);
        assertEquals(longUrl, shortener.decode(shortUrl));
    }

    @Test
    void testBase62ShortenerMultipleUrls() {
        URLShortener.Base62Shortener shortener = new URLShortener.Base62Shortener();
        String url1 = "https://a.com";
        String url2 = "https://b.com";
        String short1 = shortener.encode(url1);
        String short2 = shortener.encode(url2);
        assertNotEquals(short1, short2);
        assertEquals(url1, shortener.decode(short1));
        assertEquals(url2, shortener.decode(short2));
    }

    @Test
    void testMapShortenerEncodeDecode() {
        URLShortener.MapShortener shortener = new URLShortener.MapShortener();
        String longUrl = "https://example.org/resource";
        String shortUrl = shortener.encode(longUrl);
        assertNotNull(shortUrl);
        assertEquals(longUrl, shortener.decode(shortUrl));
    }

    @Test
    void testMapShortenerUniqueness() {
        URLShortener.MapShortener shortener = new URLShortener.MapShortener();
        String url1 = "https://x.com";
        String url2 = "https://y.com";
        String short1 = shortener.encode(url1);
        String short2 = shortener.encode(url2);
        assertNotEquals(short1, short2);
        assertEquals(url1, shortener.decode(short1));
        assertEquals(url2, shortener.decode(short2));
    }

    @Test
    void testMapShortenerDecodeUnknown() {
        URLShortener.MapShortener shortener = new URLShortener.MapShortener();
        assertNull(shortener.decode("http://short.url/abcdef"));
    }

    @Test
    void testToBase62() {
        URLShortener.Base62Shortener shortener = new URLShortener.Base62Shortener();
        assertEquals("1", shortener.toBase62(1));
        assertEquals("a", shortener.toBase62(10));
        assertEquals("A", shortener.toBase62(36));
        assertEquals("10", shortener.toBase62(62));
        assertEquals("ZZ", shortener.toBase62(3843));
        assertEquals("11Wl", shortener.toBase62(245789));
    }

    @Test
    void testFromBase62() {
        URLShortener.Base62Shortener shortener = new URLShortener.Base62Shortener();
        assertEquals(1, shortener.fromBase62("1"));
        assertEquals(10, shortener.fromBase62("a"));
        assertEquals(36, shortener.fromBase62("A"));
        assertEquals(62, shortener.fromBase62("10"));
        assertEquals(3843, shortener.fromBase62("ZZ"));
        assertEquals(245789, shortener.fromBase62("11Wl"));
    }

    @Test
    void testRoundTripMy() {
        var shortener = new URLShortener.MyUrlDecoder();
        for (int i = 1; i < 10000; i += 123) {
            String code = shortener.toBase62(i);
            int decoded = shortener.fromBase62(code);

            assertEquals(i, decoded);
        }
    }

    private static class MyUrlDecoder {

        private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        private static final String BASE_HOST = "http://example.com/";

        private static final int BASE = CHARSET.length();

        private Map<String, String> shortToLongUrls = new HashMap<>();

        private Map<String, String> longToShortUrls = new HashMap<>();

        private Map<Integer, String> idToShortUrl = new HashMap<>();

        private int id = 1;

        public String encode(String longUrl) {
            if (longToShortUrls.containsKey(longUrl)) {
                return longToShortUrls.get(longUrl);
            }
            String shortUrl = BASE_HOST + toBase62(id++);
            longToShortUrls.put(longUrl, shortUrl);
            shortToLongUrls.put(shortUrl, longUrl);
            return shortUrl;
        }

        private String toBase62(int base62Sum) {
            var sb = new StringBuilder();
            int n = base62Sum;
            while (n > 0) {
                sb.append(CHARSET.charAt(n % BASE));
                n = n / BASE;
            }
            return sb.reverse().toString();
        }

        private int fromBase62(String base62) {
            int num = 0;
            for (int i = 0; i < base62.length(); i++) {
                num = num * BASE + CHARSET.indexOf(base62.charAt(i));
            }

            return num;
        }

        public String decode(String shortUrl) {
            if (shortToLongUrls.containsKey(shortUrl)) {
                return shortToLongUrls.get(shortUrl);
            }
            return null;
        }

    }

    @Test
    void testToBase62My() {
        var shortener = new URLShortener.MyUrlDecoder();
        assertEquals("1", shortener.toBase62(1));
        assertEquals("a", shortener.toBase62(10));
        assertEquals("A", shortener.toBase62(36));
        assertEquals("10", shortener.toBase62(62));
        assertEquals("ZZ", shortener.toBase62(3843));
        assertEquals("11Wl", shortener.toBase62(245789));
    }

    @Test
    void testFromBase62My() {
        var shortener = new URLShortener.MyUrlDecoder();
        assertEquals(1, shortener.fromBase62("1"));
        assertEquals(10, shortener.fromBase62("a"));
        assertEquals(36, shortener.fromBase62("A"));
        assertEquals(62, shortener.fromBase62("10"));
        assertEquals(3843, shortener.fromBase62("ZZ"));
        assertEquals(245789, shortener.fromBase62("11Wl"));
    }

    @Test
    void testRoundTrip() {
        URLShortener.Base62Shortener shortener = new URLShortener.Base62Shortener();
        for (int i = 1; i < 10000; i += 123) {
            String code = shortener.toBase62(i);
            int decoded = shortener.fromBase62(code);

            assertEquals(i, decoded);
        }
    }

    @Test
    void testMapShortenerEncodeDecodeMy() {
        var shortener = new URLShortener.MyUrlDecoder();
        String longUrl = "https://example.org/resource";
        String shortUrl = shortener.encode(longUrl);
        assertNotNull(shortUrl);
        assertEquals(longUrl, shortener.decode(shortUrl));
    }

    @Test
    void testMapShortenerUniquenessMy() {
        var shortener = new URLShortener.MyUrlDecoder();
        String url1 = "http://x.com";
        String url2 = "http://y.com";
        String short1 = shortener.encode(url1);
        String short2 = shortener.encode(url2);
        assertNotEquals(short1, short2);
        assertEquals(url1, shortener.decode(short1));
        assertEquals(url2, shortener.decode(short2));
    }

    @Test
    void testMapShortenerDecodeUnknownMy() {
        var shortener = new URLShortener.MyUrlDecoder();
        assertNull(shortener.decode("http://short.url/abcdef"));
    }


}
