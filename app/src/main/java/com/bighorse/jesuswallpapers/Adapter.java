package com.bighorse.jesuswallpapers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<ImageViewHolder> implements ImageViewHolder.onImageClickedListener {
    private List<ImageModel> imagesList = new ArrayList<>();
    private onImageClickedListener mListener;

    private Context mContext;

    public void setListener(onImageClickedListener mListener, Context context){
        this.mListener = mListener;
        this.mContext = context;
    }

    public List<ImageModel> getList() {
        return imagesList;
    }

    public interface onImageClickedListener{
        void onClick(ImageModel adapterPosition, int position);
    }

    public void addImage(ImageModel image){
        imagesList.add(image);
        notifyItemInserted(imagesList.size() - 1);
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
        imageViewHolder.setImage(image, mContext);
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
