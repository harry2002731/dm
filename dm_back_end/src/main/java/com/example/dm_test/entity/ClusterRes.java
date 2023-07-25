package com.example.dm_test.entity;

import javax.persistence.Entity;


public class ClusterRes {
    private double x;
    private double y;
    private double clu_res;

    private String color;

    public double getClu_res() {
        return clu_res;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getColor()
    {
        return color;
    }

    public void setClu_res(double clu_res) {
        this.clu_res = clu_res;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ClusterRes(double x, double y, double clu_res, String color)
    {
        this.x = x;
        this.y = y;
        this.clu_res = clu_res;
        this.color = color;
    }
}
