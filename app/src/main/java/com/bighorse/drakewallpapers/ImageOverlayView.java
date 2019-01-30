package com.bighorse.drakewallpapers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


class ImageOverlayView extends RelativeLayout {
    private Activity mActivity;
    private Context mContext;
    private TextView mDownloadTextView;
    private TextView mSetTextView;
    private String mUriImageDownload;
    private int REQUEST_WRITE_PERMISSION_CODE = 1;

    private onImageOverlayClickedListener mListener;
    private String mFilename;

    public void clickDownload() {
        mDownloadTextView.callOnClick();
    }

    public interface onImageOverlayClickedListener{
        void onDownloadClicked(String mUriImage,String filename);
        void onSetClicked(String mUriImage, int type);
    }


    public ImageOverlayView(Context context, Activity activity, onImageOverlayClickedListener listener) {
        super(context);
        mActivity = activity;
        mContext = context;
        mListener = listener;
        init();
    }

    public ImageOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setImage(String uriImage){
        mUriImageDownload = uriImage;
    }
    public void setFilename(String filename){
        mFilename = filename;
    }
    private void init() {
        View view = inflate(getContext(), R.layout.overlay_view, this);
        mDownloadTextView = (TextView) view.findViewById(R.id.download_button);
        mDownloadTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION_CODE);
                    } else {
                        mListener.onDownloadClicked(mUriImageDownload, mFilename);
                    }
                }
            }
        });
        mSetTextView = (TextView) view.findViewById(R.id.set_button);
        mSetTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onSetClicked(mUriImageDownload, 0);
                }
            }
        });
    }

}