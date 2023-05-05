package com.example.finalreport.ui.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalreport.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivateKey;
import java.util.Objects;

public class ScoreActivity extends AppCompatActivity {

    TextView scoreTxt, totalTxt;
    int score,total;

    FirebaseUser user;

    DatabaseReference reference;
    private Button back, again;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        back = findViewById(R.id.back);
        again = findViewById(R.id.again);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, QuizFragment.class);
                startActivity(intent);
            }
        });

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, StartQuiz.class);
                startActivity(intent);
            }
        });

        score = getIntent().getIntExtra("score", 0);

        total = getIntent().getIntExtra("total", 0);

        scoreTxt = findViewById(R.id.score);
        totalTxt = findViewById(R.id.total);

        scoreTxt.setText(String.valueOf(score));
        totalTxt.setText(String.valueOf(total));

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        reference = FirebaseDatabase.getInstance("https://skyfun-english-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference.child("Score").child(user.getUid()).child("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    score += Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                }
                dataSnapshot.getRef().setValue(score);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}