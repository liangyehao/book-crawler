package com.liang.test;

import com.liang.utils.HtmlParseUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/3/16 20:51
 * @content
 */
public class ParseTest {
    public static void main(String[] args) throws IOException {
        String url1 = "https://book.douban.com/subject/3622904/";
        String url2 = "https://book.douban.com/subject/10452181/";
        String url3 = "https://book.douban.com/subject/1011399/";
        String url4 = "https://book.douban.com/subject/1008357/";
        String url5 = "https://book.douban.com/subject/26652288/";
        String url6 = "https://book.douban.com/subject/27174130/";
        String url7 = "https://book.douban.com/subject/1189784/";
        String url8 = "https://book.douban.com/subject/1883245/";
        String url9 = "https://book.douban.com/subject/1775691/";
        String url10 = "https://book.douban.com/subject/1007305/";

        List<Map<String, String>> books = new ArrayList<>();

        String[] urls = {url1,url2,url3,url4,url5,url6,url7,url8,url9,url10,};
        long start = System.currentTimeMillis();
        for (String url : urls) {
            Map<String, String> bookInfo = HtmlParseUtil.handleHtml(url);
            books.add(bookInfo);
        }
        long end = System.currentTimeMillis();
        System.out.println("图书信息{" +urls.length+" 本}::::::"+books);
        System.out.println("耗费时间::::::"+(end-start));
    }
}
