package com.tutorials.Filter;

import com.tutorials.DBBroker;
import com.tutorials.entity.AnimalEntity;
import com.tutorials.entity.LimbEntity;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by vhomyak on 19.05.2017.
 */
public class SortFilter implements Filter {
    private Filter nextFilter;
    private DBBroker broker;

    public SortFilter(DBBroker broker) {
        this.broker =  broker;
    }

    @Override
    public void execute(AnimalEntity animal) {
        Session session = broker.getConnection();


        System.out.println(" ");
         List<LimbEntity> animalLimbs = animal.getLimbs();

          animalLimbs.forEach(limbEntity -> System.out.println("Order of "+limbEntity.getName()+" before sorting :" + limbEntity.getOrder()));

        Collections.sort(animalLimbs, new Comparator<LimbEntity>() {
            @Override
            public int compare(LimbEntity o1, LimbEntity o2) {
                int result = 0;
                if(o1.getOrder()<o2.getOrder())
                    result= 1;
                if(o1.getOrder()>o2.getOrder())
                    result= -1;
                if(o1.getOrder()==o2.getOrder())
                    result = 0;

                return result;
            }
        });
        System.out.println(" ");
        animalLimbs.forEach(limbEntity -> System.out.println("Order of "+limbEntity.getName()+" after sorting :" + limbEntity.getOrder()));
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
