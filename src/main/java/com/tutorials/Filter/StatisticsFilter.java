package com.tutorials.Filter;

import com.tutorials.DBBroker;
import com.tutorials.entity.AnimalEntity;
import org.hibernate.Session;

/**
 * Created by vhomyak on 19.05.2017.
 */
public class StatisticsFilter implements Filter {
    Session session;
    private Filter nextFilter;

    public StatisticsFilter(DBBroker broker) {
        this.session =  broker.getConnection();
    }


    @Override
    public void execute(AnimalEntity animal) {
        System.out.println("execute StatisticsFilter");
        session.close();

        if(nextFilter!=null){
            nextFilter.execute(animal);
        }

        while (!session.isOpen()){
            System.out.println("StatisticsFilter session was closed");
            break;
        }
    }

    @Override
    public void setNextFilter(Filter nextfilter) {
        this.nextFilter = nextfilter;
        System.out.println("StatisticsFilter next filter");
    }
}
