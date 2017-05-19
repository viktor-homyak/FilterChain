package com.tutorials.Filter;


import com.tutorials.DBBroker;
import com.tutorials.entity.AnimalEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * Created by vhomyak on 19.05.2017.
 */
public class ChangeAttributeFilter implements Filter {

    private Session session;
    private Filter nextFilter;

    public ChangeAttributeFilter(DBBroker broker) {
        this.session =  broker.getConnection();
    }

    @Override
    public void execute(AnimalEntity animal) {
        System.out.println("Starting to execute changeAttribute");
        Transaction tx = session.beginTransaction();
        animal.setName("Was changed ");
        session.update(animal);
        tx.commit();
        session.close();
        if(nextFilter!=null){
            nextFilter.execute(animal);
        }
        while (!session.isOpen()){
            System.out.println("ChangeAttribute session was closed");
            break;
        }
    }

    @Override
    public void setNextFilter(Filter nextfilter) {
       this.nextFilter = nextfilter;
        System.out.println("changeAttribute next filter");
    }


}
