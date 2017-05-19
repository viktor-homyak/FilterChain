package com.tutorials;

import com.tutorials.entity.AnimalEntity;
import com.tutorials.entity.LimbEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by Viktor on 16.05.2017.
 */
public class DBBroker {

    private SessionFactory sessionFactory;
    private Configuration configuration;

    public DBBroker() {
    }

    public Session getConnection() {
        return sessionFactory.openSession();
    }

    public void init() {


        configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();

        Session session = sessionFactory.openSession();



    }




}
