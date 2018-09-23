package com;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Arrays;

/**
 *
 */
@Slf4j
public final class ParseAndFind {

    public final static String parseAndGet(String url, String attribute, String attributeValue) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            Element element = doc.getElementsByAttributeValue(attribute, attributeValue).get(0);
            return element.toString();
        } catch (IOException e) {
            log.error("",e);
        }
        return null;
    }

    public final static boolean find(String text, String... words) {
        return Arrays.stream(words).anyMatch(w -> text.contains(w));

    }
}