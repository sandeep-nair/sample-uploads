/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.bootstrap.app.util;

import com.spring.bootstrap.app.bean.Data;
import com.spring.bootstrap.app.bean.Feeds;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;

/**
 *
 * @author sandeep.s
 */
public class FeedExecuter {

    public static void main(String[] args) {
        RssFeedLoader feedLoader = RssFeedLoader.getInstance();

        try {
            for (String url : new String[]{"http://www.huffingtonpost.com/feeds/verticals/technology/index.xml"}) {
//            for (String url : Feeds.reedURLs) {
                ArrayList<Data> dataList = feedLoader.loadRSSUsingJsoup(url);
                for (Data data : dataList) {
                    System.out.println(data);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(FeedExecuter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String processImageFromMetaData(String url) throws IOException {
        org.jsoup.nodes.Document document = Jsoup.connect(url).followRedirects(true).get();
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

    private static void connectTOURL(String URL) throws IOException {
        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
        
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
        connection.setRequestProperty("accept","text/html,application/xhtml+xml,application/xml;");
        connection.setReadTimeout(600000);
        InputStream inputStream = connection.getInputStream();
        System.out.println(connection.getContentLength());

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        System.out.println(result.toString("UTF-8"));
    }

    private static void connectJSOUPTOURL(String URL) throws IOException {
        org.jsoup.nodes.Document document = Jsoup.connect(URL).get();
        System.out.println(document.text());
    }

}
