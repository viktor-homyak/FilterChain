package com.tutorials;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.tutorials.entity.AnimalEntity;
import com.tutorials.entity.LimbEntity;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vhomyak on 30.05.2017.
 */
public class AnimalPool {

    private LinkedList<AnimalEntity> animals;
    private final byte[] key = "list".getBytes();

    private static final String redisHost = "localhost";
    private static final Integer redisPort = 6379;
    public static JedisPool connectionPool = null;


    public byte[] getKey() {
        return key;
    }



    public List<AnimalEntity> getAnimals() {
        return animals;
    }

    public AnimalPool(LinkedList<AnimalEntity> animals) {
        // kryo = new Kryo();
//        kryo.register(AnimalEntity.class);
//        kryo.register(LimbEntity.class);


        connectionPool = new JedisPool(redisHost, redisPort);
        this.animals = animals;
    }

    public AnimalPool() {
        connectionPool = new JedisPool(redisHost, redisPort);

    }


    public String produce() throws InterruptedException {
        Output output = new Output(new ByteBufferOutput());
        Jedis jedis = connectionPool.getResource();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        output.setOutputStream(stream);
        Kryo kryo = new Kryo();
        kryo.writeObject(output, animals.removeFirst());

        jedis.rpush(key, stream.toByteArray());
        output.close();
        jedis.close();
        return "Added";


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
        Kryo kryo = new Kryo();
        kryo.register(AnimalEntity.class);
        kryo.register(LimbEntity.class);
        Jedis jedis = connectionPool.getResource();
//            while (jedis.llen(key) == 0) {
//                jedis.wait();
//            }

        byte[] byteObject = jedis.lpop(key);


        Input input = new Input(new ByteArrayInputStream(byteObject));

        AnimalEntity animalEntity = kryo.readObjectOrNull(input, AnimalEntity.class);
        input.close();
        jedis.close();
        // jedis.notifyAll();

        return animalEntity;


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





