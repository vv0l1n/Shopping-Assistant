package com.wolin.warehouseapp.ui.manageGroupActivities.manageGroupActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseGroupViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseInviteViewModel;
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
import java.util.regex.Pattern;

public class ManageGroupActivity extends AppCompatActivity {

    private TextView groupNameTextView;
    private RecyclerView usersRecyclerView;
    private Button inviteButton;
    private Button leaveButton;
    private Button deleteButton;
    private Button backButton;
    private Dialog dialog;

    private FirebaseInviteViewModel firebaseInviteViewModel;
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

        firebaseInviteViewModel = new ViewModelProvider(this).get(FirebaseInviteViewModel.class);
        firebaseGroupViewModel = new ViewModelProvider(this).get(FirebaseGroupViewModel.class);
        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        groupId = getIntent().getStringExtra("group");

        loadGroup();
        loadDialog();
    }

    public void loadGroup() {
        firebaseGroupViewModel.getGroup(groupId).observe(this, new Observer<Group>() {
            @Override
            public void onChanged(Group group) {
                if(group == null) {
                    Intent selectIntent = new Intent(ManageGroupActivity.this, SelectGroupActivity.class);
                    startActivity(selectIntent);
                } else {
                    groupNameTextView.setText(group.getName());

                    if (!group.getOwner().equals(currentFirebaseUser.getUid())) {
                        inviteButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.purple_200));
                        deleteButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.purple_200));

                        inviteButton.setEnabled(false);
                        deleteButton.setEnabled(false);
                    } else if (group.getOwner().equals(currentFirebaseUser.getUid())) {
                        leaveButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.purple_200));
                        leaveButton.setEnabled(false);
                    }
                    loadRecyclerView(group);
                }
            }
        });

    }

    public void loadRecyclerView(Group group) {
        UserAdapter adapter = new UserAdapter(this, group,  new ArrayList<User>(), firebaseGroupViewModel, firebaseUserViewModel);
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

    private void loadDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.manage_invite_dialog);

        EditText emailField = dialog.findViewById(R.id.manageDialogEmail);
        Button inviteButton = dialog.findViewById(R.id.manageDialogInviteButton);

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr = emailField.getText().toString().trim();
                if(!emailStr.equals("")) {
                    if(Pattern.compile("^(.+)@(.+)\\.(.+)$").matcher(emailStr).matches()) { //email validate
                        firebaseInviteViewModel.inviteUser(emailStr, currentFirebaseUser.getUid(), groupId);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Podany Email jest nieprawidłowy.", Toast.LENGTH_LONG);
                    }
                }
            }
        });
    }

    public void onInviteButtonClick(View view) {
        dialog.show();
    }

    public void onLeaveButtonClick(View view) {
        firebaseGroupViewModel.leave(groupId, currentFirebaseUser.getUid());
    }

    public void onDeleteButtonClick(View view) {
        firebaseGroupViewModel.delete(groupId);
    }

    public void onBackButtonClick(View view) {
        Intent selectIntent = new Intent(ManageGroupActivity.this, SelectGroupActivity.class);
        startActivity(selectIntent);
    }

}
