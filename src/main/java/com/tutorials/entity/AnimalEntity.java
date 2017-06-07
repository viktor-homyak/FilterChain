package com.tutorials.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Sort;


import javax.persistence.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Viktor on 16.05.2017.
 */
@Entity
@Table(name = "animal", schema = "public", catalog = "filterchain")
public class AnimalEntity  {
   // @JSONField(name = "face")
    private byte[] face;
   // @JSONField(name = "id")
    private Integer id;
   // @JSONField(name = "name")
    private String name;
   // @JSONField(name = "name")
    private List<LimbEntity> limbs;
   // @JSONField(name = "height")
    private Integer height;

    @Basic
    @Column(nullable = false)
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Basic
    @Column(name = "face", nullable = false)
    public byte[] getFace() {
        return face;
    }

    public void setFace(byte[] face) {
        this.face = face;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "animal_seq")
    @SequenceGenerator(name = "animal_seq", sequenceName = "animal_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 0,unique = true, columnDefinition = "numeric(10,0)")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(cascade = CascadeType.ALL, targetEntity = LimbEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    @OrderColumn(name = "order_id",nullable = false, columnDefinition = "numeric(10,0)")
    @Fetch(FetchMode.JOIN)
   // @OrderBy("order_id asc ")
    public List<LimbEntity> getLimbs() {
        return limbs;
    }

    public void setLimbs(List<LimbEntity> limbs) {
        this.limbs = limbs;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof AnimalEntity)) return false;

        AnimalEntity that = (AnimalEntity) o;
        if(this.id!=null)
            return this.id.equals(that.getId());
        else
            return o!=null? this.hashCode()==o.hashCode():null;
}

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(face);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}
