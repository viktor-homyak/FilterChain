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
        this.broker = broker;
    }


    @Override
    public void execute(AnimalEntity animal) {
        Session session = broker.getConnection();

        Query meanQuery2 = session.createSQLQuery("SELECT\n" +
                "  sum(rv*probability) as mathmean,\n" +
                "  sum(power(rv,2)*probability) as squaremathmean\n" +
                "FROM (SELECT\n" +
                "        height                                                                   AS rv,\n" +
                "        (cast(count(id) AS DOUBLE PRECISION) / cast(100000 AS DOUBLE PRECISION)) AS probability\n" +
                "      FROM animal\n" +
                "      GROUP BY height) AS foo");
        List<Object> mean2 = meanQuery2.list();

        Object[] row= (Object[]) mean2.get(0);

        Double mathMean = (Double) row[0];

        System.out.println("Math mean of heights occurrences would be(sql) " +  mathMean);

        Double dispersion = (Double)row[1]- mathMean*mathMean;
        System.out.println("dispersion of heights occurrences would be " + dispersion);

        session.close();

        if (nextFilter != null) {
            nextFilter.execute(animal);
        }


    }

    @Override
    public void setNextFilter(Filter nextfilter) {
        this.nextFilter = nextfilter;
        System.out.println("StatisticsFilter next filter");
    }

}