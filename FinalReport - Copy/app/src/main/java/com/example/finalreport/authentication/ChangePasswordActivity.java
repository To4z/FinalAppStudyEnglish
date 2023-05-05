package com.example.finalreport.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalreport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText mail, old, newP;

    Button btnChange;

    private FirebaseAuth auth;

    ProgressBar loginProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mail = findViewById(R.id.chang_email);
        old = findViewById(R.id.change_pass);
        newP = findViewById(R.id.new_pass);
        btnChange = findViewById(R.id.login_btn);
        loginProgressbar = findViewById(R.id.change_progressBar);

        loginProgressbar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String mailUser = user.getEmail();
        mail.setText(mailUser);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgressbar.setVisibility(View.VISIBLE);
                FirebaseUser user = auth.getCurrentUser();
                String email = mail.getText().toString();
                String oldPassword = old.getText().toString();
                String newPassword = newP.getText().toString();
                // reauthenticate the user
                if (user != null && user.getEmail() != null && user.getEmail().equals(email)) {
                    auth.signInWithEmailAndPassword(email, oldPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // update the user's password
                                    loginProgressbar.setVisibility(View.GONE);
                                    user.updatePassword(newPassword)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(ChangePasswordActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(ChangePasswordActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(ChangePasswordActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    loginProgressbar.setVisibility(View.GONE);
                    Toast.makeText(ChangePasswordActivity.this, "Please enter the correct email!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
