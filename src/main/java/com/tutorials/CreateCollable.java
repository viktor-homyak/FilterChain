package com.tutorials;

import com.tutorials.entity.AnimalEntity;
import com.tutorials.entity.LimbEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by vhomyak on 23.05.2017.
 */
public class CreateCollable implements Callable<String> {

    DBBroker dbBroker;
    byte[] kotikByte;
    int numberToCreate;

    public CreateCollable(DBBroker dbBroker, byte[] kotikByte,int numberToCreate)  {
        this.dbBroker = dbBroker;
        this.kotikByte = kotikByte;
        this.numberToCreate = numberToCreate;
    }

    @Override
    public String call() throws Exception {

        Session session = dbBroker.getConnection();
        Transaction t = session.beginTransaction();
        List<AnimalEntity> animals;

        animals = IntStream.range(0, numberToCreate).mapToObj(index -> {
            AnimalEntity animalEntity = new AnimalEntity();
            animalEntity.setFace(kotikByte);
            animalEntity.setName("Зверь" + index + Thread.currentThread().getName().substring(Thread.currentThread().getName().length()-1) );
            animalEntity.setHeight(Create.getHeight());
            session.save(animalEntity);
            System.out.println(animalEntity.getName());
            return animalEntity;
        }).collect(Collectors.toList());

        animals.forEach(animalEntity ->IntStream.range(0, 10).forEach(index->{
            LimbEntity limb=    new LimbEntity();
            limb.setAnimal(animalEntity);
            if(animalEntity.getLimbs()==null)
                animalEntity.setLimbs(new LinkedList());
            animalEntity.getLimbs().add(limb);
            limb.setName("limb "+index + " of " + animalEntity.getName());
           // limb.setOrder(index);
            session.save(limb);
            System.out.println(limb.getName());

        }));

//        IntStream.range(0, 100).mapToObj(index -> {
//            AnimalEntity animalEntity = new AnimalEntity();
//            // animalEntity.setId(BigInteger.valueOf(index));
//            animalEntity.setFace(kotikByte);
//            animalEntity.setName("Зверь" + index);
//            session.save(animalEntity);
//            System.out.println(animalEntity);
//            return animalEntity;
//        }).peek(animalEntity -> IntStream.range(0, 10).forEach(i -> {
//            LimbEntity limb = new LimbEntity();
//            limb.setAnimal(animalEntity);
//            limb.setName("limb" + i);
//            session.persist(limb);
//        }));


        t.commit();
        session.close();
        return null;
    }
}
