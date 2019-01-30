package com.bighorse.drakewallpapers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;


class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView mImageView;

    private onImageClickedListener mListener;

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
        GlideApp.with(context)
                .load(image.getUriThumbDownload())
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onClick(getAdapterPosition());
        }
    }


}
