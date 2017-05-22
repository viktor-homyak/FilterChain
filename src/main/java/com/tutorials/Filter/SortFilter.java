package com.tutorials.Filter;

import com.tutorials.DBBroker;
import com.tutorials.entity.AnimalEntity;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

/**
 * Created by vhomyak on 19.05.2017.
 */
public class SortFilter implements Filter {
    private Session session;
    private Filter nextFilter;


    public SortFilter(DBBroker broker) {
        this.session =  broker.getConnection();
    }

    @Override
    public void execute(AnimalEntity animal) {
        System.out.println(" ");

        List<Integer> listId = session.createSQLQuery("select id from animal").list();
        System.out.println("Index of our animal in db is :" +listId.indexOf(animal.getId()));
        System.out.println("starting to execute SortFilter");
        Collections.sort(listId);
        System.out.println("Index of our animal after sorting :" +listId.indexOf(animal.getId()));
        session.close();
        if(nextFilter!=null){
            nextFilter.execute(animal);
        }

        while (!session.isOpen()){
            System.out.println("SortFilter session was closed");
            break;
        }

    }

    @Override
    public void setNextFilter(Filter nextfilter) {
        this.nextFilter = nextfilter;
        System.out.println("SortFilter next filter");
    }
}
