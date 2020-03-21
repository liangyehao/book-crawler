package com.liang.controller;

import com.liang.utils.HtmlParseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/3/18 21:07
 * @content
 */
@RestController
@RequestMapping("/crawl")
public class CrawlerController {

    @GetMapping("/{isbn}")
    public String crawlByIsbn(@PathVariable("isbn") String isbn) throws IOException {
        String url = "https://book.douban.com/subject/"+isbn;
        Map<String, String> book = HtmlParseUtil.handleHtml(url);
        return book.toString();
    }

    @GetMapping("/hello")
    public String test(){
        return "hello";
    }
}
