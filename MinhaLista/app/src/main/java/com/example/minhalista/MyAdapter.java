package com.example.minhalista;

import android.annotation.SuppressLint;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {

    List<MyItem> itens;
    MainActivity mainActivity;

    public MyAdapter(MainActivity mainActivity, List<MyItem> itens){
        this.itens = itens;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainActivity);
        View v = inflater.inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyItem myItem = itens.get(position);

        View v = holder.itemView;

        ImageView imvPhoto = v.findViewById(R.id.imvPhoto);
        imvPhoto.setImageURI(myItem.photo);

        TextView tvTitle = v.findViewById(R.id.tvTitle);
        tvTitle.setText(myItem.title);

        TextView tvDescription = v.findViewById(R.id.tvDescription);
        tvDescription.setText(myItem.description);
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }
}
