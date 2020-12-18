package com.jianfei.blog.component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstParagraph {
	private static final Pattern TAG_REGEX = Pattern.compile("<p>(.+?)</p>", Pattern.DOTALL);

    public static String get(final String html) {
    final Matcher matcher = TAG_REGEX.matcher(html);
    matcher.find();
    return matcher.group(1);
    }
}
