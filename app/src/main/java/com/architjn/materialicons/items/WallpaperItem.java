package com.architjn.materialicons.items;

/**
 * Created by architjn on 29/07/15.
 */
public class WallpaperItem {

    private String name, author, url;

    public WallpaperItem(String name, String author, String url) {
        this.name = name;
        this.author = author;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

}
