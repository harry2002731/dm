package com.example.dm_test.entity;


public class ValidateData {
    private String name;
    private double precision;
    private double recall;
    public ValidateData(String name, double precision, double recall)
    {
        this.name = name;
        this.precision = precision;
        this.recall = recall;
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public String getName() {
        return name;
    }
}
