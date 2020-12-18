package com.jianfei.blog.component;

import org.jsoup.Jsoup;

public class HTMLtoText {
    public static String convert(String html){
        return Jsoup.parse(html).text();
    }
}
