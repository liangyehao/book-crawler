package com.liang.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/3/16 10:44
 * @content
 */
public class HtmlParseUtil {

    /**
     * 处理Html
     *
     * @param url url
     */
    public static Map<String,String> handleHtml(String url) throws IOException {

        Map<String,String> map = new HashMap<>();

        //假设我们获取的HTML的字符内容如下
        String html = CrawlUtil.crawlHtml(url);

        //第一步，将字符内容解析成一个Document类
        Document doc = Jsoup.parse(html);
        map.put("书名",doc.select("h1").text());
        //第二步，根据我们需要得到的标签，选择提取相应标签的内容
        List<Node> nodes = doc.select("div[id=info]").get(0).childNodes();
        List<Node> handledNodes = new ArrayList<>();

        //去掉空节，空格和换行
        for (Node node : nodes) {
            if (!removeBrNode(node) && !removeTextNode(node)) {
                handledNodes.add(node);
            }
        }
        //解析节点成为map
        parseNodesToMap(handledNodes,map);
//        System.out.println(map);
        return map;
    }

    /**
     * 解析节点成为map
     *
     * @param nodes 节点
     * @param map   地图
     */
    private static void parseNodesToMap(List<Node> nodes,Map<String,String> map){
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if ( node instanceof Element){
                if (((Element) node).children().size()>0){
                    Elements s = ((Element) node).children();
                    List<Node> children = new ArrayList<>(s);
                    //递归
                    parseNodesToMap(children,map);
                }else
                if (isToBeKey((Element)node)) {
                    String key = ((Element) node).text()
                                  .trim().replace(":", "")
                                  .replace("：", "");
                    String value = "";
                    if ((i+1)!=nodes.size()){
                        Node nextNode = nodes.get(i + 1);
                        value = getValue(nextNode);
                    }
                    map.put(key,value);
                }
            }
        }
    }

    /**
     * 获取值
     *
     * @param node 节点
     * @return {@link String}
     */
    private static String getValue(Node node) {
        String value = "";
        if(node instanceof TextNode){
            value = ((TextNode) node).text().trim();
        }
        if(node instanceof Element){
            value = ((Element) node).text();
        }
        return value;
    }


    /**
     * 是否作为map的key
     *
     * @param element 元素
     * @return {@link Boolean}
     */
    private static Boolean isToBeKey(Element element){
        //<span class="pl">出版社:</span>
        return "span".equalsIgnoreCase(element.tagName())
                && "pl".equalsIgnoreCase(element.className());
    }

    /**
     * 删除文本节点
     *
     * @param node 节点
     * @return {@link Boolean}
     */
    private static Boolean removeTextNode(Node node){
        if (node instanceof TextNode) {
            if (node.toString().trim().length()==0) {
                return true;
            }else  if ("&nbsp;".equalsIgnoreCase(node.toString().trim())){
                return true;
            }
        }
        return false;
    }

    /**
     * 删除Br节点
     *
     * @param node 节点
     * @return {@link Boolean}
     */
    private static Boolean removeBrNode(Node node){
        if (node instanceof Element && "br".equalsIgnoreCase(((Element) node).tagName()) ) {
            return true;
        }
        return false;
    }
}
