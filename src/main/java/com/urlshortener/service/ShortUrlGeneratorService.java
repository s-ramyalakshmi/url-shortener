package com.urlshortener.service;

import com.urlshortener.dao.UrlMappingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
public class ShortUrlGeneratorService {

    @Autowired
    private UrlMappingDao urlMappingDao;
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String encode(long value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(BASE62.charAt((int)(value % 62)));
            value /= 62;
        }
        return sb.reverse().toString();
    }

    public static long decode(String base62) {
        long result = 0;
        for (char c : base62.toCharArray()) {
            result = result * 62 + BASE62.indexOf(c);
        }
        return result;
    }

    public String generateShortUrl(String longUrl) throws NoSuchAlgorithmException {
        String shortUrl;
        long suffix = 0;
        do {
            long numericValue = hashToNumber(longUrl) + suffix;
            shortUrl = encode(numericValue);
            suffix++;
        } while (urlMappingDao.getUrlMappingByShortUrl(shortUrl).isPresent());

        return shortUrl;
    }

    private long hashToNumber(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes());
        return new BigInteger(1, Arrays.copyOfRange(hash, 0, 8)).longValue() & Long.MAX_VALUE;
    }
}
