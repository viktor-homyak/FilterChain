package com.tutorials.Filter;

import com.tutorials.DBBroker;
import com.tutorials.entity.AnimalEntity;
import org.hibernate.Session;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * Created by vhomyak on 19.05.2017.
 */
public class StatisticsFilter implements Filter {
    private Filter nextFilter;
    private DBBroker broker;

    public StatisticsFilter(DBBroker broker) {
        this.broker =  broker;
    }


    @Override
    public void execute(AnimalEntity animal) {
        Session session=broker.getConnection();
        System.out.println(" ");

        System.out.println("starting to execute StatisticsFilter");
        List<AnimalEntity> listHeight = session.createSQLQuery("select * from animal").list();

        Map<Integer,List<AnimalEntity>> groupedHeight = listHeight.stream().collect(Collectors.groupingBy(AnimalEntity::getHeight));
           Map<Integer,Double> mean = new HashMap<>();

        groupedHeight.values().forEach(l->{
            mean.put(l.get(0).getHeight(), (double) (l.size()/10000));
        });
//TODO this must be =1. If it is, we will find math mean, and then deviation
        double result = mean.values().stream().mapToDouble(Double::valueOf).sum();



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
