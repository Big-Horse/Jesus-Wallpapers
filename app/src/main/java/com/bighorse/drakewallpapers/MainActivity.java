package com.bighorse.drakewallpapers;

import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.onImageClickedListener, FirebaseController.FirebaseListener {

    private RecyclerView mRecyclerView;
    public static Adapter mAdapter;

    private ProgressBar mProgressBar;




    private AdView mAdMainBannerView;
    private InterstitialAd mInterstitialMainAd;
    private static InterstitialAd mInterstitialVideoAd;

    static int videoAttempts = 0;

    public static void addVideoAttempt() {
        videoAttempts++;

        if ((videoAttempts !=0) && (videoAttempts %2 == 0)) {
            mInterstitialVideoAd.show();
        }else{
            mInterstitialVideoAd.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseApp.initializeApp(this);
        mRecyclerView = findViewById(R.id.recyclerview);
        mProgressBar = findViewById(R.id.mainProgressbar);

        mAdapter = new Adapter();
        mAdapter.setListener(this, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        FirebaseController.getInstance().setListener(this);
        FirebaseController.getInstance().attachDatabaseListener();

        MobileAds.initialize(this, "ca-app-pub-5005687032079051~8894641764");

        mAdMainBannerView = findViewById(R.id.adView);
        mAdMainBannerView.loadAd(new AdRequest.Builder().build());

        mInterstitialMainAd = new InterstitialAd(this);
        mInterstitialMainAd.setAdUnitId("ca-app-pub-5005687032079051/6711455267");
        mInterstitialMainAd.loadAd(new AdRequest.Builder().build());

        mInterstitialMainAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialMainAd.show();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
            }
        });

        mInterstitialVideoAd = new InterstitialAd(this);
        mInterstitialVideoAd.setAdUnitId("ca-app-pub-5005687032079051/9007866993");

    }

    @Override
    public void onClick(ImageModel image, int position) {
        Intent imageViewActivity = new Intent(this, ImageViewActivity.class);
        imageViewActivity.putExtra("image", image);
        imageViewActivity.putExtra("position", position);
        startActivity(imageViewActivity);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            shareApp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent shareApp() {
        Intent intent= new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app));
        startActivity(Intent.createChooser(intent, "Spread the message!!"));
        return intent;
    }

    @Override
    public void onChildAdded(final ImageModel image) {

        new Thread() {
            @Override
            public void run() {

                final StorageReference[] storageReference = {FirebaseStorage.getInstance().getReference().child("thumbs/" + image.getUriThumb())};
                storageReference[0].getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        image.setUriThumbDownload(uri.toString());

                        storageReference[0] = FirebaseStorage.getInstance().getReference().child("wallpapers/" + image.getUriWallpaper());
                        storageReference[0].getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                image.setUriWallpaperDownload(uri.toString());

                                mAdapter.addImage(image);

                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        }.start();

    }



}
