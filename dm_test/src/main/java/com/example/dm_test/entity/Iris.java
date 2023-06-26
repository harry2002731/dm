package com.example.dm_test.entity;

import javax.persistence.*;


@Entity
@Table(name = "iris")
public class Iris {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float SepL;
    private float SepW;
    private float PetL;
    private float PetW;
    private String Species;

    public Long getId(){
        return id;
    }

    public float getSepL(){
        return SepL;
    }

    public float getSepW(){
        return SepW;
    }

    public float getPetL(){
        return PetL;
    }

    public float getPetW(){
        return PetW;
    }

    public String getSpecies(){
        return Species;
    }
}
