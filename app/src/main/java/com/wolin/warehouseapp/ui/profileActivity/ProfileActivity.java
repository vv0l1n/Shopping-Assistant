package com.wolin.warehouseapp.ui.profileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.ui.invitesActivity.InvitesActivity;
import com.wolin.warehouseapp.ui.mainActivity.MainActivity;
import com.wolin.warehouseapp.ui.yourProductsActivity.YourProductsActivity;
import com.wolin.warehouseapp.utils.model.User;

public class ProfileActivity extends AppCompatActivity {

    private TextView name;
    private TextView lastname;
    private TextView email;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseUserViewModel firebaseUserViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.profileName);
        lastname = findViewById(R.id.profileLastName);
        email = findViewById(R.id.profileEmail);

        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        loadUserData();
    }

    private void loadUserData() {
        firebaseUserViewModel.getUser(currentFirebaseUser.getUid()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                name.setText(user.getName());
                lastname.setText(user.getLastName());
                email.setText(user.getEmail());
            }
        });
    }

    public void onProfileBackButtonClick(View view) {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
