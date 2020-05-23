package com.khhs.adminappbatch4;

public class CategoryModel {
   public String categoryTitle;

   public String createdAt;

    public CategoryModel(String categoryTitle, String createdAt) {
        this.categoryTitle = categoryTitle;
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public CategoryModel(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public CategoryModel() {
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
