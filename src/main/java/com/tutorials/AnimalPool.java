package com.tutorials;

import com.tutorials.entity.AnimalEntity;

import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.stream.IntStream;

/**
 * Created by vhomyak on 30.05.2017.
 */
public class AnimalPool {
    private ArrayDeque<AnimalEntity> pool;
    private List<AnimalEntity> animals;

    public List<AnimalEntity> getAnimals() {
        return animals;
    }

    public AnimalPool(List<AnimalEntity> animals) {

        pool = new ArrayDeque<AnimalEntity>();

        this.animals = animals;

    }

    public String produce() throws InterruptedException {

    synchronized (pool) {

        while (pool.size() > 20) {
            if(animals.isEmpty())
              return "";
            pool.wait();
        }

        ;

            pool.add(animals.remove(0));
           pool.notifyAll();


    }

        return "Added 1. Pool capacity = "+pool.size() ;

    }

    public AnimalEntity consume() throws InterruptedException {
        synchronized (pool) {

            while (pool.size() == 0) {
                pool.wait();
            }

            AnimalEntity returnValue = pool.removeFirst();
            pool.notifyAll();

            return returnValue;
        }

    }
}





