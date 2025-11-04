package com.example.app3.data.model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private double price;
    @Nullable
    private String imageUri;
    private String category;
    private Priority priority;
    @Nullable
    private String productUrl;
    private long createdAt;
    private boolean isPurchased;
    @Nullable
    private Long purchasedDate;
    @Nullable
    private String notes;

    // Constructor
    public Product(long id, String name, double price, @Nullable String imageUri, String category,
                   Priority priority, @Nullable String productUrl, long createdAt, boolean isPurchased,
                   @Nullable Long purchasedDate, @Nullable String notes) {
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
                   @Nullable String imageUri, @Nullable String productUrl, @Nullable String notes) {
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

    @Nullable
    public String getImageUri() {
        return imageUri;
    }

    public String getCategory() {
        return category;
    }

    public Priority getPriority() {
        return priority;
    }

    @Nullable
    public String getProductUrl() {
        return productUrl;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    @Nullable
    public Long getPurchasedDate() {
        return purchasedDate;
    }

    @Nullable
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

    public void setImageUri(@Nullable String imageUri) {
        this.imageUri = imageUri;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setProductUrl(@Nullable String productUrl) {
        this.productUrl = productUrl;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    public void setPurchasedDate(@Nullable Long purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }
}

