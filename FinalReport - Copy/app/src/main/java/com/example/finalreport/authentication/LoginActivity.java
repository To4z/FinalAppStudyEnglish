package com.example.finalreport.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.finalreport.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail, loginPass;
    Button loginBtn,loginRegBtn;
    FirebaseAuth auth;
    ProgressBar loginProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.login_btn);
        loginEmail = findViewById(R.id.login_email);
        loginPass = findViewById(R.id.login_pass);
        loginRegBtn = findViewById(R.id.login_reg_btn);
        loginProgressbar = findViewById(R.id.login_progressBar);
        loginProgressbar.setVisibility(View.GONE);


        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });
    }

    private void openRegister() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    private void loginUser() {

        String email, pass;
        email = loginEmail.getText().toString().trim();
        pass = loginPass.getText().toString().trim();

        if(email.isEmpty() || pass.isEmpty() ){
            Toast.makeText(this, "please provides all fields", Toast.LENGTH_SHORT).show();
        } else {
            loginProgressbar.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        loginProgressbar.setVisibility(View.GONE);
                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                        finish();
                    } else {
                        loginProgressbar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}