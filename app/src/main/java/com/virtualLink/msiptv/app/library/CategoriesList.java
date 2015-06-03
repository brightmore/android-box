package com.virtualLink.msiptv.app.library;

/**
 * Created by bright on 10/7/14.
 */
public class CategoriesList {
    private String category;
    private String description;
    private String imageUrl;
    private String catID;

    public CategoriesList(String category,String description,String imageUrl,String catID){
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
        this.catID = catID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    @Override
    public String toString() {
        return this.category;
    }
}
