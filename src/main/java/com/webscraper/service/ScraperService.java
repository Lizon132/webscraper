package com.webscraper.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperService {

    public List<Guide> scrapeData(String url) throws IOException {
        List<Guide> guides = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        Elements states = doc.select("h3");
        for (Element state : states) {
            String stateName = state.text();
            Elements links = state.nextElementSibling().select("a");
            for (Element link : links) {
                String location = link.text();
                String guideUrl = link.absUrl("href");
                guides.addAll(scrapeGuidePage(stateName, location, guideUrl));
            }
        }
        return guides;
    }

    private List<Guide> scrapeGuidePage(String state, String location, String url) throws IOException {
        List<Guide> guides = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements rows = doc.select("table tbody tr");

        for (Element row : rows) {
            Guide guide = new Guide();
            guide.setState(state);
            guide.setLocation(location);
            guide.setName(row.selectFirst("h4 a").text());
            guide.setCaptain(row.selectFirst("td > p").text());
            guide.setPhone(row.selectFirst("a[href^=tel:]").text());
            guide.setEmail(row.selectFirst("a[href^=mailto:]").text());
            guide.setWebsite(row.select("a[target=_blank][rel=nofollow]").stream()
                    .filter(e -> !e.attr("href").contains("facebook.com"))
                    .map(e -> e.absUrl("href"))
                    .findFirst()
                    .orElse(""));
            guide.setFacebook(row.select("a[href*=facebook.com]").stream()
                    .map(e -> e.absUrl("href"))
                    .findFirst()
                    .orElse(""));
            guide.setProfileUrl(row.selectFirst(".btn-primary").absUrl("href"));

            guides.add(guide);
        }
        return guides;
    }
}
