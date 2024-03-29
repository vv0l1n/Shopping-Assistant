package com.wolin.warehouseapp.ui.registerActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.ui.loginActivity.LoginActivity;
import com.wolin.warehouseapp.utils.model.User;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText repeat;
    private EditText name;
    private EditText lastName;
    private Button register;
    private Button cancel;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseUserViewModel firebaseUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);
        repeat = findViewById(R.id.repeatField);
        name = findViewById(R.id.nameField);
        lastName = findViewById(R.id.lastNameField);
        register = findViewById(R.id.registerButton);
        cancel = findViewById(R.id.cancelButton);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");

        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        register.setOnClickListener(view -> {
            String emailStr = email.getText().toString();
            String passwordStr = password.getText().toString();
            String repeatStr = repeat.getText().toString();
            String nameStr = name.getText().toString();
            String lastNameStr = lastName.getText().toString();

            if(TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passwordStr) || TextUtils.isEmpty(passwordStr) || TextUtils.isEmpty(passwordStr) || TextUtils.isEmpty(passwordStr) ) {
                Toast.makeText(RegisterActivity.this, "Podaj wszystkie dane.", Toast.LENGTH_SHORT).show();
            } else if(!passwordStr.equals(repeatStr)){
                Toast.makeText(RegisterActivity.this, "Podane hasła są różne. " + passwordStr + " " + repeatStr, Toast.LENGTH_SHORT).show();
            } else if(!Pattern.compile("^(.+)@(.+)\\.(.+)$").matcher(emailStr).matches()) {
                Toast.makeText(RegisterActivity.this, "Niepoprawny email.", Toast.LENGTH_SHORT).show();
            } else if(passwordStr.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Hasło jest za krótkie.", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(emailStr, passwordStr, nameStr, lastNameStr);
            }
        });

        cancel.setOnClickListener(view -> {
            Intent LoginActivityIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(LoginActivityIntent);
        });


    }

    private void registerUser(String emailStr, String passwordStr, String nameStr, String lastNameStr) {
        auth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                User user = new User(authResult.getUser().getUid(), emailStr, nameStr, lastNameStr, null);
                firebaseUserViewModel.registerUser(user);
                Intent loginActivityIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginActivityIntent);
            }
        });
    }
}