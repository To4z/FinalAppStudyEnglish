package com.example.finalreport;

import com.example.finalreport.ui.quiz.QuestionData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataProvider {
    private static DataProvider instance;
    private DatabaseReference reference;

    private DataProvider() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        reference = FirebaseDatabase.getInstance("https://skyfun-english-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
    }

    public static synchronized DataProvider getInstance() {
        if (instance == null) {
            instance = new DataProvider();
        }
        return instance;
    }

}