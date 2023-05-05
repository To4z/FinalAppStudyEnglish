package com.example.finalreport.chaptertopics;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.provider.ContactsContract;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.finalreport.R;
import com.example.finalreport.chapter.one.Hello;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Objects;


public class TopicAdapter extends BaseAdapter {
    private String[] topics;
    private Context context;

    private String chapterName;

    FirebaseUser user;

    DatabaseReference reference;





    public TopicAdapter(String[] topics, String chapterName, Context context) {
        this.topics = topics;
        this.chapterName = chapterName;
        this.context = context;
    }

    @Override
    public int getCount() {
        return topics.length;
    }

    @Override
    public Object getItem(int position) {
        return topics[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_topic_item_layout, parent, false);
        }


        TextView topicTextView = convertView.findViewById(R.id.topic_text);
        ImageView topicImageView = convertView.findViewById(R.id.image_view);

        int[][] Array = ImgAndSound(this.topics); // initialize Array here


        final Topic topic = new Topic(topics[position], Array[position][0], Array[position][1]);

        topicTextView.setText(topic.getTopicName());

        //topicImageView.setImageResource(topic.getImage());
        StringUrl sringUrl = new StringUrl();

        if(Objects.equals(chapterName, "heading2")){
            position += 5;
         }else if(Objects.equals(chapterName, "heading3")){
            position += 10;
        } else if(Objects.equals(chapterName, "heading4")){
            position += 15;
        } else if(Objects.equals(chapterName, "heading5")){
            position += 20;
        } else {
            position += 25;
        }

        final int finalPosition = position;

        View finalConvertView = convertView;

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        reference = FirebaseDatabase.getInstance("https://skyfun-english-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        reference.child("Score").child(user.getUid()).child("chapter").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //score += Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    String ws = "";
                    String f = finalPosition + "";
                    if (dataSnapshot.exists()) {
                        ws = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                        if (ws.contains(f)) {
                            finalConvertView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2BD566")));
                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        loadImageWithGlideAndStoreLocation(context,sringUrl.getUrl(position),topicImageView);


        topicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalConvertView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2BD566")));

                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                reference = FirebaseDatabase.getInstance("https://skyfun-english-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
                user = FirebaseAuth.getInstance().getCurrentUser();
                reference.child("Score").child(user.getUid()).child("chapter").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String ws = "";
                        String f = finalPosition + "";
                        if (dataSnapshot.exists()) {
                            ws = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                            if (!ws.contains(f)) {
                                ws += " " +f;
                            }
                        } else {
                            ws = f;
                        }
                        dataSnapshot.getRef().setValue(ws);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(context, Hello.class);
                intent.putExtra("topic", topic);
                intent.putExtra("position", finalPosition);
                context.startActivity(intent);
            }
        });

        Button topicButton = convertView.findViewById(R.id.play_button);
        topicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, topic.getSoundPath());
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                        mp.release();
                    }
                });
                mediaPlayer.start();
            }
        });

        return finalConvertView;
    }


    public int[][] ImgAndSound(String[] arr) {
        int[][] result = new int[arr.length][2]; // create a 2D array of size arr.length x 2

        for (int i = 0; i < arr.length; i++) {
            String chapterName = arr[i].replace(" ", "").toLowerCase(); // remove spaces and convert to lowercase
            int imageResId = context.getResources().getIdentifier(chapterName, "drawable", context.getPackageName());
            int soundResId = context.getResources().getIdentifier(chapterName, "raw", context.getPackageName());
            result[i][0] = imageResId;
            result[i][1] = soundResId;
        }
        return result;
    }

    public void loadImageWithGlideAndStoreLocation(Context context, String imageUrl, ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .into(imageView);
    }

    public static boolean findWord(String sentence, String find) {
        String[] words = sentence.split(" ");
        for (String word : words) {
            if (word.equalsIgnoreCase(find)) {
                return true;
            }
        }
        return false;
    }

}

