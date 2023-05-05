package com.example.finalreport.ui.quiz;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.finalreport.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;



public class StartQuiz extends AppCompatActivity {

   private TextView questionTxt, indicator;
   private LinearLayout container;
   private Button nextBtn, shareBtn;

   private int score = 0;

   private int position = 0;

   private int count = 0;

   DatabaseReference reference;

   private List<QuestionData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        questionTxt = findViewById(R.id.question);
        indicator = findViewById(R.id.indicatior);
        container = findViewById(R.id.linearLayout2);

        nextBtn = findViewById(R.id.btn_next);
        shareBtn = findViewById(R.id.btn_share);



        list = new ArrayList<>();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        reference = FirebaseDatabase.getInstance("https://skyfun-english-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        //reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Question").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String question = snapshot.child("question").getValue().toString();
                    String option1 = snapshot.child("option1").getValue().toString();
                    String option2 = snapshot.child("option2").getValue().toString();
                    String option3 = snapshot.child("option3").getValue().toString();
                    String option4 = snapshot.child("option4").getValue().toString();
                    String correctAns = snapshot.child("answer").getValue().toString();

                    list.add(new QuestionData(option1,option2,option3,option4,question,correctAns));
                }
                if(list.size() > 0){
                    //indicator.setText(list.get(3).getAns());
                    loadQuestion(questionTxt,0,list.get(position).getQues());

                    for (int i = 0 ; i < 4; i++){
                        container.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                 checkAnswer((Button)v);
                            }
                        });
                    }

                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nextBtn.setEnabled(false);
                            nextBtn.setAlpha(.7f);
                            enable(true);
                            position++;

                            if(position == list.size()){
                                Intent intent = new Intent(StartQuiz.this, ScoreActivity.class);
                                intent.putExtra("score", score);
                                intent.putExtra("total", list.size());
                                startActivity(intent);
                                finish();
                                return;
                            }
                            count = 0;
                            loadQuestion(questionTxt, 0, list.get(position).getQues());
                        }
                    });

                    shareBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String body = "*"+list.get(position).getQues()+"*\n"
                                    + "(a)" + list.get(position).getOp1()+"*\n"
                                    + "(b)" + list.get(position).getOp2()+"*\n"
                                    + "(c)" + list.get(position).getOp3()+"*\n"
                                    + "(d)" + list.get(position).getOp4();
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("Text/Plain");
                            intent.putExtra(Intent.EXTRA_SUBJECT, "English Skyfun");
                            intent.putExtra(Intent.EXTRA_TEXT,body);
                            startActivity(Intent.createChooser(intent, "Share via"));
                        }
                    });
                }else {
                    Toast.makeText(StartQuiz.this, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StartQuiz.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAnswer(Button selectedOption) {
        enable(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        if(selectedOption.getText().toString().equals(list.get(position).getAns())){
            score++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4caf50")));
        } else {
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctOption = container.findViewWithTag(list.get(position).getAns());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4caf50")));
        }
    }
    private void enable(Boolean enable){
        for (int i =0 ; i < 4; i++){
            container.getChildAt(i).setEnabled(enable);
            if(enable){
                container.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
    }
    private void loadQuestion(View view, int value, String data){
        for (int i = 0; i < 4; i++){
            container.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#898989")));
        }
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        if(value == 0 && count < 4 ){
                            String option = "";
                            if(count == 0){
                                option = list.get(position).getOp1();
                            } else if (count == 1){
                                option = list.get(position).getOp2();
                            } else if (count == 2){
                                option = list.get(position).getOp3();
                            } else if(count == 3){
                                option = list.get(position).getOp4();
                            }
                            Log.d("option", option);
                            loadQuestion(container.getChildAt(count), 0 ,option);
                            count++;
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(value == 0){
                            try {
                                ((TextView)view).setText(data);
                                indicator.setText(position+"/"+ list.size());
                            } catch (ClassCastException e){
                                ((Button)view).setText(data);
                            }
                            view.setTag(data);
                            loadQuestion(view, 1,data);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

}