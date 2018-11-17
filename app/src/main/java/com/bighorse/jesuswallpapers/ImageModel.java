package com.bighorse.jesuswallpapers;

import android.net.Uri;

import com.google.firebase.database.Exclude;

public class ImageModel {

    private String uri;
    @Exclude
    private String name;

    public ImageModel(){}

    public ImageModel(String name, String uri) {
        this.uri = uri;
    }


    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Exclude
    public String getName() {
        return name;
    }
    @Exclude
    public void setName(String name) {
        this.name = name;
    }
}
