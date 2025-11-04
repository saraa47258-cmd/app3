package com.example.app3.data.model;

public class ProductWithProgress {
    private Product product;
    private int monthsNeeded;
    private float progressPercentage;
    private String estimatedDate;

    public ProductWithProgress(Product product, int monthsNeeded, float progressPercentage, String estimatedDate) {
        this.product = product;
        this.monthsNeeded = monthsNeeded;
        this.progressPercentage = progressPercentage;
        this.estimatedDate = estimatedDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getMonthsNeeded() {
        return monthsNeeded;
    }

    public void setMonthsNeeded(int monthsNeeded) {
        this.monthsNeeded = monthsNeeded;
    }

    public float getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(float progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public String getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(String estimatedDate) {
        this.estimatedDate = estimatedDate;
    }
}


