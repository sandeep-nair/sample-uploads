/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.bootstrap.app.util;

import com.spring.bootstrap.app.bean.Data;
import java.io.IOException;
import org.jsoup.Jsoup;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

/**
 *
 * @author sandeep.s
 */
public class RssFeedLoader {

    private final String dateFormatPatterns[] = new String[]{"EEE, d MMM yyyy HH:mm:ss Z",
        "yyyy-MM-dd'T'HH:mm:ss", "d MMM yyyy, HH:mm", "yyyy-MM-dd'T'HH:mm:ssz",
        "dd MMM yyyy, HH:mm", "yyyy.MM.dd G 'at' HH:mm:ss z", "yyMMddHHmmssZ",
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"};

    HashMap<String, String> checksumMap;
    String currentURL = null;
    Random random;

    private RssFeedLoader() {
        checksumMap = new HashMap<>();
        random = new Random();
    }
    private static RssFeedLoader feedLoader;

    public static RssFeedLoader getInstance() {
        if (feedLoader == null) {
            feedLoader = new RssFeedLoader();
        }
        return feedLoader;
    }

    private String readInpuStream(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "UTF-8");
        for (;;) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0) {
                break;
            }
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    public ArrayList<Data> loadRSSUsingJsoup(String urlString) throws Exception {
        this.currentURL = urlString;
        System.out.println("Loading..." + urlString);
        ArrayList<Data> results = new ArrayList<>();

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
        connection.setReadTimeout(60000);
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER) {

                String newUrl = connection.getHeaderField("Location");
                // get the cookie if need, for login
                String cookies = connection.getHeaderField("Set-Cookie");
                // open the new connnection again
                connection = (HttpURLConnection) new URL(newUrl).openConnection();
                connection.setRequestProperty("Cookie", cookies);
                connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
                connection.addRequestProperty("Referer", "google.com");
            }
        }
        InputStream inputStream = connection.getInputStream();

        org.jsoup.nodes.Document document = Jsoup.parse(readInpuStream(inputStream), urlString, Parser.xmlParser());
        if (document == null) {
            System.out.println("Document is null");
        }
        org.jsoup.nodes.Element dateElement = document.getElementsByTag("lastBuildDate").first();
        String rssDate = "";
        String currentRSSDate = checksumMap.get(currentURL);
        if (dateElement != null) {
            rssDate = dateElement.text();
        } else {
            dateElement = document.getElementsByTag("updated").first();
            if (dateElement != null) {
                rssDate = dateElement.text();
            }
        }
        if (!rssDate.equalsIgnoreCase(currentRSSDate)) {
            checksumMap.put(currentURL, rssDate);
            Elements items = document.getElementsByTag("item");
            if (items.isEmpty()) {
                items = document.getElementsByTag("entry");
            }
            results = processJsoupItem(items);
        }
        return results;
    }

    public ArrayList<Data> processJsoupItem(Elements items) {
        ArrayList<Data> results = new ArrayList<>();
        for (org.jsoup.nodes.Element e : items) {
            String title, link, pubDate, published, description, category, dc_creator, author, thumbnail, media_thumbnail, content_encoded, content, summary;
            title = e.getElementsByTag("title").isEmpty() ? null : e.getElementsByTag("title").first().text();
            link = e.getElementsByTag("link").isEmpty() ? null : e.getElementsByTag("link").first().text();
            pubDate = e.getElementsByTag("pubDate").isEmpty() ? null : e.getElementsByTag("pubDate").first().text();
            published = e.getElementsByTag("published").isEmpty() ? null : e.getElementsByTag("published").first().text();
            description = e.getElementsByTag("description").isEmpty() ? null : e.getElementsByTag("description").first().text();
//            category = e.getElementsByTag("category").isEmpty() ? null : e.getElementsByTag("category").text();
            category = "";
            dc_creator = e.getElementsByTag("dc:creator").isEmpty() ? null : e.getElementsByTag("dc:creator").first().text();
            author = e.getElementsByTag("author").isEmpty() ? null : e.getElementsByTag("author").text();
            thumbnail = e.getElementsByTag("thumbnail").isEmpty() ? null : e.getElementsByTag("thumbnail").first().text();
            if (thumbnail == null || thumbnail.isEmpty()) {
                thumbnail = e.getElementsByTag("thumbnail").isEmpty() ? null : e.getElementsByTag("thumbnail").first().attr("url");
            }
            media_thumbnail = e.getElementsByTag("media:thumbnail").isEmpty() ? null : e.getElementsByTag("media:thumbnail").first().text();
            if (media_thumbnail == null || media_thumbnail.isEmpty()) {
                media_thumbnail = e.getElementsByTag("media:thumbnail").isEmpty() ? null : e.getElementsByTag("media:thumbnail").first().attr("url");
            }
            content_encoded = e.getElementsByTag("content:encoded").isEmpty() ? null : e.getElementsByTag("content:encoded").first().text();
            content = e.getElementsByTag("content").isEmpty() ? null : e.getElementsByTag("content").first().text();
            summary = e.getElementsByTag("summary").isEmpty() ? null : e.getElementsByTag("summary").first().text();

            Data data = new Data();
            data.setTitle(title);
            if (link == null || link.isEmpty()) {
                link = e.getElementsByTag("link").first().attr("href");
            }
            data.setUrl(link);
            for (org.jsoup.nodes.Element element : e.getElementsByTag("category")) {
                category = category + "," + element.text();
            }
//            System.out.println("title:" + title);
//            System.out.println("link:" + link);
//            System.out.println("pubDate" + pubDate);
//            System.out.println("published" + published);
//            System.out.println("description" + description);
//            System.out.println("category" + category);
//            System.out.println("dc_creator" + dc_creator);
//            System.out.println("author:" + author);
//            System.out.println("thumbnail:" + thumbnail);
//            System.out.println("media_thumbnail:" + media_thumbnail);
//            System.out.println("content_encoded:" + content_encoded);
//            System.out.println("content:" + content);
//            System.out.println("summary:" + summary);
            if (pubDate != null) {
                data.setPublishedDate(parseDate(pubDate));
            } else if (published != null) {
                data.setPublishedDate(parseDate(published));
            }
            if (description != null) {
                data.setDesc(description);
            } else if (summary != null) {
                data.setDesc(summary);
            }
            if (category != null) {
                data.setCatergories(category);
            }
            if (dc_creator != null) {
                data.setAuthor(dc_creator);
            } else if (author != null) {
                data.setAuthor(author);
            }
            if (thumbnail != null) {
                data.setImgUrl(thumbnail);
            } else if (media_thumbnail != null) {
                data.setImgUrl(media_thumbnail);
            }

            if (content_encoded != null) {
                data.setContent(content_encoded);
            } else if (content != null) {
                data.setContent(content);
            }

            if (data.getImgUrl() == null || data.getImgUrl().isEmpty()) {
                org.jsoup.nodes.Element imgElement = Jsoup.parse(data.getDesc()).getElementsByTag("img").first();
                if (imgElement == null) {
                    imgElement = Jsoup.parse(data.getContent()).getElementsByTag("img").first();
                }
                if (imgElement != null) {
                    String imgURL = imgElement.attr("src");
                    data.setImgUrl(imgURL);
                }
            }
            if (data.getContent() == null || data.getContent().isEmpty()) {
                data.setContent(data.getDesc());
            }
            if (data.getDesc() != null && !data.getDesc().isEmpty()) {
                data.setDesc(Jsoup.parse(data.getDesc()).text());
            } else if (data.getContent() != null) {
                data.setDesc(Jsoup.parse(data.getContent()).text());
            }

            try {
                MessageDigest messageDigest = MessageDigest.getInstance("md5");
                byte[] hash = messageDigest.digest(data.getTitle().getBytes());
                String hashData = toHexString(hash);
                data.setDataHash(hashData);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (data.getTitle().length() > 65) {
                data.setTitle(data.getTitle().substring(0, 65) + "...");
            }
            if (data.getDesc().length() > 123) {
                data.setDesc(data.getDesc().substring(0, 123) + "...");
            }
            if (data.getImgUrl() == null || data.getImgUrl().trim().isEmpty()) {
                try {
                    String image = processImageFromMetaData(data.getUrl());
                    data.setImgUrl(image);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            results.add(data);
        }
        return results;
    }

    private Date parseDate(String dateString) {
        //12 Oct 2015, 09:15
        SimpleDateFormat simpleDateFormat;
        for (String format : dateFormatPatterns) {
            try {
                simpleDateFormat = new SimpleDateFormat(format);
                return simpleDateFormat.parse(dateString);
            } catch (Exception ex) {

            }
        }
        return null;
    }

    private String processImageFromMetaData(String url) throws IOException {
        String[] userAgents = new String[]{
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10; rv:33.0) Gecko/20100101 Firefox/33.0",
            "Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36",
            "Opera/9.80 (X11; Linux i686; Ubuntu/14.10) Presto/2.12.388 Version/12.16",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.75.14 (KHTML, like Gecko) Version/7.0.3 Safari/7046A194A",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.246"
        };
        
        int uagentPos = random.nextInt(7 - 0 + 1) + 0;
        System.out.println("using : "+userAgents[uagentPos]);
        org.jsoup.nodes.Document document = Jsoup.connect(url).followRedirects(true).header("User-Agent", userAgents[uagentPos]).get();
        org.jsoup.nodes.Element pngs = document.select("meta[property$=og:image]").first();
        org.jsoup.nodes.Element twpngs = document.select("meta[name$=twitter:image]").first();
        String imgSrcURL = "";
        if (pngs != null) {
            imgSrcURL = pngs.attr("content");
        }
        if (imgSrcURL.isEmpty() && twpngs != null) {
            imgSrcURL = twpngs.attr("content");
        }
        return imgSrcURL;
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
