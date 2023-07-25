package com.example.dm_test.entity;

public class StatsData {
    private double mean;
    private double min;
    private double max;
    private double median;
    private double quartile25;
    private double quartile75;

    public double getMax() {
        return max;
    }

    public double getMean() {
        return mean;
    }

    public double getMedian() {
        return median;
    }

    public double getMin() {
        return min;
    }

    public double getQuartile25() {
        return quartile25;
    }

    public double getQuartile75() {
        return quartile75;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setQuartile25(double quartile25) {
        this.quartile25 = quartile25;
    }

    public void setQuartile75(double quartile75) {
        this.quartile75 = quartile75;
    }

    public StatsData(double min, double max, double mean, double median, double quartile25, double quartile75)
    {
        this.max = max;
        this.min = min;
        this.mean = mean;
        this.median = median;
        this.quartile25 = quartile25;
        this.quartile75 = quartile75;
    }
}
