package com.wolin.warehouseapp.ui.invitesActivity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.ui.yourProductsActivity.productadapter.ProductViewHolderYPA;
import com.wolin.warehouseapp.utils.listeners.ButtonClickedListener;
import com.wolin.warehouseapp.utils.model.GroupInvite;

import java.util.List;

public class InvitesAdapter extends RecyclerView.Adapter<InvitesViewHolder>{

    private List<GroupInvite> invites;
    private ButtonClickedListener listener;

    public InvitesAdapter(List<GroupInvite> invites, ButtonClickedListener listener) {
        this.invites = invites;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InvitesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvitesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InvitesViewHolder holder, int position) {
        System.out.println("ADAPTER inv");
            GroupInvite invite = invites.get(position);

            holder.getInviteText().setText("Dnia " + invite.getDate() + " użytkownik " + invite.getInviterEmail() +
                    " zaprosił Cię do grupy: " + invite.getGroupName());

            holder.getAccept().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.clicked(invite, "accept");
                }
            });

            holder.getDecline().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.clicked(invite, "decline");
                }
            });
    }

    @Override
    public int getItemCount() {
        return invites.size();
    }

    public void update(List<GroupInvite> groupInvites) {
        invites.clear();
        invites.addAll(groupInvites);
        this.notifyDataSetChanged();
    }
}
