package com.example.galeriapublica;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter {

    Context context;
    List<ImageData> imageDataList;

    public GridAdapter(Context context, List<ImageData> imageDataList){
        this.context = context;
        this.imageDataList = imageDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.grid_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Bitmap thumb = imageDataList.get(position).thumb;
        ImageView imageView = holder.itemView.findViewById(R.id.imvGrid);
        imageView.setImageBitmap(thumb);

    }

    @Override
    public int getItemCount() {
        return imageDataList.size();
    }
}
