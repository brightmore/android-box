package com.virtualLink.msiptv.app.library;

/**
 * Created by root on 9/28/14.
 */
public class TVChannel {
    public String url;
    public String name;
    public String description;
    public String category;

    public TVChannel(String name, String url) {
        this.url = url;
        this.name = name;
        this.category = "";
        this.description = "";
    }

    public TVChannel(String name, String url, String category, String description) {
        this(name, url);
        this.description = description;
        this.category = category;
    }

    public TVChannel(String name, String url, String category) {
        this(name, url);
        this.category = category;
    }

    @Override
    public String toString() {
        return this.name;
    }
}