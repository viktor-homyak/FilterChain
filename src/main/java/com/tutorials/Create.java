package com.tutorials;

import com.tutorials.entity.AnimalEntity;
import com.tutorials.entity.LimbEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by vhomyak on 19.05.2017.
 */
public class Create {
    //start this before using chain
    public static void main(String[] args) throws IOException {

        DBBroker dbBroker = new DBBroker();

        dbBroker.init();


        File kotikFile = new File("src/main/resources/kotik.jpg");
        byte[] kotikByte = Files.readAllBytes(kotikFile.toPath());

        List<Session> sessions = IntStream.range(0,1).mapToObj(index-> dbBroker.getConnection()).collect(Collectors.toList());
        for (Session session:sessions) {
            Transaction t = session.beginTransaction();
            List<AnimalEntity> animals;

            animals = IntStream.range(0, 10000).mapToObj(index -> {
                AnimalEntity animalEntity = new AnimalEntity();
                animalEntity.setFace(kotikByte);
                animalEntity.setName("Зверь" + index );
                animalEntity.setHeight(getHeight());
                session.save(animalEntity);
                System.out.println(animalEntity.getId());
                return animalEntity;
            }).collect(Collectors.toList());

            animals.forEach(animalEntity ->IntStream.range(0, 10).forEach(index->{
                LimbEntity limb=    new LimbEntity();
                limb.setAnimal(animalEntity);
                limb.setName("limb"+index);
                System.out.println(limb.getId());
                session.save(limb);
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
        }
    }

    public static int getHeight(){
        Random r = new Random();
        int Low = 175;
        int High = 190;
        return  r.nextInt(High-Low) + Low;



    }
}
