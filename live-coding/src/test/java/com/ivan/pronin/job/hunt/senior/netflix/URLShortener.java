package com.ivan.pronin.job.hunt.senior.netflix;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

}
