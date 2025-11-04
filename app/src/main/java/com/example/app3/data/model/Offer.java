package com.example.app3.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "offers")
public class Offer {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String productName;
    private int discountPercentage;
    private double originalPrice;
    private double discountedPrice;
    private String offerUrl;
    private Long expiryDate;
    private String source;
    private boolean isActive;
    private long createdAt;

    // Constructor for Room
    public Offer(long id, String productName, int discountPercentage, double originalPrice,
                 double discountedPrice, String offerUrl, Long expiryDate, String source,
                 boolean isActive, long createdAt) {
        this.id = id;
        this.productName = productName;
        this.discountPercentage = discountPercentage;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.offerUrl = offerUrl;
        this.expiryDate = expiryDate;
        this.source = source;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Constructor with defaults (for programmatic use)
    @Ignore
    public Offer(String productName, int discountPercentage, double originalPrice, double discountedPrice) {
        this(0, productName, discountPercentage, originalPrice, discountedPrice,
                null, null, "عرض محلي", true, System.currentTimeMillis());
    }

    // Constructor for creating offers with optional fields
    @Ignore
    public Offer(String productName, int discountPercentage, double originalPrice, double discountedPrice,
                 String offerUrl, Long expiryDate, String source) {
        this(0, productName, discountPercentage, originalPrice, discountedPrice,
                offerUrl, expiryDate, source, true, System.currentTimeMillis());
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public String getOfferUrl() {
        return offerUrl;
    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public String getSource() {
        return source;
    }

    public boolean isActive() {
        return isActive;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public void setOfferUrl(String offerUrl) {
        this.offerUrl = offerUrl;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}

