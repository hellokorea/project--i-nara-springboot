package com.eureka.mindbloom.book.type;

public enum SortOption {
    VIEWCOUNT("viewCount"),
    LIKES("likes"),
    RECENT("recent");

    private final String value;

    SortOption(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
