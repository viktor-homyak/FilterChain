package com.tutorials.Filter;

import com.tutorials.DBBroker;
import com.tutorials.entity.AnimalEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by vhomyak on 19.05.2017.
 */
public class SortFilter implements Filter {
    Session session;
    private Filter nextFilter;


    public SortFilter(DBBroker broker) {
        this.session =  broker.getConnection();
    }

    @Override
    public void execute(AnimalEntity animal) {
        System.out.println("starting to execute SortFilter");
        Transaction tx = session.beginTransaction();
        animal.setName("Was changed ");
        session.update(animal);
        tx.commit();
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
