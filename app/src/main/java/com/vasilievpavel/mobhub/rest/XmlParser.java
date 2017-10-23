package com.vasilievpavel.mobhub.rest;

import com.vasilievpavel.mobhub.rest.model.FeedEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlParser {
    public List<FeedEntry> parseFeed(String xml) {
        xml = normalizeXml(xml);
        Document doc = Jsoup.parse(xml);
        List<FeedEntry> feed = new ArrayList<>();
        Elements entries = doc.select("entry");
        for (Element element : entries) {
            FeedEntry entry = new FeedEntry();
            entry.setId(parseId(element));
            entry.setThumbnail(parseThumbnail(element));
            entry.setAuthor(parseAuthor(element));
            entry.setTitle(parseTitle(element));
            entry.setDate(parseDate(element));
            feed.add(entry);
        }
        return feed;
    }

    public List<Map<String, String>> parseContributions(String xml) {
        Document doc = Jsoup.parse(xml);
        Elements days = doc.select("rect");
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> map;
        for (Element day : days) {
            map = new HashMap<>();
            map.put("fill", day.attr("fill"));
            map.put("data-count", day.attr("data-count"));
            map.put("data-date", day.attr("data-date"));
            data.add(map);
        }
        return data;
    }

    private String parseAuthor(Element element) {
        return element.select("name").first().text();
    }

    private String parseId(Element element) {
        return element.select("id").first().text();
    }

    private String parseThumbnail(Element element) {
        Element thumbnail = element.select("[url]").first();
        return thumbnail.attr("url");
    }

    private String parseTitle(Element element) {
        Element title = element.select(".title").first();
        List<Node> titleNodes = title.childNodes();
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Node node : titleNodes) {
            if (node instanceof Element) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    builder.append("<b>");
                    builder.append(((Element) node).text());
                    builder.append("</b>");
                }
            } else if (node instanceof TextNode) {
                if (((TextNode) node).text().trim().length() != 0) {
                    builder.append(((TextNode) node).text());
                }
            }
        }
        return builder.toString();
    }

    private String parseDate(Element element) {
        Element datetime = element.select("div [datetime]").first();
        return datetime.attr("datetime");
    }

    private String normalizeXml(String xml) {
        xml = xml.replaceAll("&lt;", "<");
        xml = xml.replaceAll("&gt;", ">");
        xml = xml.replaceAll("&quot;", "\"");
        xml = xml.replaceAll("&amp;", "&");
        return xml;
    }
}
