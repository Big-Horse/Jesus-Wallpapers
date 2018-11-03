package com.bighorse.jesuswallpapers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.facebook.drawee.view.SimpleDraweeView;

class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final SimpleDraweeView mImageView;

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
    }

    public void setImage(ImageModel image) {
        mImageView.setImageURI(image.getUri());
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onClick(getAdapterPosition());
        }
    }
}
