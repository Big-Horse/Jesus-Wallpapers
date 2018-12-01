package com.bighorse.jesuswallpapers;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


class ImageOverlayView extends RelativeLayout {
    private Context mContext;
    private TextView mDownloadTextView;
    private TextView mSetTextView;
    private String mUriImageDownload;

    private onImageOverlayClickedListener mListener;

    public interface onImageOverlayClickedListener{
        void onDownloadClicked(String mUriImage);
        void onSetClicked(String mUriImage);
    }


    public ImageOverlayView(Context context,onImageOverlayClickedListener listener) {
        super(context);
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
    private void init() {
        View view = inflate(getContext(), R.layout.overlay_view, this);
        mDownloadTextView = (TextView) view.findViewById(R.id.download_button);
        mDownloadTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onDownloadClicked(mUriImageDownload);
                }
            }
        });
        mSetTextView = (TextView) view.findViewById(R.id.set_button);
        mSetTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onSetClicked(mUriImageDownload);
                }
            }
        });
    }
}