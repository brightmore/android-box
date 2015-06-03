package com.virtualLink.msiptv.app.library;

/**
 * Created by bright on 11/5/14.
 */
public class MusicCategory {

    private String categoryTitle;
    private String posterUrl;

    public MusicCategory(String categoryTitle,String posterUrl){
        this.categoryTitle = categoryTitle;
        this.posterUrl = posterUrl;
    }

    public String getCategoryTitle(){
        return this.categoryTitle;
    }

    public String getPosterUrl(){
        return this.posterUrl;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @Override
    public String toString() {
        return "MusicCategory{" +
                "categoryTitle='" + categoryTitle + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                '}';
    }
}
