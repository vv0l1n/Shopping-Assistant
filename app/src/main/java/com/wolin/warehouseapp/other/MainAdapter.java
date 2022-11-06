package com.wolin.warehouseapp.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.model.Product;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{

    Context context;
    List<Product> items;

    public MainAdapter(Context context, List<Product> items) {
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
        holder.getImageView().setImageResource(items.get(position).getImageOfProduct());
        holder.getProductName().setText(items.get(position).getName());
        holder.getCount().setText(Integer.toString(items.get(position).getCount()));
        holder.getBoughtButton().setOnClickListener(view -> {
            items.get(position).setActive(false);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
