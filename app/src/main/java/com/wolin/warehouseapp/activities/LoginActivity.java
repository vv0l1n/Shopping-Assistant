package com.wolin.warehouseapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wolin.warehouseapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private FirebaseAuth auth;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();
        register = findViewById(R.id.register);

        login.setWidth(register.getWidth());

        login.setOnClickListener(view -> {
            String emailStr = email.getText().toString();
            String passwordStr = password.getText().toString();

            if(TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passwordStr)) {
                Toast.makeText(LoginActivity.this, "Podaj email i hasło.", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(emailStr, passwordStr);
            }
        });

        register.setOnClickListener(view -> {
            Intent registerActivityIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerActivityIntent);
        });

    }

    private void loginUser(String emailStr, String passwordStr) {
        auth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Intent MainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(MainActivityIntent);
                } else {
                    Toast.makeText(LoginActivity.this, "Logowanie nieudane.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}