package com.wolin.warehouseapp.ui.yourProductsActivity.productadapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.wolin.warehouseapp.R;


public class ProductViewHolderYPA extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView productName;
    private TextView count;
    private Button editButton;
    private Button deleteButton;
    private ImageView shopLogo;
    private ConstraintLayout background;

    public ProductViewHolderYPA(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageViewYPA);
        productName = itemView.findViewById(R.id.productNameYPA);
        count = itemView.findViewById(R.id.countYPA);
        editButton = itemView.findViewById(R.id.editButtonYPA);
        deleteButton = itemView.findViewById(R.id.deleteButtonYPA);
        shopLogo = itemView.findViewById(R.id.shopLogoYPA);
        background = itemView.findViewById(R.id.itemBackgroundYPA);
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

    public Button getEditButton() {
        return editButton;
    }

    public void setEditButton(Button editButton) {
        this.editButton = editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public ImageView getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(ImageView shopLogo) {
        this.shopLogo = shopLogo;
    }

    public ConstraintLayout getBackground() {
        return background;
    }

    public void setBackground(ConstraintLayout background) {
        this.background = background;
    }
}
