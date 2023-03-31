package com.example.googleanalyticsass;

class Category {
    String id;
    String categoryName;
    private  Category(){}
    Category(String id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return categoryName;
    }

}