package com.example.finalreport.GPT;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

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

public class GptProvider {
    private static GptProvider instance;
    private final OkHttpClient client;
    private final String apiKey;
    private final String url;
    private String prompt;
    private int maxTokens;
    private float temperature;

    private GptProvider(Context context) {
        apiKey = "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";
        url = "https://api.openai.com/v1/engines/text-davinci-003/completions";
        client = new OkHttpClient();
        prompt = "";
        maxTokens = 500;
        temperature = 0;
    }

    public static GptProvider getInstance(Context context) {
        if (instance == null) {
            instance = new GptProvider(context);
        }
        return instance;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void getResponse(String prompt, Callback callback) {
        if (!prompt.isEmpty()) {
            String requestBody = "{\n" +
                    "  \"prompt\": \"" + prompt + "\",\n" +
                    "  \"max_tokens\": " + maxTokens + ",\n" +
                    "  \"temperature\": " + temperature + "\n" +
                    "}";

            RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Request failed", e);
                    callback.onFailure(call, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    callback.onResponse(call, response);
                }
            });
        } else {
            //Toast.makeText(context, "Prompt cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    public static void handleResponse(String responseBody, TextView txtResponse) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray jsonArray = jsonObject.getJSONArray("choices");
            String textResult = jsonArray.getJSONObject(0).getString("text");
            txtResponse.setText(textResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPrompt() {
        return prompt;
    }
}
