package com.example.dm_test.entity;


import javax.persistence.*;

@Entity
public class AprioriData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int milk;
    private int bread;
    private int eggs;
    private int coke;
    private int cereal;


    public Long getId()
    {
        return id;
    }

    public int getMilk()
    {
        return milk;
    }

    public int getBread()
    {
        return bread;
    }

    public int getEggs()
    {
        return eggs;
    }

    public int getCoke()
    {
        return coke;
    }

    public int getCereal()
    {
        return cereal;
    }
}
