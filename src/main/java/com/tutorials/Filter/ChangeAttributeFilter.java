package com.tutorials.Filter;


import com.tutorials.DBBroker;
import com.tutorials.entity.AnimalEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * Created by vhomyak on 19.05.2017.
 */
public class ChangeAttributeFilter implements Filter {


    private Filter nextFilter;
    private DBBroker broker;

    public ChangeAttributeFilter(DBBroker broker) {
        this.broker = broker;
    }

    @Override
    public void execute(AnimalEntity animal) {
        Session session=broker.getConnection();
        Transaction tx = null;
        while(tx==null) {
            try {
                tx = session.beginTransaction();
            } catch (Exception e) {
                session.close();
                session = broker.getConnection();
            }
        }
        try {
            System.out.println("Starting to execute changeAttribute");

            animal.setName("Was changed ");
            session.update(animal);
            tx.commit();
        }
        catch (Exception ex){
            tx.rollback();
        }
        finally {
            session.close();
        }
        if(nextFilter!=null){
            nextFilter.execute(animal);
        }
    }

    @Override
    public void setNextFilter(Filter nextfilter) {
       this.nextFilter = nextfilter;
        System.out.println("changeAttribute next filter");
    }


}
