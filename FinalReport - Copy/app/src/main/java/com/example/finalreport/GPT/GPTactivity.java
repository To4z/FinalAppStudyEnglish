package com.example.finalreport.GPT;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalreport.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GPTactivity extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();
    private EditText etQuestion;
    private Button btnSubmit;
    private TextView txtResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gptactivity);

        etQuestion = findViewById(R.id.etQuestion);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtResponse = findViewById(R.id.txtResponse);

        btnSubmit.setOnClickListener(view -> {
            String question = etQuestion.getText().toString().trim();
            Toast.makeText(this, question, Toast.LENGTH_SHORT).show();
            if (!question.isEmpty()) {
                getResponse(question, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            JSONArray jsonArray = jsonObject.getJSONArray("choices");
                            String textResult = jsonArray.getJSONObject(0).getString("text");
                            runOnUiThread(() -> txtResponse.setText(textResult));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("error", "API failed", e);
                    }
                });
            }
        });
    }

    private void getResponse(String question, Callback callback) {
        final String apiKey = "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";
        final String url = "https://api.openai.com/v1/engines/text-davinci-003/completions";

        String requestBody = "{\n" +
                "  \"prompt\": \"" + question + "\",\n" +
                "  \"max_tokens\": 500,\n" +
                "  \"temperature\": 0\n" +
                "}";

        RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }
}