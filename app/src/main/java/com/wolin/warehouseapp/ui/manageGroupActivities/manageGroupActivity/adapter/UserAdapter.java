package com.wolin.warehouseapp.ui.manageGroupActivities.manageGroupActivity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseGroupViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.User;

import java.util.List;
import java.util.Set;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{

    private Context context;
    private FirebaseGroupViewModel firebaseGroupViewModel;
    private Group group;
    private List<User> userList;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public UserAdapter(Context context, Group group, List<User> userList, FirebaseGroupViewModel firebaseGroupViewModel) {
        this.context = context;
        this.group = group;
        this.userList = userList;
        this.firebaseGroupViewModel = firebaseGroupViewModel;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        System.out.println("Lista użytkowników: " + userList);
        if(group.getMembers() != null) {
            User user = userList.get(position);
            holder.getName().setText(user.getName());
            holder.getLastname().setText(user.getLastName());
            holder.getEmail().setText(user.getEmail());

            if(group.getOwner().equals(currentFirebaseUser.getUid())) {
                if(!currentFirebaseUser.getUid().equals(user.getUid())) {
                    holder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            firebaseGroupViewModel.kickMember(user.getUid(), group.getId());
                        }
                    });
                } else {
                    holder.getDeleteButton().setVisibility(View.GONE);
                }
            } else {
                holder.getDeleteButton().setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateData(List<User> users) {
        if(userList != null) {
            userList.clear();
        }
        userList.addAll(users);
        System.out.println("adapter: " + users);
        this.notifyItemRangeChanged(0, userList.size());
    }
}
