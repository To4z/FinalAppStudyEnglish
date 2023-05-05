package com.example.finalreport.leaderboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalreport.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;

    List<ScoreData> list;

    ScoreAdapter scoreAdapter;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.learderboard_recycleview);
        progressBar = findViewById(R.id.progressBar);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        reference = FirebaseDatabase.getInstance("https://skyfun-english-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Score");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();

        reference.orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ScoreData data = snapshot.getValue(ScoreData.class);
                    list.add(data);
                }
                scoreAdapter = new ScoreAdapter(list, LeaderboardActivity.this);
                recyclerView.setAdapter(scoreAdapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LeaderboardActivity.this, "databaseError" , Toast.LENGTH_SHORT).show();
            }
        });

    }
}