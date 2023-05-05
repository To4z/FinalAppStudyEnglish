package com.example.finalreport.chaptertopics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalreport.R;
import com.example.finalreport.chapter.one.Hello;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TopicActivity extends AppCompatActivity {

    Toolbar toolbar;
    ExpandableHeightGridView gridView;
    String chapterName;

    TopicAdapter topicAdapter;
    String[] arr = null;
    ImageView chapterImage;

    List<ChapterName> list;
//    String[] chapter1 = {"Hello", "Good morning", "Good afternoon", "Good evening", "Goodnight"};
//    String[] chapter2 = {"Hello", "Good morning", "Good afternoon", "Good evening", "Goodnight"};
//    String[] chapter3 = {"Hello", "Good morning", "Good afternoon", "Good evening", "Goodnight"};
//    String[] chapter4 = {"Hello", "Good morning", "Good afternoon", "Good evening", "Goodnight"};
//    String[] chapter5 = {"Hello", "Good morning", "Good afternoon", "Good evening", "Goodnight"};
    String[] chapter1;
    String[] chapter2;
    String[] chapter3;
    String[] chapter4;
    String[] chapter5;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        list = new ArrayList<>();
        // set toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // get gridview
        //chapterName = getIntent().getStringExtra("chapterName");
        chapterImage = findViewById(R.id.chapter_image);
        gridView = findViewById(R.id.topics_name);
        gridView.setExpanded(true);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        reference = FirebaseDatabase.getInstance("https://skyfun-english-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        reference.child("Chapter").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String tp1 = snapshot.child("tp1").getValue() != null ? snapshot.child("tp1").getValue().toString() : "1";
                    String tp2 = snapshot.child("tp2").getValue() != null ? snapshot.child("tp2").getValue().toString() : "2";
                    String tp3 = snapshot.child("tp3").getValue() != null ? snapshot.child("tp3").getValue().toString() : "3";
                    String tp4 = snapshot.child("tp4").getValue() != null ? snapshot.child("tp4").getValue().toString() : "4";
                    String tp5 = snapshot.child("tp5").getValue() != null ? snapshot.child("tp5").getValue().toString() : "5";
                    list.add(new ChapterName(tp1,tp2,tp3,tp4,tp5));
                }
                if(list.size()> 0){
                    for (int i = 0 ; i < 5; i++){
                        if(i == 0){
                            chapter1 = new String[] {list.get(i).getTp1(),list.get(i).getTp2(),list.get(i).getTp3(),list.get(i).getTp4(),list.get(i).getTp5()};
                        } else if(i == 1){
                            chapter2 = new String[] {list.get(i).getTp1(),list.get(i).getTp2(),list.get(i).getTp3(),list.get(i).getTp4(),list.get(i).getTp5()};
                        } else if(i == 2){
                            chapter3 = new String[] {list.get(i).getTp1(),list.get(i).getTp2(),list.get(i).getTp3(),list.get(i).getTp4(),list.get(i).getTp5()};
                        }else if(i == 3){
                            chapter4 = new String[] {list.get(i).getTp1(),list.get(i).getTp2(),list.get(i).getTp3(),list.get(i).getTp4(),list.get(i).getTp5()};
                        } else {
                            chapter5 = new String[] {list.get(i).getTp1(),list.get(i).getTp2(),list.get(i).getTp3(),list.get(i).getTp4(),list.get(i).getTp5()};
                        }
                    }
                    chapterName = getIntent().getStringExtra("chapterName");
                    if (chapterName != null) {
                        compareAndOpen();
                    } else {
                        // handle the case when chapterName is null
                        Toast.makeText(TopicActivity.this, "Chapter name is null", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle cancelled event here
                Toast.makeText(TopicActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void compareAndOpen() {

        if (chapterName.equals("heading1")) {
            arr = chapter1;
//            chapterImage.setImageResource(R.drawable.img_card1);
            Glide.with(TopicActivity.this)
                    .load("https://firebasestorage.googleapis.com/v0/b/skyfun-english.appspot.com/o/img_card1.png?alt=media&token=260d0d09-0fff-415b-afaf-cb83e63fe6df")
                    .into(chapterImage);
            getSupportActionBar().setTitle("Heading 1");
        }
        else if (chapterName.equals("heading2")) {
            arr = chapter2;
//            chapterImage.setImageResource(R.drawable.img_card1);
            Glide.with(TopicActivity.this)
                    .load("https://firebasestorage.googleapis.com/v0/b/skyfun-english.appspot.com/o/img_card2.png?alt=media&token=c6d8e533-e46d-46c1-88ba-f474bf625c1d")
                    .into(chapterImage);
            getSupportActionBar().setTitle("Heading 2");
        }
        else if (chapterName.equals("heading3")) {
            arr = chapter3;
//            chapterImage.setImageResource(R.drawable.img_card1);
            Glide.with(TopicActivity.this)
                    .load("https://firebasestorage.googleapis.com/v0/b/skyfun-english.appspot.com/o/img_card3.png?alt=media&token=2cb7f9a6-7aef-47e2-b308-05b7b48ec574")
                    .into(chapterImage);
            getSupportActionBar().setTitle("Heading 3");
        }
        else if (chapterName.equals("heading4")) {
            arr = chapter4;
//            chapterImage.setImageResource(R.drawable.img_card1);
            Glide.with(TopicActivity.this)
                    .load("https://firebasestorage.googleapis.com/v0/b/skyfun-english.appspot.com/o/img_card4.png?alt=media&token=74dc690c-c661-459f-ab0a-8ad10790308b")
                    .into(chapterImage);
            getSupportActionBar().setTitle("Heading 4");
        }
        else if (chapterName.equals("heading5")) {
            arr = chapter5;
//            chapterImage.setImageResource(R.drawable.img_card1);
            Glide.with(TopicActivity.this)
                    .load("https://firebasestorage.googleapis.com/v0/b/skyfun-english.appspot.com/o/img_card5.png?alt=media&token=c245846e-940d-455b-8393-01149006c56a")
                    .into(chapterImage);
            getSupportActionBar().setTitle("Heading 5");
        }
        else {
            arr = null;
        }

        // set adapter
        topicAdapter = new TopicAdapter(arr, chapterName, TopicActivity.this);
        gridView.setAdapter(topicAdapter);

        // set onItemClickListener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                //Toast.makeText(TopicActivity.this, arr[i], Toast.LENGTH_SHORT).show();
                openActivity(arr[i]);
            }
        });

    }

    private void openActivity(String s) {
        switch (s) {
            case "Hello":
                startActivity(new Intent(TopicActivity.this, Hello.class));
                break;
            // add more cases for other topics
        }
    }
}
