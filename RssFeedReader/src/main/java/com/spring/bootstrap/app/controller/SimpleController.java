/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.bootstrap.app.controller;

import com.spring.bootstrap.app.bean.FeedData;
import com.spring.bootstrap.app.dao.FeedDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *
 * @author sandeep.s
 */
@Controller
@RequestMapping("/simple")
public class SimpleController {
    
    @Autowired
    FeedDao feedDao;
    
    @RequestMapping(value="/index",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String index(Model model) {
        return "index";
    }
    
    @RequestMapping(value="/index/{pgno}",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<FeedData> getFirst(@PathVariable("pgno") int pageNo, Model model) {
        System.out.println("PageNo:"+pageNo);
        //Because page starts from 1
        List<FeedData> feedData = feedDao.getPagedData(pageNo-1,9);
        return feedData;
    }
    
    @RequestMapping(value="/feed/{id}",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public FeedData getFeed(@PathVariable("id") int feed, Model model) {
        System.out.println("feed:"+feed);
        FeedData feedData = feedDao.getFeedData(feed);
        return feedData;
    }
    
    
}
