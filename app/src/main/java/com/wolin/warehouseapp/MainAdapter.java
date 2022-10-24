package com.wolin.warehouseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{

    Context context;
    List<MainMenuItem> items;

    public MainAdapter(Context context, List<MainMenuItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.main_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.getImageView().setImageResource(items.get(position).getImage());
        holder.getProductName().setText(items.get(position).getName());
        holder.getZone().setText(items.get(position).getZone());
        holder.getRack().setText(items.get(position).getRack());
        holder.getShelf().setText(items.get(position).getShelf());
        holder.getZone().setText(items.get(position).getZone());
        holder.getCount().setText(Integer.toString(items.get(position).getCount()));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
