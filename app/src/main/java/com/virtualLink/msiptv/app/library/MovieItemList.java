package com.virtualLink.msiptv.app.library;

/**
 * Created by bright on 10/6/14.
 */
public class MovieItemList {

    private String title;
    private String description;
    private String image_url;
    private String category;
    private String year;
    private String video_url;

    public MovieItemList(String title, String description, String image_url,String video_url, String year) {
        this(title, description, image_url);
        this.year = year;
        this.video_url = video_url;
    }

    public MovieItemList(){}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getCategory(){
        return this.category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public MovieItemList(String title, String description, String image_url) {
        this.title = title;
        this.description = description;
        this.image_url = image_url;
    }

    public MovieItemList(String image_url) {
        this.image_url = image_url;
    }

    public MovieItemList(String title, String image_url) {
        this(image_url);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getYear() {
        return year;
    }
}
