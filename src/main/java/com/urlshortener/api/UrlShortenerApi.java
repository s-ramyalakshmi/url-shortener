package com.urlshortener.api;

import com.urlshortener.dao.UrlMappingDao;
import com.urlshortener.data.UrlMapping;
import com.urlshortener.service.ShortUrlGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UrlShortenerApi {

    @Autowired
    private ShortUrlGeneratorService shortUrlGeneratorService;

    @Autowired
    private UrlMappingDao urlMappingDao;

    @GetMapping("/shorten")
    public ResponseEntity<UrlMapping> getShortUrl(@RequestParam String longUrl) throws NoSuchAlgorithmException {
        Optional<UrlMapping> existingMapping = urlMappingDao.getUrlMappingByLongUrl(longUrl);

        if (existingMapping.isPresent()) {
            return ResponseEntity.ok(existingMapping.get());
        }

        String shortUrl = shortUrlGeneratorService.generateShortUrl(longUrl);
        LocalDateTime expiryDate = LocalDateTime.now().plusYears(5);

        UrlMapping newMapping = new UrlMapping(null, longUrl, shortUrl, LocalDateTime.now(), expiryDate);
        urlMappingDao.save(newMapping);

        return ResponseEntity.ok(newMapping);
    }

    @GetMapping("/redirect")
    public ResponseEntity<Void> redirectToLongUrl(@RequestParam String shortUrl) {
        Optional<UrlMapping> urlMapping = urlMappingDao.getUrlMappingByShortUrl(shortUrl);
        if (urlMapping.isPresent() && !urlMapping.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", urlMapping.get().getLongURL())
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/details")
    public ResponseEntity<UrlMapping> getUrlDetails(@RequestParam String shortUrl) {
        Optional<UrlMapping> urlMapping = urlMappingDao.getUrlMappingByShortUrl(shortUrl);
        return urlMapping.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
