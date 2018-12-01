package com.bighorse.jesuswallpapers;

import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
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
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.onImageClickedListener, ImageOverlayView.onImageOverlayClickedListener, FirebaseController.FirebaseListener {

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private ImageOverlayView mOverlayView;
    private ProgressBar mProgressBar;
    private int REQUEST_WRITE_PERMISSION_CODE = 1;



    private AdView mAdMainBannerView;
    private InterstitialAd mInterstitialMainAd;
    private InterstitialAd mInterstitialVideoAd;

    int videoAttempts = 0;@Override
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

        MobileAds.initialize(this, "ca-app-pub-5005687032079051~6397593090");

        mAdMainBannerView = findViewById(R.id.adView);
        mAdMainBannerView.loadAd(new AdRequest.Builder().build());

        mInterstitialMainAd = new InterstitialAd(this);
        mInterstitialMainAd.setAdUnitId("ca-app-pub-5005687032079051/2725941397");
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
        mInterstitialVideoAd.setAdUnitId("ca-app-pub-5005687032079051/7793724804");

    }

    @Override
    public void onClick(ImageModel image, int position) {
        final List<ImageModel> list = mAdapter.getList();

        mOverlayView = new ImageOverlayView(this, this, this);
        new ImageViewer.Builder<>(this, list)
                .setFormatter(new ImageViewer.Formatter<ImageModel>() {
                    @Override
                    public String format(ImageModel customImage) {
                        return customImage.getUriWallpaperDownload();
                    }
                }).hideStatusBar(true).setOnDismissListener(new ImageViewer.OnDismissListener() {
            @Override
            public void onDismiss() {
                videoAttempts++;

                if ((videoAttempts !=0) && (videoAttempts %2 == 0)) {
                    mInterstitialVideoAd.show();
                }else{
                    mInterstitialVideoAd.loadAd(new AdRequest.Builder().build());
                }
            }
        }).setStartPosition(position).setOverlayView(mOverlayView).setImageChangeListener(new ImageViewer.OnImageChangeListener() {
            @Override
            public void onImageChange(int position) {
                ImageModel image = (ImageModel) list.get(position);
                mOverlayView.setImage(image.getUriWallpaperDownload());
                mOverlayView.setFilename(image.getName());
            }
        }).build().show();
    }

    @Override
    public void onDownloadClicked(String mUriImage, String filename) {
        Uri uri = Uri.parse(mUriImage);
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);

        // set title and description
        request.setTitle(filename + ".jgp");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //set the local destination for download file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename + ".jpg");
        request.setMimeType("image/*");
        downloadManager.enqueue(request);

    }

    @Override
    public void onSetClicked(final String mUriImage, int type) {
        final WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());

        AsyncTask.execute(() -> {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // On Android N and above use the new API to set both the general system wallpaper and
                    // the lock-screen-specific wallpaper
                    myWallpaperManager.setBitmap(Utility.getBitmap(mUriImage, getApplicationContext()),null, true, WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK);
                } else {
                    myWallpaperManager.setBitmap(Utility.getBitmap(mUriImage, getApplicationContext()));
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally {
                MainActivity.this.runOnUiThread(()->{
                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.wallpaper_set),Toast.LENGTH_SHORT).show();
                });

            }
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_WRITE_PERMISSION_CODE)
        {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mOverlayView.clickDownload();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }
    }

}
