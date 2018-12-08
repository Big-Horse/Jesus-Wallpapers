package com.bighorse.jesuswallpapers;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class ImageModel implements Serializable {

    private String uri;
    private String uriThumb;
    private String uriWallpaper;
    @Exclude
    private String name;
    @Exclude
    private String uriThumbDownload;
    @Exclude
    private String uriWallpaperDownload;

    public ImageModel(){}

    public ImageModel(String name, String uri, final String uriThumb, String uriWallpaper) {
        this.uri = uri;
        this.uriThumb = uriThumb;
        this.uriWallpaper = uriWallpaper;
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

    public String getUriThumb() {
        return uriThumb;
    }
    public void setUriThumb(String uriThumb) {
        this.uriThumb = uriThumb;
    }

    public String getUriWallpaper() {
        return uriWallpaper;
    }
    public void setUriWallpaper(String uriWallpaper) {
        this.uriWallpaper = uriWallpaper;
    }

    public String getUriThumbDownload() {
        return uriThumbDownload;
    }
    public void setUriThumbDownload(String uriThumbDownload) {
        this.uriThumbDownload = uriThumbDownload;
    }

    public String getUriWallpaperDownload() {
        return uriWallpaperDownload;
    }
    public void setUriWallpaperDownload(String uriWallpaperDownload) {
        this.uriWallpaperDownload = uriWallpaperDownload;
    }

}
