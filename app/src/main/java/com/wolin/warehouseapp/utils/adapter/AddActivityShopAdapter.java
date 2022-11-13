package com.wolin.warehouseapp.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.utils.model.Shop;

import java.util.List;

public class AddActivityShopAdapter  extends RecyclerView.Adapter<AddActivityShopViewHolder>{

    private Context context;
    private List<Shop> shops;
    private ItemSelectListener itemSelectListener;

    public AddActivityShopAdapter(Context context, List<Shop> shops, ItemSelectListener itemSelectListener) {
        this.context = context;
        this.shops = shops;
        this.itemSelectListener = itemSelectListener;
    }

    @NonNull
    @Override
    public AddActivityShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddActivityShopViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.add_activity_shop_dialog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddActivityShopViewHolder holder, int position) {
        holder.getAddActivityShopLogo().setImageResource(shops.get(position).getShopLogo());
        holder.getAddActivityShopName().setText(shops.get(position).getName());

        holder.getAddActivityWholeItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelectListener.onItemClick(shops.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }


}
