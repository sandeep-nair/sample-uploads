/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.bootstrap.app.dao;

import com.spring.bootstrap.app.bean.FeedData;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sandeep.s
 */
@Repository
@Transactional
public class FeedDao {

    @Autowired
    private SessionFactory sessionFactory;

    public FeedData saveData(FeedData feedData) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createSQLQuery("insert into feeddata (author, category, content, datahash, description, feedurl, image, published, title) "
                + "values (:author, :category, :content, :datahash, "
                + ":description, :feedurl, :image, :published, :title) ON DUPLICATE KEY UPDATE title=:title");
        query.setParameter("author", feedData.getAuthor());
        query.setParameter("category", feedData.getCategory());
        query.setParameter("content", feedData.getContent());
        query.setParameter("datahash", feedData.getDatahash());
        query.setParameter("description", feedData.getDescription());
        query.setParameter("feedurl", feedData.getFeedurl());
        query.setParameter("image", feedData.getImage());
        query.setParameter("published", feedData.getPublished());
        query.setParameter("title", feedData.getTitle());
        query.executeUpdate();

//        session.save(feedData);
        return feedData;
    }

    public List<FeedData> getData() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery("select * from feeddata").addEntity(FeedData.class);
        List<FeedData> list = (List<FeedData>) query.list();
        System.out.println("com.spring.bootstrap.app.dao.FeedDao.getData():" + list.size());
        return list;
    }

    public FeedData getFeedData(int id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery("select * from feeddata where id = " + id).addEntity(FeedData.class);
        FeedData data = (FeedData) query.uniqueResult();
        return data;
    }

    public List<FeedData> getPagedData(int pageNo, int limit) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery("select * from feeddata order by published desc LIMIT " + (pageNo * limit) + "," + limit).addEntity(FeedData.class);
        List<FeedData> list = (List<FeedData>) query.list();
        System.out.println("com.spring.bootstrap.app.dao.FeedDao.getPagedData():" + list.size());
        return list;
    }
}
