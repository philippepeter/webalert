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
        } catch (IOException e) {
            log.error("",e);
            return null;
        }

        Element element = doc.getElementsByAttributeValue(attribute, attributeValue).get(0);
        if(element != null) {
            return element.toString();
         } else {
            log.error("No attribute {} with value {} found in doc {}", attribute, attributeValue, doc.toString());
            return null;
        }

    }

    public final static boolean find(String text, String... words) {
        if(words == null || words.length == 0 || (words.length==1&&words[0].equals(""))) {
            return false;
        } else {
            return Arrays.stream(words).anyMatch(w -> text.contains(w));
        }

    }
}