package com.tutorials;

import com.tutorials.Filter.*;
import com.tutorials.entity.AnimalEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;


/**
 * Created by Viktor on 16.05.2017.
 */
public class Main {

    private Filter f1;

    public Main(DBBroker broker) {
        // initialize the chain
        this.f1 = new ChangeAttributeFilter(broker);
        Filter f2 = new SearchDuplicatesFilter(broker);
        Filter f3 = new SortFilter(broker);
        Filter f4 = new StatisticsFilter(broker);

        // set the chain of responsibility
        f1.setNextFilter(f2);
        f2.setNextFilter(f3);
        f3.setNextFilter(f4);
    }


    public static void main(String[] args) throws IOException, InterruptedException {

        AnimalPool pool = new AnimalPool();
        DBBroker dbBroker = new DBBroker();
        dbBroker.init();
    //    Session session = dbBroker.getConnection();
//
//
//        Query animalQuery = session.createQuery("SELECT a from AnimalEntity as a WHERE a.id in (:param)")
//                .setParameterList("param", getRandomAnimalsId());
//        List<AnimalEntity> animalEntities = animalQuery.list();
//        LinkedList<AnimalEntity> linkedList = new LinkedList<>();
//        //linked list is a best practice for deleting elements from head or tail of this collection
//        linkedList.addAll(animalEntities);
//

//
//        session.close();


        DefaultThreadFactory factory = new DefaultThreadFactory("Filter Thread");
        List<Callable<String>> sessionsToExecute = new ArrayList<>();


        // IntStream.range(0, 5).forEach(index -> {

       // AnimalProducer.main(new String[]{"foo"});


        // });

        IntStream.range(0, 3).forEach(index -> {
            Callable<String> consumer = new Callable<String>() {
                public String call() throws Exception {
                    Jedis jedis = AnimalPool.connectionPool.getResource();
                    while (jedis.llen(pool.getKey())!=0) {
                        Main main = new Main(dbBroker);
                        main.f1.execute(pool.consume());
                    }
                    jedis.close();
                    return "all good";
                }
            };
            sessionsToExecute.add(consumer);
        });


        ExecutorService executorService = Executors.newCachedThreadPool(factory);

        List<Future<String>> futures = null;

        futures = executorService.invokeAll(sessionsToExecute);


        for (Future<String> f : futures) {
            try {
                System.out.println("future.get()=" + f.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();

    }

//    public static List<Integer> getRandomAnimalsId() {
//        Random random = new Random();
//
//        ArrayList<Integer> list = new ArrayList<>();
//
//        IntStream.range(0, 5).forEach(i -> list.add(Integer.valueOf(random.nextInt(100000))));
//        ;
//        return list;
//    }


}
