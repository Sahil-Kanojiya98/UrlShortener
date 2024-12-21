package com.api.urlshortener.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UrlShortenerService {

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);

    private final StringRedisTemplate redisTemplate;
    private final String urlPrefix;

    public UrlShortenerService(StringRedisTemplate redisTemplate,
                               @Value("${url_short.prefix}") String urlPrefix) {
        this.redisTemplate = redisTemplate;
        this.urlPrefix = urlPrefix;
    }

    public String shortenUrl(String originalUrl){
        if (!(originalUrl.startsWith("http://") || originalUrl.startsWith("https://"))) {
            originalUrl="http://"+originalUrl;
        }

        String encodedUrl = Base64.getUrlEncoder().encodeToString(originalUrl.getBytes(StandardCharsets.UTF_8));
        String shortUrl = urlPrefix + encodedUrl;

        redisTemplate.opsForValue().set(encodedUrl, originalUrl, 15, TimeUnit.DAYS);
        logger.info("Shortened URL: {} for original URL: {}", shortUrl, originalUrl);
        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        logger.info("Retrieving original URL for: {}", shortUrl);
        String encodedUrl = shortUrl.replace(urlPrefix, "");
        return redisTemplate.opsForValue().get(encodedUrl);
    }

}
