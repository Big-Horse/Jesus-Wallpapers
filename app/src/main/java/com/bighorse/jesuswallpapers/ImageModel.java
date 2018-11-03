package com.bighorse.jesuswallpapers;

import android.net.Uri;

public class ImageModel {

    private Uri uri;

    public ImageModel(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
