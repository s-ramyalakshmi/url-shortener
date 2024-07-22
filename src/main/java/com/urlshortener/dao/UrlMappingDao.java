package com.urlshortener.dao;

import com.urlshortener.data.UrlMapping;
import com.urlshortener.jooq.tables.tables.records.UrlMappingRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.urlshortener.jooq.tables.tables.UrlMapping.URL_MAPPING;

@Repository
public class UrlMappingDao {

    @Autowired
    private DSLContext dsl;

    // Fetch an existing short URL for a given long URL
    public Optional<UrlMapping> getUrlMappingByShortUrl(String shortUrl) {
        UrlMappingRecord record = dsl.selectFrom(URL_MAPPING)
                .where(URL_MAPPING.SHORT_URL.eq(shortUrl))
                .fetchOne();
        if (record == null) {
            return Optional.empty();
        }
        return Optional.of(new UrlMapping(
                record.getId(),
                record.getLongUrl(),
                record.getShortUrl(),
                record.getCreatedAt(),
                record.getExpiryDate()
        ));
    }


    // Insert a new URL mapping
    public void insertUrlMapping(String longUrl, String shortUrl, LocalDateTime expiryDate) {
        dsl.insertInto(URL_MAPPING,
                        URL_MAPPING.LONG_URL,
                        URL_MAPPING.SHORT_URL,
                        URL_MAPPING.CREATED_AT,
                        URL_MAPPING.EXPIRY_DATE)
                .values(longUrl, shortUrl, LocalDateTime.now(), expiryDate)
                .execute();
    }

    public void save(UrlMapping urlMapping) {
        dsl.insertInto(URL_MAPPING)
                .set(URL_MAPPING.SHORT_URL, urlMapping.getShortURL())
                .set(URL_MAPPING.LONG_URL, urlMapping.getLongURL())
                .set(URL_MAPPING.CREATED_AT, urlMapping.getCreatedAt())
                .set(URL_MAPPING.EXPIRY_DATE, urlMapping.getExpiryDate())
                .execute();
    }

    public Optional<UrlMapping> getUrlMappingByLongUrl(String longUrl) {
        UrlMappingRecord record = dsl.selectFrom(URL_MAPPING)
                .where(URL_MAPPING.LONG_URL.eq(longUrl))
                .fetchOne();

        if (record != null) {
            return Optional.of(new UrlMapping(
                    record.getId(), // Convert Integer to Long
                    record.getLongUrl(),
                    record.getShortUrl(),
                    record.getCreatedAt(),
                    record.getExpiryDate()
            ));
        }
        return Optional.empty();
    }

}
