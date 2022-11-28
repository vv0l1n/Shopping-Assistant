package com.wolin.warehouseapp.ui.mainActivity.adapter.productadapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder>{

    private Context context;
    private List<Product> items;
    private ItemSelectListener<Object> itemSelectListener;
    private FirebaseProductViewModel firebaseProductViewModel;
    private String uid;
    private String groupId;

    public ProductAdapter(Context context, List<Product> items, ItemSelectListener<Object> itemSelectListener, FirebaseProductViewModel firebaseProductViewModel, String groupId, String uid) {
        this.context = context;
        this.items = items;
        this.itemSelectListener = itemSelectListener;
        this.firebaseProductViewModel = firebaseProductViewModel;
        this.groupId = groupId;
        this.uid = uid;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.main_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        System.out.println("1");
        Product product = items.get(position);
        System.out.println("2");
        if(!(product.getPhoto() == null)) {
            Glide.with(holder.getImageView().getContext()).load(product.getPhoto())
                    .into(holder.getImageView());
        }
        System.out.println("3");
        holder.getProductName().setText(product.getName());
        holder.getCount().setText(Integer.toString(product.getCount()));
        holder.getShopLogo().setImageResource(product.getShop().getShopLogo());
        holder.getBoughtButton().setOnClickListener(view -> {
            firebaseProductViewModel.setBought(product.getProductId(), uid, groupId);
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

    public void updateData(List<Product> products, String groupId) {
        this.groupId = groupId;
        System.out.println("aktualizuje dane");
        if(items != null) {
            items.clear();
        }
        items.addAll(products);
        System.out.println("koniec metody updateData");
        this.notifyItemRangeChanged(0, products.size());
    }
}
