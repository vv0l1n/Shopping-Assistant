package com.wolin.warehouseapp.ui.mainactivity.adapter.productadapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wolin.warehouseapp.R;


public class ProductViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView productName;
    private TextView count;
    private Button boughtButton;
    private ImageView shopLogo;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        productName = itemView.findViewById(R.id.productName);
        count = itemView.findViewById(R.id.count);
        boughtButton = itemView.findViewById(R.id.boughtButton);
        shopLogo = itemView.findViewById(R.id.shopLogo);
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

    public TextView getCount() {
        return count;
    }

    public void setCount(TextView count) {
        this.count = count;
    }

    public Button getBoughtButton() {
        return boughtButton;
    }

    public void setBoughtButton(Button boughtButton) {
        this.boughtButton = boughtButton;
    }

    public ImageView getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(ImageView shopLogo) {
        this.shopLogo = shopLogo;
    }
}
