package com.wolin.warehouseapp.ui.createGroupActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseGroupViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.ui.mainActivity.MainActivity;
import com.wolin.warehouseapp.utils.model.User;

import java.util.List;

public class CreateGroupActivity  extends AppCompatActivity {

    private EditText groupName;
    private Button cancelButton;
    private Button createButton;

    private List<String> userGroups;
    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uid;
    private FirebaseUserViewModel firebaseUserViewModel;
    private FirebaseGroupViewModel firebaseGroupViewModel;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        groupName = findViewById(R.id.groupNameEditText);
        cancelButton = findViewById(R.id.cancelGroupButton);
        createButton = findViewById(R.id.createGroupButton);

        uid = currentFirebaseUser.getUid();
        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);
        firebaseGroupViewModel = new ViewModelProvider(this).get(FirebaseGroupViewModel.class);

        firebaseUserViewModel.getUser(uid).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                CreateGroupActivity.this.user = user;
                userGroups = CreateGroupActivity.this.user.getGroups();
                System.out.println("grupy: " + userGroups);
            }
        });

    }

    public void onCreateGroupCreate(View view) {
        for (String name : userGroups) {
            if(name.equals(user.getUid()+ "-" + groupName.getText().toString())) {
                Toast.makeText(this, "Posiadasz już w grupie o takiej nazwie.", Toast.LENGTH_LONG).show();
                groupName.setText("");
                return;
            }
        }
        firebaseGroupViewModel.addGroup(user.getUid(), groupName.getText().toString(), userGroups);
        Intent intent = new Intent(CreateGroupActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onCreateGroupCancel(View view) {
        Intent intent = new Intent(CreateGroupActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
