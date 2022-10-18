package com.wolin.warehouseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(view -> {
            String emailStr = email.getText().toString();
            String passwordStr = password.getText().toString();

            if(TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passwordStr)) {
                Toast.makeText(MainActivity.this, "Podaj email i hasło.", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(emailStr, passwordStr);
            }
        });
    }

    private void loginUser(String emailStr, String passwordStr) {
        auth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Zalogowano.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Logowanie nieudane.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}