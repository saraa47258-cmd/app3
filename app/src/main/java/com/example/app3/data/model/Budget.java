package com.example.app3.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "budget")
public class Budget {
    @PrimaryKey
    private long id;
    private double monthlyIncome;
    private double monthlySaving;
    private double fixedExpenses;
    private String currency;
    private long lastUpdated;

    // Constructor for Room
    public Budget(long id, double monthlyIncome, double monthlySaving, double fixedExpenses,
                  String currency, long lastUpdated) {
        this.id = id;
        this.monthlyIncome = monthlyIncome;
        this.monthlySaving = monthlySaving;
        this.fixedExpenses = fixedExpenses;
        this.currency = currency;
        this.lastUpdated = lastUpdated;
    }

    // Default constructor (for programmatic use)
    @Ignore
    public Budget() {
        this(1, 0.0, 0.0, 0.0, "ر.س", System.currentTimeMillis());
    }

    // Computed property
    public double getAvailableForSaving() {
        return monthlyIncome - fixedExpenses;
    }

    // Getters
    public long getId() {
        return id;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public double getMonthlySaving() {
        return monthlySaving;
    }

    public double getFixedExpenses() {
        return fixedExpenses;
    }

    public String getCurrency() {
        return currency;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setMonthlyIncome(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public void setMonthlySaving(double monthlySaving) {
        this.monthlySaving = monthlySaving;
    }

    public void setFixedExpenses(double fixedExpenses) {
        this.fixedExpenses = fixedExpenses;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

