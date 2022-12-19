package com.wolin.warehouseapp.ui.invitesActivity.adapter;

import com.wolin.warehouseapp.R;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InvitesViewHolder extends RecyclerView.ViewHolder{

    private TextView inviteText;
    private Button accept;
    private Button decline;

    public InvitesViewHolder(@NonNull View itemView) {
        super(itemView);
        inviteText = itemView.findViewById(R.id.inviteItemInviteText);
        accept = itemView.findViewById(R.id.inviteItemAcceptButton);
        decline = itemView.findViewById(R.id.inviteItemDeclineButton);
    }

    public TextView getInviteText() {
        return inviteText;
    }

    public void setInviteText(TextView inviteText) {
        this.inviteText = inviteText;
    }

    public Button getAccept() {
        return accept;
    }

    public void setAccept(Button accept) {
        this.accept = accept;
    }

    public Button getDecline() {
        return decline;
    }

    public void setDecline(Button decline) {
        this.decline = decline;
    }
}
