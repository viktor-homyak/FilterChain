package com.tutorials;

import com.tutorials.Filter.*;
import com.tutorials.entity.AnimalEntity;
import com.tutorials.entity.LimbEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;


import java.io.IOException;
import java.util.Scanner;


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


    public static void main(String[] args) throws IOException {

        DBBroker dbBroker = new DBBroker();
        dbBroker.init();
        Session session = dbBroker.getConnection();
           //     session.beginTransaction();
         while(session.isOpen()){
             System.out.println("Enter id of desired animal:");
            Scanner input = new Scanner(System.in);
            int animalId = input.nextInt();



            AnimalEntity animalEntity =
                    (AnimalEntity)session.get(AnimalEntity.class, animalId);
            if (animalEntity==null){
                System.out.println("There is no animal in DB with such id");
              continue;
            }
             session.close();

            Main main = new Main(dbBroker);
            main.f1.execute(animalEntity);


        }


    }


}
