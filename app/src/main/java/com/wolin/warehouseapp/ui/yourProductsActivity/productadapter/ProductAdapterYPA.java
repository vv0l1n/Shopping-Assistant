package com.wolin.warehouseapp.ui.yourProductsActivity.productadapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.utils.listeners.ItemEditListener;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.List;

public class ProductAdapterYPA extends RecyclerView.Adapter<ProductViewHolderYPA>{

    private List<Product> items;
    private ItemSelectListener<Object> itemSelectListener;
    private ItemEditListener<Product> itemEditListener;
    private FirebaseProductViewModel firebaseProductViewModel;
    private String uid;
    private String groupId;
    private Resources resources;

    public ProductAdapterYPA(List<Product> items, ItemSelectListener<Object> itemSelectListener, ItemEditListener<Product> itemEditListener, FirebaseProductViewModel firebaseProductViewModel, String groupId, String uid, Resources resources) {
        this.items = items;
        this.itemSelectListener = itemSelectListener;
        this.itemEditListener = itemEditListener;
        this.firebaseProductViewModel = firebaseProductViewModel;
        this.groupId = groupId;
        this.uid = uid;
        this.resources = resources;
    }

    @NonNull
    @Override
    public ProductViewHolderYPA onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolderYPA(LayoutInflater.from(parent.getContext()).inflate(R.layout.your_products_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolderYPA holder, int position) {
        Product product = items.get(position);
        if(product.getOwner().equals(uid)) {
            if(!(product.getPhoto() == null)) {
                Glide.with(holder.getBackground().getContext()).load(product.getPhoto())
                        .into(holder.getImageView());
            }

            holder.getProductName().setText(product.getName());
            holder.getCount().setText(Integer.toString(product.getCount()));
            holder.getShopLogo().setImageResource(resources.getIdentifier(product.getShop().getShopLogo(),
                    "drawable", "com.wolin.warehouseapp"));

            if(!product.isActive()) {
                holder.getBackground().setBackgroundResource(R.color.lightRed);
                holder.getEditButton().setVisibility(View.GONE);
                holder.getDeleteButton().setVisibility(View.GONE);
            } else {
                holder.getDeleteButton().setOnClickListener(view -> {
                    firebaseProductViewModel.deleteProduct(product.getProductId(), uid, groupId);
                });
                holder.getEditButton().setOnClickListener(view -> {
                    itemEditListener.onItemEdit(product);
                });
            }

            holder.getBackground().setOnClickListener(view -> {
                itemSelectListener.onItemClick(product);
            });

            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(
                    new RecyclerView.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<Product> products, String groupId) {
        this.groupId = groupId;
        if(items != null) {
            items.clear();
        }
        items.addAll(products);
        this.notifyItemRangeChanged(0, products.size());
    }
}
