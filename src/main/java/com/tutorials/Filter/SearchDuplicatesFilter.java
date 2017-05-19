package com.tutorials.Filter;

import com.tutorials.DBBroker;
import com.tutorials.entity.AnimalEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by vhomyak on 19.05.2017.
 */
public class SearchDuplicatesFilter implements Filter {

    Session session;
    private Filter nextFilter;

    public SearchDuplicatesFilter(DBBroker broker) {
        this.session =  broker.getConnection();
    }

    @Override
    public void execute(AnimalEntity animal) {
        System.out.println("Starting to execute SearchDuplicatesFilter");

       Query query= session.createSQLQuery("select count(*) from animal where name IN (SELECT name from animal where id=:param)")
                .setParameter("param", animal.getId());

        System.out.println("There is ["+ query.uniqueResult().toString()+ "] duplicates of '" + animal.getName()+ "'");
        session.close();

        if(nextFilter!=null){
            nextFilter.execute(animal);
        }

        while (!session.isOpen()){
            System.out.println("SearchDuplicatesFilter session was closed");
            break;
        }

    }

    @Override
    public void setNextFilter(Filter nextfilter) {
        this.nextFilter = nextfilter;
        System.out.println("SearchDuplicatesFilter next filter");
    }
}
