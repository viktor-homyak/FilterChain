package com.tutorials;

import java.util.concurrent.Callable;

/**
 * Created by vhomyak on 31.05.2017.
 */
public class AnimalProducer implements Callable<String> {


    private AnimalPool pool;

    public AnimalProducer(AnimalPool pool) {
        super();
       this.pool = pool;
    }

    public String produce() throws InterruptedException {
        return pool.produce();
    }

    @Override
    public String call() {
         int counter=0;
        while (!pool.getAnimals().isEmpty()) {
            try {
                produce();
                counter++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        return "There was " + counter+ " calls of produce()method, that was injected by thread named "
                + Thread.currentThread().getName();
    }
}
