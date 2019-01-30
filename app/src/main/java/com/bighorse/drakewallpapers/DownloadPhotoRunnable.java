package com.bighorse.drakewallpapers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadPhotoRunnable implements Runnable {

    private static final String TAG = "DownloadPhotoRunnable";
    private final Context mContext;
    private ImageModel image;

    public DownloadPhotoRunnable(ImageModel imageModel, Context context) {
        this.image = imageModel;
        this.mContext = context;
    }

    @Override
    public void run() {
        Log.d(TAG, "Inizio download");
        File directory = null;
        if (Utility.isExternalStorageWritable() && Utility.isStoragePermissionGranted(mContext)) {
            directory = Utility.getPublicAlbumStorageDir(mContext.getResources().getString(R.string.app_name));
            File mypath = null;
            if (directory != null) {
                mypath = new File(directory, "Photo_" + image.getName()+".jpg");
                if(mypath.exists()){
                    Log.d(TAG, "Esiste già");
                } else {
                    Bitmap image = Utility.getBitmap(this.image.getUri(),mContext);
                    if(image != null) {
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(mypath);
                            // Use the compress method on the BitMap object to write image to the OutputStream
                            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {

                                Log.e(TAG,"Error method saveImageIntoStorage: " + e.toString());
                            }
                        }
                        if (mypath != null) {
                            Log.d(TAG, "download completato");
                            Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri contentUri = Uri.fromFile(mypath);
                            scanIntent.setData(contentUri);
                            mContext.sendBroadcast(scanIntent);
                        } else {
                            Log.e(TAG,"Error method saveImageIntoStorage: myPath null");
                        }
                    } else {
                        Log.e(TAG,"Error method saveImageIntoStorage: Bitmap null");
                    }
                }
            } else {
                Log.e(TAG,"Error method saveImageIntoStorage: directory null");
            }
        } else {
            Log.e(TAG,"Error method saveImageIntoStorage: storage problems");
        }
    }
}