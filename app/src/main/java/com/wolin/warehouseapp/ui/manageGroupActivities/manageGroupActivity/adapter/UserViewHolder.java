package com.wolin.warehouseapp.ui.manageGroupActivities.manageGroupActivity.adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.wolin.warehouseapp.R;

public class UserViewHolder extends RecyclerView.ViewHolder{
    private TextView name;
    private TextView lastname;
    private TextView email;
    private ImageButton kickButton;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.manageUserItemName);
        lastname = itemView.findViewById(R.id.manageUserItemLastname);
        email = itemView.findViewById(R.id.manageUserItemEmail);
        kickButton = itemView.findViewById(R.id.manageUserItemKickButton);
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getLastname() {
        return lastname;
    }

    public void setLastname(TextView lastname) {
        this.lastname = lastname;
    }

    public TextView getEmail() {
        return email;
    }

    public void setEmail(TextView email) {
        this.email = email;
    }

    public ImageButton getDeleteButton() {
        return kickButton;
    }

    public void setDeleteButton(ImageButton deleteButton) {
        this.kickButton = deleteButton;
    }
}
