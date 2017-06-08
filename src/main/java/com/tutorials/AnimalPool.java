package com.tutorials;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.tutorials.entity.AnimalEntity;
import com.tutorials.entity.LimbEntity;
import org.hibernate.Session;
import org.hibernate.collection.internal.AbstractPersistentCollection;
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
    private Kryo kryo;
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
        connectionPool = new JedisPool(redisHost, redisPort);
        this.animals = animals;
        this.kryo =   new Kryo() {
            @Override
            public Serializer<?> getDefaultSerializer(final Class type ) {
                if (AbstractPersistentCollection.class.isAssignableFrom( type )) {
                    return new FieldSerializer( kryo, type );
                }
                return super.getDefaultSerializer( type );
            }
        };
        kryo.register(LimbEntity.class);
        kryo.register(AnimalEntity.class);


    }

    public AnimalPool() {
        connectionPool = new JedisPool(redisHost, redisPort);
        this.kryo =   new Kryo() {
            @Override
            public Serializer<?> getDefaultSerializer(final Class type ) {
                if (AbstractPersistentCollection.class.isAssignableFrom( type )) {
                    return new FieldSerializer( kryo, type );
                }
                return super.getDefaultSerializer( type );
            }
        };
        kryo.register(LimbEntity.class);
        kryo.register(AnimalEntity.class);
    }


    public String produce() throws InterruptedException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Output output = new Output(stream);
        Jedis jedis = connectionPool.getResource();

//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        output.setOutputStream(stream);


        kryo.writeObject(output, animals.removeFirst());
        output.close();
        Input input = new Input(new ByteArrayInputStream(stream.toByteArray()));
            input.close();
        AnimalEntity animalEntity = kryo.readObjectOrNull(input,AnimalEntity.class);

        jedis.rpush(key, stream.toByteArray());

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

        Jedis jedis = connectionPool.getResource();
//            while (jedis.llen(key) == 0) {
//                jedis.wait();
//            }

        byte[] byteObject = jedis.lpop(key);


        Input input = new Input(new ByteArrayInputStream(byteObject));
        input.close();
        AnimalEntity animalEntity = kryo.readObjectOrNull(input, AnimalEntity.class);

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





