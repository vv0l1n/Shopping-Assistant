package com.wolin.warehouseapp.ui.manageGroupActivities.selectGroupActivity.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.wolin.warehouseapp.R;

public class GroupViewHolder extends RecyclerView.ViewHolder{
    private ConstraintLayout groupItem;
    private TextView groupName;

    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);
        groupItem = itemView.findViewById(R.id.selectGroupItemContainer);
        groupName = itemView.findViewById(R.id.selectGroupItemName);
    }

    public ConstraintLayout getGroupItem() {
        return groupItem;
    }

    public void setGroupItem(ConstraintLayout groupItem) {
        this.groupItem = groupItem;
    }

    public TextView getGroupName() {
        return groupName;
    }

    public void setGroupName(TextView groupName) {
        this.groupName = groupName;
    }
}
