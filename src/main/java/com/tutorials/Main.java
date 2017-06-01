package com.tutorials;

import com.tutorials.Filter.*;
import com.tutorials.entity.AnimalEntity;
import org.hibernate.Query;
import org.hibernate.Session;

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

        DBBroker dbBroker = new DBBroker();
        dbBroker.init();
        Session session = dbBroker.getConnection();


        Query animalQuery = session.createQuery("SELECT a from AnimalEntity as a WHERE a.id in (:param)")
                .setParameterList("param", getRandomAnimalsId());
        List<AnimalEntity> animalEntities = animalQuery.list();


        AnimalPool pool = new AnimalPool(animalEntities);

        session.close();


        DefaultThreadFactory factory = new DefaultThreadFactory("Filter Thread");
        List<Callable<String>> sessionsToExecute = new ArrayList<>();


        IntStream.range(0, 5).forEach(index -> {
            sessionsToExecute.add(new AnimalProducer(pool));
        });

        IntStream.range(0, 5).forEach(index -> {
            Callable<String> consumer = new Callable<String>() {
                public String call() throws Exception {
                    while (pool.getAnimals().size() != 0 ) {
                        Main main = new Main(dbBroker);
                        main.f1.execute(pool.consume());
                    }

                    return "add good";
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

    public static List<Integer> getRandomAnimalsId() {
        Random random = new Random();

        ArrayList<Integer> list = new ArrayList<>();

        IntStream.range(0, 500).forEach(i -> list.add(Integer.valueOf(random.nextInt(100000))));
        ;
        return list;
    }


}
