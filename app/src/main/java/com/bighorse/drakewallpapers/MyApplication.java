package com.bighorse.drakewallpapers;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            Fresco.initialize(this);
        }

}
