package com.tutorials;

import com.tutorials.entity.AnimalEntity;
import com.tutorials.entity.LimbEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by vhomyak on 19.05.2017.
 */
public class Create {

    private static int numberToCreate = 0;
    public static HashMap<String,Integer> mapOfNames = new HashMap<>();

    //start this before using chain
    public static void main(String[] args) throws IOException, InterruptedException {
        Arrays.stream(Names.values()).forEach(value->mapOfNames.put(value.toString(),0));
        DBBroker dbBroker = new DBBroker();

        dbBroker.init();

        File kotikFile = new File("src/main/resources/kotik.jpg");
        byte[] kotikByte = Files.readAllBytes(kotikFile.toPath());

        DefaultThreadFactory factory = new DefaultThreadFactory("Creating session");

        List<Callable<String>> sessionsToExecute = new ArrayList<>();

        sessionsToExecute = IntStream.range(0,20).mapToObj(index-> new CreateCollable(dbBroker,kotikByte,getAnimalsToCreate())).collect(Collectors.toList());

        ExecutorService executorService = Executors.newScheduledThreadPool(3,factory);

        List<Future<String>> futures = executorService.invokeAll(sessionsToExecute);

        for (Future<String> future : futures) {
            try {
                System.out.println("future.get()=" + future.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        }

        executorService.shutdown();

    }

    private static int getAnimalsToCreate() {

        numberToCreate = numberToCreate + 5000;
        return numberToCreate;
    }

    public static int getHeight(){
        Random r = new Random();
        int Low = 175;
        int High = 190;
        return  r.nextInt(High-Low) + Low;
    }


}
