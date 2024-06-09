package com.webscraper.controller;

import com.webscraper.service.Guide;
import com.webscraper.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class ScraperController {

    @Autowired
    private ScraperService scraperService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("data", List.of());
        return "index";
    }

    @GetMapping("/scrape")
    public String scrape(@RequestParam String url, Model model) throws IOException {
        List<Guide> data = scraperService.scrapeData(url);
        model.addAttribute("data", data);
        return "index";
    }
}
