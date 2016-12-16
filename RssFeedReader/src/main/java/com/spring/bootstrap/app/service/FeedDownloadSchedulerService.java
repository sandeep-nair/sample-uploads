/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.bootstrap.app.service;

import com.spring.bootstrap.app.bean.Data;
import com.spring.bootstrap.app.bean.FeedData;
import com.spring.bootstrap.app.bean.Feeds;
import com.spring.bootstrap.app.dao.FeedDao;
import com.spring.bootstrap.app.util.RssFeedLoader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author sandeep.s
 */
@Component
public class FeedDownloadSchedulerService {

    @Autowired
    FeedDao feedDao;
    
    @Scheduled(cron = "0 0/5 * * * ?")
    public void demoServiceMethod() {
        System.out.println("Running Scheduler");
        RssFeedLoader feedLoader = RssFeedLoader.getInstance();
        for(String url: Feeds.reedURLs) {
            try {
                ArrayList<Data> dataList = feedLoader.loadRSSUsingJsoup(url);
                for(Data data : dataList) {
                    feedDao.saveData(dataToFeedData(data));
                }
            } catch (Exception ex) {
                Logger.getLogger(FeedDownloadSchedulerService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private FeedData dataToFeedData(Data data) {
        FeedData feedData = new FeedData();
        feedData.setTitle(data.getTitle());
        feedData.setDescription(data.getDesc());
        feedData.setImage(data.getImgUrl());
        feedData.setPublished(data.getPublishedDate());
        feedData.setDatahash(data.getDataHash());
        feedData.setFeedurl(data.getUrl());
        feedData.setCategory(data.getCatergories());
        feedData.setContent(data.getContent());
        feedData.setAuthor(data.getAuthor());
        return feedData;
    }

}
