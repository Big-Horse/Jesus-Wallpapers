package com.bighorse.jesuswallpapers;

import android.app.WallpaperManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.onImageClickedListener, ImageOverlayView.onImageOverlayClickedListener, FirebaseController.FirebaseListener {

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private ImageOverlayView mOverlayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new Adapter();
        mAdapter.setListener(this, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        FirebaseController.getInstance().setListener(this);
        FirebaseController.getInstance().attachDatabaseListener();


    }

    @Override
    public void onClick(ImageModel image, int position) {
        final List<ImageModel> list = mAdapter.getList();

        mOverlayView = new ImageOverlayView(this, this);
        new ImageViewer.Builder<>(this, list)
                .setFormatter(new ImageViewer.Formatter<ImageModel>() {
                    @Override
                    public String format(ImageModel customImage) {
                        return customImage.getUriWallpaperDownload();
                    }
                }).setOverlayView(mOverlayView).setImageChangeListener(new ImageViewer.OnImageChangeListener() {
            @Override
            public void onImageChange(int position) {
                ImageModel image = (ImageModel) list.get(position);
                mOverlayView.setImage(image.getUri());
            }
        }).build().show();
    }

    @Override
    public void onDownloadClicked(String mUriImage) {

    }

    @Override
    public void onSetClicked(String mUriImage) {
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());
        try {
            myWallpaperManager.setBitmap(Utility.getBitmap(mUriImage, getApplicationContext()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onChildAdded(ImageModel image) {
        mAdapter.addImage(image);
    }
}
