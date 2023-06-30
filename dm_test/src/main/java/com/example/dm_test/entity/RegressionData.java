package com.example.dm_test.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RegressionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double x;
    private double y;

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public Long getId() {
        return id;
    }
}
