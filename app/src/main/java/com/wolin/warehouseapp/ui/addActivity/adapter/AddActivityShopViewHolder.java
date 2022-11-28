package com.wolin.warehouseapp.ui.addActivity.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.wolin.warehouseapp.R;

public class AddActivityShopViewHolder extends RecyclerView.ViewHolder{

    private ImageView addActivityShopLogo;
    private TextView addActivityShopName;
    private ConstraintLayout addActivityWholeItem;

    public AddActivityShopViewHolder(@NonNull View itemView) {
        super(itemView);
        addActivityShopLogo = itemView.findViewById(R.id.AddActivityShopLogo);
        addActivityShopName = itemView.findViewById(R.id.AddActivityShopName);
        addActivityWholeItem = itemView.findViewById(R.id.addActivityWholeItem);
    }

    public ImageView getAddActivityShopLogo() {
        return addActivityShopLogo;
    }

    public void setAddActivityShopLogo(ImageView addActivityShopLogo) {
        this.addActivityShopLogo = addActivityShopLogo;
    }

    public TextView getAddActivityShopName() {
        return addActivityShopName;
    }

    public void setAddActivityShopName(TextView addActivityShopName) {
        this.addActivityShopName = addActivityShopName;
    }

    public ConstraintLayout getAddActivityWholeItem() {
        return addActivityWholeItem;
    }

    public void setAddActivityWholeItem(ConstraintLayout addActivityWholeItem) {
        this.addActivityWholeItem = addActivityWholeItem;
    }
}
