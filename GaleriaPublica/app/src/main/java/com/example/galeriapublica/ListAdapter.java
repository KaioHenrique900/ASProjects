package com.example.galeriapublica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter {

    Context context;
    List<ImageData> imageDataList;

    public ListAdapter(Context context, List<ImageData> imageDataList){
        this.context = context;
        this.imageDataList = imageDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageData imageData = imageDataList.get(position);

        TextView tvName = holder.itemView.findViewById(R.id.tvName);
        tvName.setText(imageData.filename);

        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
        tvDate.setText("Data: " + new SimpleDateFormat(("HH:mm dd/MM/yyyy")).format(imageData.date));

        TextView tvSize = holder.itemView.findViewById(R.id.tvSize);
        tvSize.setText("Tamanho: " + String.valueOf(imageData.size));

        ImageView imageView = holder.itemView.findViewById(R.id.imvList);
        imageView.setImageBitmap(imageData.thumb);
    }

    @Override
    public int getItemCount() {
        return imageDataList.size();
    }
}
