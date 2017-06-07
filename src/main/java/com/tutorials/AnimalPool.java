package com.tutorials;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import com.tutorials.entity.AnimalEntity;
import com.tutorials.entity.LimbEntity;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.stream.IntStream;

/**
 * Created by vhomyak on 30.05.2017.
 */
public class AnimalPool {

    private LinkedList<AnimalEntity> animals;
    private Jedis jedis;
    private Kryo kryo;
    private Output output;
    private Input input;
    private static final String redisHost = "localhost";
    private static final Integer redisPort = 6379;
    private static JedisPool connectionPool = null;

    public List<AnimalEntity> getAnimals() {
        return animals;
    }

    public AnimalPool(LinkedList<AnimalEntity> animals) {
        kryo = new Kryo();
        kryo.register(AnimalEntity.class);
        kryo.register(LimbEntity.class);

        output = new Output(new ByteBufferOutput());
        input = new Input(new ByteBufferInput());

        connectionPool = new JedisPool(redisHost, redisPort);

        jedis = connectionPool.getResource();

        this.animals = animals;
    }

    public String produce() throws InterruptedException {
        synchronized (jedis) {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            output.setOutputStream(stream);
            kryo.writeObject(output, animals.removeFirst());
            output.close();
            jedis.lpush("list".getBytes(), stream.toByteArray());


            return "Added";

        }

//        synchronized (pool) {
//            while (pool.size() > 20) {
//                if (animals.isEmpty())
//                    return "";
//                pool.wait();
//            }
//            pool.add();
//            pool.notifyAll();
//        }
//        return "Added 1. Pool capacity = " + pool.size();

    }

    public AnimalEntity consume() throws InterruptedException {

        synchronized (jedis) {
            while (jedis.llen("list") == 0) {
                jedis.wait();
            }
            byte[] byteObject = jedis.lpop("list".getBytes());

            ByteArrayInputStream stream = new ByteArrayInputStream(byteObject);
            input.setInputStream(stream);
            AnimalEntity animalEntity = kryo.readObject(input, AnimalEntity.class);
            input.close();
            jedis.notifyAll();
            return animalEntity;

        }


//        synchronized (pool) {
//            while (pool.size() == 0) {
//                pool.wait();
//            }
//            AnimalEntity returnValue = pool.removeFirst();
//            pool.notifyAll();
//            return returnValue;
//        }

    }
}





