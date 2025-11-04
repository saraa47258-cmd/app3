package com.example.app3.data.model;

public enum Priority {
    LOW("منخفضة", 1),
    MEDIUM("متوسطة", 2),
    HIGH("عالية", 3);

    private final String displayName;
    private final int value;

    Priority(String displayName, int value) {
        this.displayName = displayName;
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getValue() {
        return value;
    }
}


