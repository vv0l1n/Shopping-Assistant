package com.wolin.warehouseapp.ui.manageGroupActivities.manageGroupActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseGroupViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.ui.mainActivity.MainActivity;
import com.wolin.warehouseapp.ui.manageGroupActivities.manageGroupActivity.adapter.UserAdapter;
import com.wolin.warehouseapp.ui.manageGroupActivities.selectGroupActivity.SelectGroupActivity;
import com.wolin.warehouseapp.ui.manageGroupActivities.selectGroupActivity.adapter.GroupAdapter;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ManageGroupActivity extends AppCompatActivity {

    private TextView groupNameTextView;
    private RecyclerView usersRecyclerView;
    private Button inviteButton;
    private Button leaveButton;
    private Button deleteButton;
    private Button backButton;

    private FirebaseGroupViewModel firebaseGroupViewModel;
    private FirebaseUserViewModel firebaseUserViewModel;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String groupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        groupNameTextView = findViewById(R.id.manageGroupName);
        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        inviteButton = findViewById(R.id.manageInviteButton);
        leaveButton = findViewById(R.id.manageLeaveGroupButton);
        deleteButton = findViewById(R.id.manageDeleteGroupButton);
        backButton = findViewById(R.id.manageBackButton);

        firebaseGroupViewModel = new ViewModelProvider(this).get(FirebaseGroupViewModel.class);
        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        groupId = getIntent().getStringExtra("group");

        loadGroup();
    }

    public void loadGroup() {
        System.out.println("LoadGroup");
        firebaseGroupViewModel.getGroup(groupId).observe(this, new Observer<Group>() {
            @Override
            public void onChanged(Group group) {
                System.out.println("USUNIETY");
                groupNameTextView.setText(group.getName());
                loadRecyclerView(group);
            }
        });

    }

    public void loadRecyclerView(Group group) {
        UserAdapter adapter = new UserAdapter(this, group,  new ArrayList<User>(), firebaseGroupViewModel);
        usersRecyclerView.setAdapter(adapter);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersRecyclerView.refreshDrawableState();

        firebaseUserViewModel.getUsers(group.getMembers()).observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                System.out.println("loadRecyclerViewOnChanged: " + users);
                if(users.size() > 0) {
                    adapter.updateData(users);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void onInviteButtonClick(View view) {

    }

    public void onLeaveButtonClick(View view) {

    }

    public void onDeleteButtonClick(View view) {

    }

    public void onBackButtonClick(View view) {
        Intent selectIntent = new Intent(ManageGroupActivity.this, SelectGroupActivity.class);
        startActivity(selectIntent);
    }

}
