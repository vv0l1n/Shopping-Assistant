package com.wolin.warehouseapp.utils.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.List;
import java.util.Locale;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{

    private Context context;
    private List<Product> items;
    private ItemSelectListener<Object> itemSelectListener;
    private FirebaseProductViewModel firebaseProductViewModel;
    private String uid;
    private String groupId;
    private ItemBuyListener itemBuyListener;

    public MainAdapter(Context context, List<Product> items, ItemSelectListener<Object> itemSelectListener, ItemBuyListener itemBuyListener, FirebaseProductViewModel firebaseProductViewModel, String groupId, String uid) {
        this.context = context;
        this.items = items;
        this.itemSelectListener = itemSelectListener;
        this.firebaseProductViewModel = firebaseProductViewModel;
        this.groupId = groupId;
        this.uid = uid;
        this.itemBuyListener = itemBuyListener;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.main_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
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
            itemBuyListener.buy();

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
