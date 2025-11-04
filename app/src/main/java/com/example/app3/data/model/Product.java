package com.example.app3.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private double price;
    private String imageUri;
    private String category;
    private Priority priority;
    private String productUrl;
    private long createdAt;
    private boolean isPurchased;
    private Long purchasedDate;
    private String notes;

    // Constructor
    public Product(long id, String name, double price, String imageUri, String category,
                   Priority priority, String productUrl, long createdAt, boolean isPurchased,
                   Long purchasedDate, String notes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUri = imageUri;
        this.category = category;
        this.priority = priority;
        this.productUrl = productUrl;
        this.createdAt = createdAt;
        this.isPurchased = isPurchased;
        this.purchasedDate = purchasedDate;
        this.notes = notes;
    }

    // Constructor for creating new products from UI
    @Ignore
    public Product(String name, double price, String category, Priority priority,
                   String imageUri, String productUrl, String notes) {
        this(0, name, price, imageUri, category, priority, productUrl,
                System.currentTimeMillis(), false, null, notes);
    }

    // Simple constructor
    @Ignore
    public Product(String name, double price) {
        this(0, name, price, null, "أخرى", Priority.MEDIUM, null,
                System.currentTimeMillis(), false, null, null);
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getCategory() {
        return category;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public Long getPurchasedDate() {
        return purchasedDate;
    }

    public String getNotes() {
        return notes;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    public void setPurchasedDate(Long purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

