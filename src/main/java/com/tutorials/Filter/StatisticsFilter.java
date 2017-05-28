package com.tutorials.Filter;

import com.tutorials.DBBroker;
import com.tutorials.entity.AnimalEntity;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
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
//        System.out.println(" ");
//
//        System.out.println("starting to execute StatisticsFilter");
        List<Integer> heights = session.createSQLQuery("select height from animal GROUP BY height").list();
        Map<Integer,Double> heightProbabilities = new HashMap<>();

        heights.forEach(height-> {
            Query probability = session.createSQLQuery("select count(id)  from animal where height=:param GROUP BY height ").setParameter("param", height);
            heightProbabilities.put(height, Double.valueOf(probability.uniqueResult().toString()) / 100000);
        });

            double mathMean = heightProbabilities
                    .keySet()
                    .stream()
                    .map(h -> h * heightProbabilities.get(h))
                    .mapToDouble(Number::doubleValue)
                    .sum();
            System.out.println("Math mean of heights occurrences would be " + mathMean);

           double squareMathMean = heightProbabilities
                   .keySet()
                   .stream()
                   .map(h -> Math.pow(2,h * heightProbabilities.get(h)))
                   .mapToDouble(Number::doubleValue)
                   .sum();
           double dispersion = Math.pow(2,mathMean)+ squareMathMean;
        System.out.println("dispersion of heights occurrences would be " + dispersion);

            session.close();

            if (nextFilter != null) {
                nextFilter.execute(animal);
            }

            while (!session.isOpen()) {
         //       System.out.println("StatisticsFilter session was closed");
                break;
            }
        }

            @Override
            public void setNextFilter (Filter nextfilter){
                this.nextFilter = nextfilter;
                System.out.println("StatisticsFilter next filter");
            }

        }