package com.tutorials.entity;

import javax.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
/**
 * Created by Viktor on 16.05.2017.
 */
@Entity
@Table(name = "limb", schema = "public", catalog = "filterchain")
public class LimbEntity {
    private String name;
    private Integer id;
    private AnimalEntity animal;
   // private Integer order;




//    @Basic
//    public Integer getOrder() {
//        return order;
//    }
//
//    public void setOrder(Integer order) {
//        this.order = order;
//    }

    @Basic
    @Column(name = "name", nullable = true, length = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "limb_seq")
    @SequenceGenerator(name = "limb_seq", sequenceName = "limb_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @ManyToOne(cascade = CascadeType.ALL, targetEntity = AnimalEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "animal_id", referencedColumnName = "id", insertable = false, updatable = false)
    public AnimalEntity getAnimal() {
        return animal;
    }

    public void setAnimal(AnimalEntity animal) {
        this.animal = animal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof LimbEntity)) return false;

        LimbEntity that = (LimbEntity) o;
        if(this.id!=null)
            return this.id.equals(that.getId());
        else
            return o!=null? this.hashCode()==o.hashCode():null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + id;
        return result;
    }


}
