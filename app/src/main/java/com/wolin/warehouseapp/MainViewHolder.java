package com.wolin.warehouseapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MainViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView productName;
    private TextView zone;
    private TextView rack;
    private TextView shelf;
    private TextView count;
    private TextView category;

    public MainViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        productName = itemView.findViewById(R.id.productName);
        zone = itemView.findViewById(R.id.zone);
        rack = itemView.findViewById(R.id.rack);
        shelf = itemView.findViewById(R.id.shelf);
        count = itemView.findViewById(R.id.count);
        category = itemView.findViewById(R.id.category);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getProductName() {
        return productName;
    }

    public void setProductName(TextView productName) {
        this.productName = productName;
    }

    public TextView getZone() {
        return zone;
    }

    public void setZone(TextView zone) {
        this.zone = zone;
    }

    public TextView getRack() {
        return rack;
    }

    public void setRack(TextView rack) {
        this.rack = rack;
    }

    public TextView getShelf() {
        return shelf;
    }

    public void setShelf(TextView shelf) {
        this.shelf = shelf;
    }

    public TextView getCount() {
        return count;
    }

    public void setCount(TextView count) {
        this.count = count;
    }

    public TextView getCategory() {
        return category;
    }

    public void setCategory(TextView category) {
        this.category = category;
    }
}
