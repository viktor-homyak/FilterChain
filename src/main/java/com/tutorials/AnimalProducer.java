package com.tutorials;

import com.tutorials.entity.AnimalEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

/**
 * Created by vhomyak on 31.05.2017.
 */
public class AnimalProducer {


    public static AnimalPool pool;
    public AnimalProducer() {
        super();
     //  this.pool = pool;
    }

    public static String produce() throws InterruptedException {
        return pool.produce();
    }

//    @Override
//    public String call() {
//         int counter=0;
//        while (!pool.getAnimals().isEmpty()) {
//            try {
//                produce();
//                counter++;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        return "There was " + counter+ " calls of produce()method, that was injected by thread named "
//                + Thread.currentThread().getName();
//    }


    public static void main(String[] args) {
        DBBroker dbBroker = new DBBroker();
        dbBroker.init();
        Session session = dbBroker.getConnection();


        Query animalQuery = session.createQuery("SELECT a from AnimalEntity as a WHERE a.id in (:param)")
                .setParameterList("param", getRandomAnimalsId());
        List<AnimalEntity> animalEntities = animalQuery.list();
        LinkedList<AnimalEntity> linkedList = new LinkedList<>();
        //linked list is a best practice for deleting elements from head or tail of this collection
        linkedList.addAll(animalEntities);
        session.close();

         pool = new AnimalPool(linkedList);
        while (!pool.getAnimals().isEmpty()) {
                        try {
                produce();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        Jedis jedis = AnimalPool.connectionPool.getResource();
       jedis.expire(pool.getKey(), 1000);
    }

    public static List<Integer> getRandomAnimalsId() {
        Random random = new Random();

        ArrayList<Integer> list = new ArrayList<>();

        IntStream.range(0, 50).forEach(i -> list.add(Integer.valueOf(random.nextInt(100000))));

        return list;
    }
}
