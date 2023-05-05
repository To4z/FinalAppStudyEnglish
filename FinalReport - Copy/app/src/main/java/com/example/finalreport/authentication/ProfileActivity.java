package com.example.finalreport.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.finalreport.MainActivity;
import com.example.finalreport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName,tvMail,tvScore;
    ImageView userImage;
    Button btnLogout;

    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvName = findViewById(R.id.tv_name);
        tvMail = findViewById(R.id.tv_mail);
        tvScore = findViewById(R.id.tv_score);
        userImage = findViewById(R.id.user_img);
        btnLogout = findViewById(R.id.logout_btn);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference reference = FirebaseDatabase.getInstance("https://skyfun-english-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
         tvName.setText(firebaseUser.getDisplayName());
         tvMail.setText(firebaseUser.getEmail());
        reference.child("Score").child(firebaseUser.getUid()).child("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tvScore.setText(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                }
                tvScore.setText("0");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        Glide.with(ProfileActivity.this).load(firebaseUser.getPhotoUrl()).into(userImage);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}