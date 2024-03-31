package com.springboot.petProject.util;

import org.jsoup.Jsoup;

public class HtmlTextUtil {
    public static String extractTextFromHtml(String html) {
        return Jsoup.parse(html).text();
    }
}
