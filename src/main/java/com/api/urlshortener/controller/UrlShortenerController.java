package com.api.urlshortener.controller;

import com.api.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
public class UrlShortenerController {

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);

    @Autowired
    private UrlShortenerService urlShortenerService;

    @GetMapping("/")
    public String showForm(Model model) {
        return "index";
    }

    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam String url, Model model) {

        logger.info("Received URL to shorten: {}", url);
        String shortenedUrl = urlShortenerService.shortenUrl(url);
        model.addAttribute("shortenedUrl", shortenedUrl);
        return "index";
    }

    @GetMapping("/url/{shortUrl}")
    public String expandUrl(@PathVariable String shortUrl) {
        String originalUrl = urlShortenerService.getOriginalUrl(shortUrl);
        if (originalUrl != null) {
            return "redirect:" + originalUrl;
        } else {
            return "error";
        }
    }

}