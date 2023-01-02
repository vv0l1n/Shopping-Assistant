package com.wolin.warehouseapp.ui.mainActivity.adapter.productadapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.utils.listeners.ItemBuyListener;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder>{

    private List<Product> items;
    private ItemSelectListener<Object> itemSelectListener;
    private ItemBuyListener itemBuyListener;
    private Resources resources;

    public ProductAdapter(List<Product> items, ItemSelectListener<Object> itemSelectListener, ItemBuyListener itemBuyListener,
                          Resources resources) {
        this.items = items;
        this.itemSelectListener = itemSelectListener;
        this.itemBuyListener = itemBuyListener;
        this.resources = resources;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = items.get(position);
        if(!(product.getPhoto() == null)) {
            Glide.with(holder.getImageView().getContext()).load(product.getPhoto())
                    .into(holder.getImageView());
        }

        holder.getProductName().setText(product.getName());
        holder.getCount().setText(Integer.toString(product.getCount()));
        holder.getShopLogo().setImageResource(resources.getIdentifier(product.getShop().getShopLogo(), "drawable", "com.wolin.warehouseapp"));

        holder.getBoughtButton().setOnClickListener(view -> {
            itemBuyListener.buy(product);
        });

        holder.getBackground().setOnClickListener(view -> {
            itemSelectListener.onItemClick(product);
        });

        if(!product.isActive()) {
            holder.getBoughtButton().setText("Kupiony");
            holder.getBoughtButton().setBackgroundColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<Product> products) {
        if(items != null) {
            items.clear();
        }
        items.addAll(products);
        this.notifyItemRangeChanged(0, products.size());
    }
}
