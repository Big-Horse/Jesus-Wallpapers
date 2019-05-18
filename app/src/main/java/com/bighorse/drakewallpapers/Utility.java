package com.bighorse.drakewallpapers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class Utility {
    static final String TAG = "Utility";

    static int DEFAULT_INT_VALUE = 0;

    public static void askStoragePermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public static boolean isStoragePermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                /*Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);*/
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public static File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("PublicAlbumStorageDir", "Directory not created");
        }
        return file;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static Bitmap getBitmap(String uriStorage, Context context){
        Bitmap image = null;
        try {
            image = Glide.
                    with(context)
                    .asBitmap()
                    .load(uriStorage)
                    .submit()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static int getIntPreference(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(key, DEFAULT_INT_VALUE);
    }

    public static void saveIntPreference(Context context, String key, int data) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(key, data)
                .apply();
    }
}
