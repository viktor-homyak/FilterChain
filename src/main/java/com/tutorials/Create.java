package com.tutorials;

import com.tutorials.entity.AnimalEntity;
import com.tutorials.entity.LimbEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by vhomyak on 19.05.2017.
 */
public class Create {

    private static int numberToCreate = 5000;

    //start this before using chain
    public static void main(String[] args) throws IOException, InterruptedException {

        DBBroker dbBroker = new DBBroker();

        dbBroker.init();

        File kotikFile = new File("src/main/resources/kotik.jpg");
        byte[] kotikByte = Files.readAllBytes(kotikFile.toPath());

        DefaultThreadFactory factory = new DefaultThreadFactory("Creating session");

        List<Callable<String>> sessionsToExecute = new ArrayList<>();

        sessionsToExecute = IntStream.range(0,3).mapToObj(index-> new CreateCollable(dbBroker,kotikByte,getAnimalsToCreate())).collect(Collectors.toList());

        ExecutorService executorService = Executors.newCachedThreadPool(factory);

        List<Future<String>> futures = executorService.invokeAll(sessionsToExecute);
       // List<Session> sessions = IntStream.range(0,10).mapToObj(index-> dbBroker.getConnection()).collect(Collectors.toList());


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
