package com.bighorse.jesuswallpapers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;


class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView mImageView;

    private onImageClickedListener mListener;

    StorageReference storageReference;

    public void setListener(onImageClickedListener mListener){
        this.mListener = mListener;
    }

    public interface onImageClickedListener{
        void onClick(int adapterPosition);
    }

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.image_view);
        mImageView.setOnClickListener(this);
    }

    public void setImage(final ImageModel image, final Context context) {
        storageReference = FirebaseStorage.getInstance().getReference().child("thumbs/" + image.getUriThumb());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                GlideApp.with(context)
                        .load(imageURL)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mImageView);
                image.setUriThumbDownload(imageURL);
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference().child("wallpapers/" + image.getUriWallpaper());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                image.setUriWallpaperDownload(imageURL);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onClick(getAdapterPosition());
        }
    }


}
