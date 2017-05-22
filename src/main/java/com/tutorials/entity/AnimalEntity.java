package com.tutorials.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Viktor on 16.05.2017.
 */
@Entity
@Table(name = "animal", schema = "public", catalog = "filterchain")
public class AnimalEntity {
    private byte[] face;
    private Integer id;
    private String name;
    private List<LimbEntity> limbs;
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
    @Column(name = "id", nullable = false, precision = 0,unique = true)
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

    @OneToMany(cascade = CascadeType.ALL, targetEntity = LimbEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    @Transient
    @Fetch(FetchMode.SUBSELECT)
    public List<LimbEntity> getLimbs() {
        return limbs;
    }
    @Transient
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
