package com.example.finalreport.chapter.one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.finalreport.GPT.GptProvider;
import com.example.finalreport.R;
import com.example.finalreport.chaptertopics.StringUrl;
import com.example.finalreport.chaptertopics.Topic;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Hello extends AppCompatActivity {


    private TextView showTextTopic;

    private EditText findVideo;

    private ImageView imgFind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        Topic topic = getIntent().getParcelableExtra("topic");
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        showTextTopic = findViewById(R.id.showTextTopic);
        showTextTopic.setText(topic.getTopicName());
        findVideo = findViewById(R.id.findYoutube);

        imgFind = findViewById(R.id.find);
        imgFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(findVideo.getText().toString().trim(),topic.getTopicName());
            }
        });


        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                StringUrl stringurl = new StringUrl();
                String videoId = stringurl.strinUrlYoutube(position - 5);
                youTubePlayer.loadVideo(videoId, 0);
            }
        });

        // Sử dụng GPT provider
        GptProvider gptProvider = GptProvider.getInstance(getApplicationContext());
        gptProvider.setPrompt("Từ vựng:"+ topic.getTopicName() + "Hãy liệt kê từ đông nghĩ, trái nghĩa, cách phát âm, phiên âm");
        gptProvider.getResponse(gptProvider.getPrompt(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                runOnUiThread(() -> {
                    TextView textView = findViewById(R.id.showText);
                    GptProvider.handleResponse(responseBody, textView);
                });
            }
        });
    }


        private void showDialog(String find,String topicName) {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog);

            TextView title = dialog.findViewById(R.id.dialog_title);
            TextView content = dialog.findViewById(R.id.dialog_content);
            Button button = dialog.findViewById(R.id.dialog_button);
            title.setText(find);

            GptProvider gptProvider = GptProvider.getInstance(getApplicationContext());
            gptProvider.setPrompt(find + "of" + topicName);
            gptProvider.getResponse(gptProvider.getPrompt(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    runOnUiThread(() -> {
                        GptProvider.handleResponse(responseBody, content);
                    });
                }
            });
                //content.setText(find + topicName);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

}