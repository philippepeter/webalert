package com;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Arrays;

/**
 *
 */
public final class ParseAndFind {

	public final static boolean parseAndFind(String url, String attribute, String attributeValue, String... words) {
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			Element element = doc.getElementsByAttributeValue(attribute, attributeValue).get(0);
			return Arrays.stream(words).anyMatch(w->element.toString().contains(w));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}