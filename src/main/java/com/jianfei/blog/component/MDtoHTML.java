package com.jianfei.blog.component;

import java.util.Arrays;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class MDtoHTML {
    public static String convert(String origin){
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS,
            Arrays.asList(
            StrikethroughExtension.create(), // 打ち消し線に対応
            TablesExtension.create(), // テーブルに対応
            TocExtension.create() // [TOC] の部分に目次を生成する
        ));
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(origin);
        String html = renderer.render(document);
        //System.out.println(html);
        return html;
    }
}
