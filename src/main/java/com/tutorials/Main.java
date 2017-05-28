package com.tutorials;

import com.tutorials.Filter.*;
import com.tutorials.entity.AnimalEntity;
import com.tutorials.entity.LimbEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;


import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Created by Viktor on 16.05.2017.
 */
public class Main {

    private Filter f1;

    public Main( DBBroker broker) {
        // initialize the chain
        this.f1 = new ChangeAttributeFilter( broker);
        Filter f2 = new SearchDuplicatesFilter(broker);
        Filter f3 = new SortFilter(broker);
        Filter f4 = new StatisticsFilter(broker);

        // set the chain of responsibility
        f1.setNextFilter(f2);
        f2.setNextFilter(f3);
        f3.setNextFilter(f4);
    }


    public static void main(String[] args) throws IOException,InterruptedException {

        DBBroker dbBroker = new DBBroker();
        dbBroker.init();
        Session session = dbBroker.getConnection();

        ArrayDeque<AnimalEntity> queue = new ArrayDeque<>();

       //  while(session.isOpen()){



        IntStream.range(0,5).forEach(it->{
                 System.out.println("Enter id of desired animal:");
                 Scanner input = new Scanner(System.in);
                 AnimalEntity animalEntity =
                         (AnimalEntity)session.get(AnimalEntity.class, input.nextInt());
                 if (animalEntity==null){
                     System.out.println("There is no animal in DB with such id");

                 }else {
                     queue.add(animalEntity);
                 }

             });
               session.close();



             DefaultThreadFactory factory = new DefaultThreadFactory("Filter Thread");

             List<Callable<String>> sessionsToExecute = IntStream.range(0,queue.size()).mapToObj(index-> {
                 Callable<String> callable = new Callable<String>() {
                     public String call() throws Exception {
                         Main main = new Main(dbBroker);
                         main.f1.execute(getAnimal(queue));

                         return "add good";
                     }
                 };
              return callable;
             }).collect(Collectors.toList());

             ExecutorService executorService = Executors.newScheduledThreadPool(3,factory);

             List<Future<String>> futures = null;

                 futures = executorService.invokeAll(sessionsToExecute);


        assert futures != null;
        for (Future<String> f : futures) {

            try {
                System.out.println("future.get()=" + f.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();

        }

    private static synchronized AnimalEntity getAnimal(ArrayDeque<AnimalEntity> queue) {
        return queue.removeFirst();
    }


    //  }


}
