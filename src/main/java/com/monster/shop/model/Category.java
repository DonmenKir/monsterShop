package com.monster.shop.model;

public class Category {
    private Integer categoryID;
    private String categoryName;
    private String description;

    public Category() {}
    
    // Getters and Setters
    public Integer getCategoryID() { return categoryID; }
    public void setCategoryID(Integer categoryID) { this.categoryID = categoryID; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    // Override toString for UI Dropdown display
    @Override
    public String toString() {
        return categoryName;
    }
}