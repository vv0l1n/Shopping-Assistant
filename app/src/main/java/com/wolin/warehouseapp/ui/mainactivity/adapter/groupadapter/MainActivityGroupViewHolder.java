package com.wolin.warehouseapp.ui.mainactivity.adapter.groupadapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.wolin.warehouseapp.R;

public class MainActivityGroupViewHolder extends RecyclerView.ViewHolder{
    private ConstraintLayout groupItem;
    private TextView mainActivityGroupName;

    public MainActivityGroupViewHolder(@NonNull View itemView) {
        super(itemView);
        groupItem = itemView.findViewById(R.id.groupItem);
        mainActivityGroupName = itemView.findViewById(R.id.groupDialogTextView);
    }

    public ConstraintLayout getGroupItem() {
        return groupItem;
    }

    public void setGroupItem(ConstraintLayout groupItem) {
        this.groupItem = groupItem;
    }

    public TextView getMainActivityGroupName() {
        return mainActivityGroupName;
    }

    public void setMainActivityGroupName(TextView mainActivityGroupName) {
        this.mainActivityGroupName = mainActivityGroupName;
    }
}
