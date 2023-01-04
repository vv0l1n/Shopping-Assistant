package com.wolin.warehouseapp.ui.mainActivity.adapter.productadapter;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.utils.common.Category;
import com.wolin.warehouseapp.utils.common.SortState;
import com.wolin.warehouseapp.utils.listeners.ItemBuyListener;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder>{

    private final List<Product> items;
    private final ItemSelectListener<Object> itemSelectListener;
    private final ItemBuyListener itemBuyListener;
    private final Resources resources;
    private boolean onlyActive;
    private Category category;

    public ProductAdapter(List<Product> items, ItemSelectListener<Object> itemSelectListener, ItemBuyListener itemBuyListener,
                          Resources resources) {
        this.items = items;
        this.itemSelectListener = itemSelectListener;
        this.itemBuyListener = itemBuyListener;
        this.resources = resources;
        this.onlyActive = true;
        this.category = Category.NONE;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = items.get(position);
        if(onlyActive) {
            if(product.isActive()) {
                if(category == Category.NONE || product.getCategory() == category) {;
                    bindItem(product, holder);

                } else { //if category is not valid
                    holder.getBackground().setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }

            } else { //if category is not none
                holder.getBackground().setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
            }
        } else { //if onlyActive is true dont show not active products
            if (category == Category.NONE || product.getCategory() == category) {
                bindItem(product, holder);

                if(!product.isActive()) {
                    holder.getBoughtButton().setText("Kupiony");
                    holder.getBoughtButton().setBackgroundColor(Color.RED);
                }
            } else {
                holder.getBackground().setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<Product> products, boolean onlyActive, Category category, SortState newSortState) {
        if(items != null) {
            items.clear();
        }
        items.addAll(products);
        this.onlyActive = onlyActive;
        this.category = category;
        sort(newSortState);
        this.notifyItemRangeChanged(0, products.size());
    }

    private void bindItem(Product product, ProductViewHolder holder) {
        if (!(product.getPhoto() == null)) {
            Glide.with(holder.getImageView().getContext()).load(product.getPhoto())
                    .into(holder.getImageView());
        }

        holder.getProductName().setText(product.getName());
        holder.getCount().setText(Integer.toString(product.getCount()));
        holder.getShopLogo().setImageResource(resources.getIdentifier(product.getShop().getShopLogo(), "drawable", "com.wolin.warehouseapp"));

        holder.getBoughtButton().setOnClickListener(view -> itemBuyListener.buy(product));

        holder.getBackground().setOnClickListener(view -> itemSelectListener.onItemClick(product));
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
