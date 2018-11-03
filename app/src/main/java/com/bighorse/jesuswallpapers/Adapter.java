package com.bighorse.jesuswallpapers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<ImageViewHolder> implements ImageViewHolder.onImageClickedListener {
    List<ImageModel> imagesList = new ArrayList<>();
    private onImageClickedListener mListener;

    public void setListener(onImageClickedListener mListener){
        this.mListener = mListener;
    }

    public interface onImageClickedListener{
        void onClick(ImageModel adapterPosition, int position);
    }

    public void addImagesList(List<ImageModel> images){
        imagesList.addAll(images);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.image_item, viewGroup, false);
        ImageViewHolder holder = new ImageViewHolder(view);
        holder.setListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        ImageModel image = imagesList.get(i);
        imageViewHolder.setImage(image);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }


    @Override
    public void onClick(int adapterPosition) {
        if(mListener != null){
            mListener.onClick(imagesList.get(adapterPosition), adapterPosition);
        }
    }
}
