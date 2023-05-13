package com.example.finalreport.pdf;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalreport.R;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class readFilePdf extends AppCompatActivity {

    private final int CHOOSE_PDF_FROM_DEVICE = 1001;
    private final OkHttpClient client = new OkHttpClient();
    private ImageView slt_file;

    private TextView txt;

    private EditText findAbout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_file_pdf);

        slt_file = findViewById(R.id.findpdf);
        txt = findViewById(R.id.showTextpdf);
        findAbout = findViewById(R.id.findAbout);

        slt_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(findAbout.getText().toString().trim().isEmpty()){
                    Toast.makeText(readFilePdf.this, "Please fill the input text", Toast.LENGTH_SHORT).show();
                } else {
                    callChoosePdfFile();
                }

            }
        });



    }

    private void callChoosePdfFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent,CHOOSE_PDF_FROM_DEVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_PDF_FROM_DEVICE && resultCode == RESULT_OK){
            if(data != null){
                //Log.d(tag, " onActivityResult: " + data.getData());
                ExtractTextPdfFile(data.getData());
            }
        }
    }

    InputStream inputStream;

    private void ExtractTextPdfFile(Uri uri){
        try {
            inputStream = readFilePdf.this.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Xử lý đọc file PDF
                String filecontent = "";
                StringBuilder builder = new StringBuilder();
                PdfReader reader = null;

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        reader = new PdfReader(inputStream);

                        int pages = reader.getNumberOfPages();

                        for (int i = 1; i <= pages; i++){
                            filecontent = PdfTextExtractor.getTextFromPage(reader,i);
                        }

                        builder.append(filecontent);
                    }
                    reader.close();

                    // Cập nhật giao diện TextView
                    final String text = builder.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            txt.setText(text);
                            String question = (findAbout.getText().toString().trim() + " for "+ text.replaceAll("\\r?\\n", " "));
                            if (!question.isEmpty()) {
                                getResponse(question, new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String body = response.body().string();
                                        try {
                                            JSONObject jsonObject = new JSONObject(body);
                                            JSONArray jsonArray = jsonObject.getJSONArray("choices");
                                            String textResult = jsonArray.getJSONObject(0).getString("text");
                                            runOnUiThread(() -> txt.setText(text + "\n" + textResult));
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
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


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