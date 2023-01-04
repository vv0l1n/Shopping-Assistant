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
import com.wolin.warehouseapp.utils.common.Category;
import com.wolin.warehouseapp.utils.common.SortState;
import com.wolin.warehouseapp.utils.listeners.ItemDeleteListener;
import com.wolin.warehouseapp.utils.listeners.ItemEditListener;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductAdapterYPA extends RecyclerView.Adapter<ProductViewHolderYPA>{

    private List<Product> items;
    private ItemSelectListener<Object> itemSelectListener;
    private ItemEditListener<Product> itemEditListener;
    private ItemDeleteListener itemDeleteListener;
    private String uid;
    private Resources resources;
    private boolean onlyActive;
    private Category category;

    public ProductAdapterYPA(List<Product> items, ItemSelectListener<Object> itemSelectListener, ItemEditListener<Product> itemEditListener, ItemDeleteListener itemDeleteListener, String uid, Resources resources) {
        this.items = items;
        this.itemSelectListener = itemSelectListener;
        this.itemEditListener = itemEditListener;
        this.itemDeleteListener = itemDeleteListener;
        this.uid = uid;
        this.resources = resources;
        this.onlyActive = true;
        this.category = Category.NONE;
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
            if(onlyActive) {
                if(product.isActive()) {
                    if(product.getCategory() == category || category == Category.NONE) {
                        setItem(product, holder);
                    } else { //if product is active but not valid category
                        holder.itemView.setVisibility(View.GONE);
                        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                    }
                } else { //if product is not active
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }
            } else {
                if (product.getCategory() == category || category == Category.NONE) {

                    setItem(product, holder);

                } else { //if category is not valid
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }
            }
        } else { //if product is not active
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<Product> products, boolean onlyActive, Category category, SortState sortState) {
        if(items != null) {
            items.clear();
        }
        items.addAll(products);
        this.onlyActive = onlyActive;
        this.category = category;
        sort(sortState);
        this.notifyItemRangeChanged(0, products.size());
    }

    private void setItem(Product product, ProductViewHolderYPA holder) {
        if(!(product.getPhoto() == null)) {
            Glide.with(holder.getBackground().getContext()).load(product.getPhoto())
                    .into(holder.getImageView());
        }

        holder.getProductName().setText(product.getName());
        holder.getCount().setText(Integer.toString(product.getCount()));
        holder.getShopLogo().setImageResource(resources.getIdentifier(product.getShop().getShopLogo(),
                "drawable", "com.wolin.warehouseapp"));

        holder.getDeleteButton().setOnClickListener(view -> {
            items.remove(product);
            itemDeleteListener.delete(product);
            notifyItemRemoved(holder.getBindingAdapterPosition());
            notifyItemRangeChanged(holder.getBindingAdapterPosition(), items.size());
        });

        if(!product.isActive()) {
            holder.getBackground().setBackgroundResource(R.color.lightRed);
            holder.getEditButton().setVisibility(View.GONE);
        } else {
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
    }

    private void sort(SortState sortState) {
        switch (sortState) {
            case DESCPRIORITY:
                Collections.sort(items, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        if(o1.getPriority().equals("Wysoki")) {

                            if(o2.getPriority().equals("Wysoki")) {
                                return 0;
                            } else if(o2.getPriority().equals("Średni")) {
                                return -1;
                            } else if(o2.getPriority().equals("Niski")){
                                return -1;
                            }

                        } else if(o1.getPriority().equals("Średni")) {
                            if(o2.getPriority().equals("Wysoki")) {
                                return 1;
                            } else if(o2.getPriority().equals("Średni")) {
                                return 0;
                            } else if(o2.getPriority().equals("Niski")){
                                return -1;
                            }

                        } else if(o1.getPriority().equals("Niski")){
                            if(o2.getPriority().equals("Wysoki")) {
                                return 1;
                            } else if(o2.getPriority().equals("Średni")) {
                                return 1;
                            } else if(o2.getPriority().equals("Niski")){
                                return 0;
                            }
                        }
                        throw new RuntimeException();
                    }
                });
                break;
            case ASCPRIORITY:
                Collections.sort(items, (o1, o2) -> {
                    if(o2.getPriority().equals("Wysoki")) {

                        if(o1.getPriority().equals("Wysoki")) {
                            return 0;
                        } else if(o1.getPriority().equals("Średni")) {
                            return -1;
                        } else if(o1.getPriority().equals("Niski")){
                            return -1;
                        }

                    } else if(o2.getPriority().equals("Średni")) {
                        if(o1.getPriority().equals("Wysoki")) {
                            return 1;
                        } else if(o1.getPriority().equals("Średni")) {
                            return 0;
                        } else if(o1.getPriority().equals("Niski")){
                            return -1;
                        }

                    } else if(o2.getPriority().equals("Niski")){
                        if(o1.getPriority().equals("Wysoki")) {
                            return 1;
                        } else if(o1.getPriority().equals("Średni")) {
                            return 1;
                        } else if(o1.getPriority().equals("Niski")){
                            return 0;
                        }
                    }
                    throw new RuntimeException();
                });
                break;
            case DESCDATE:
                Collections.sort(items, (o1, o2) -> {

                    if(o1.getDateToBuy().equals("Brak daty wygaśnięcia.")) {
                        if(o2.getDateToBuy().equals("Brak daty wygaśnięcia.")) {
                            return 0;
                        } else {
                            return -1;
                        }
                    } else if (o2.getDateToBuy().equals("Brak daty wygaśnięcia.")) {
                        return -1;
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    LocalDate date1 = LocalDate.parse(o1.getDateToBuy(), formatter);
                    LocalDate date2 = LocalDate.parse(o2.getDateToBuy(), formatter);

                    return date1.compareTo(date2);
                });
                break;
            case ASCDATE:
                Collections.sort(items, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {

                        if(o1.getDateToBuy().equals("Brak daty wygaśnięcia.")) {
                            if(o2.getDateToBuy().equals("Brak daty wygaśnięcia.")) {
                                return 0;
                            } else {
                                return -1;
                            }
                        } else if (o2.getDateToBuy().equals("Brak daty wygaśnięcia.")) {
                            return -1;
                        }

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        LocalDate date1 = LocalDate.parse(o1.getDateToBuy(), formatter);
                        LocalDate date2 = LocalDate.parse(o2.getDateToBuy(), formatter);

                        return date2.compareTo(date1);
                    }
                });
                break;
        }
    }
}
