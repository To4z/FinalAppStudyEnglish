package com.example.finalreport.ui.code;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.finalreport.MainActivity;
import com.example.finalreport.GPT.GptProvider;
import com.example.finalreport.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;




import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CodeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CodeFragment() {
        // Required empty public constructor
    }

    private Spinner fromSpinner, toSpinner;

    private TextInputEditText sourceEdt;

    private ImageView micIV;

    private MaterialButton stranlateBtn;

    private TextView translatedTV;

    String[] fromLanguages = {"From", "English", "Vietnamese"};
    String[] toLanguages = {"To", "English","Vietnamese"};


    private static final int REQUEST_PERMISSION_CODE = 1;

    int languageCode, fromLanguageCode, toLanguageCode = 0;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CodeFragment newInstance(String param1, String param2) {
        CodeFragment fragment = new CodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_code, container, false);

        fromSpinner = view.findViewById(R.id.idFromSpinner);
        toSpinner = view.findViewById(R.id.idToSpinner);
        sourceEdt = view.findViewById(R.id.idEdtSource);

        micIV = view.findViewById(R.id.idIVMic);
        stranlateBtn = view.findViewById(R.id.btnStranslate);
        translatedTV = view.findViewById(R.id.idTVTranslatedTV);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguageCode = getLanguageCode(fromLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter fromAdapter = new ArrayAdapter(view.getContext(), R.layout.spinner_item,fromLanguages);

        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSpinner.setAdapter(fromAdapter);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toLanguageCode = getLanguageCode(toLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter(view.getContext(), R.layout.spinner_item,toLanguages);

        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

        stranlateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translatedTV.setText("");
                if(sourceEdt.getText().toString().isEmpty()){
                    Toast.makeText(view.getContext(), "Please enter your text to", Toast.LENGTH_SHORT).show();
                } else if(fromLanguageCode == 0) {
                    Toast.makeText(view.getContext(), "Please select source language", Toast.LENGTH_SHORT).show();
                } else if(toLanguageCode == 0) {
                    Toast.makeText(view.getContext(), "Please select source language to make translation", Toast.LENGTH_SHORT).show();
                }else {
                    translateText(view.getContext(),fromLanguageCode,toLanguageCode,sourceEdt.getText().toString());
                }
            }
        });

        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to convert into Text");
                try{
                    startActivityForResult(i,REQUEST_PERMISSION_CODE);
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PERMISSION_CODE){
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                sourceEdt.setText(result.get(0));
            }
        }
    }

    private void translateText(Context context, int fromLanguageCode, int toLanguageCode, String source) {
        translatedTV.setText("Wait for result");
        GptProvider gptProvider = GptProvider.getInstance(context);
        if(fromLanguageCode == 1){
            gptProvider.setPrompt("Hãy dịch cho tôi : " + source);
        } else {
            gptProvider.setPrompt("Hãy dịch cho tôi : " + source + "sang tiếng anh");
        }

        gptProvider.getResponse(gptProvider.getPrompt(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GptProvider.handleResponse(responseBody, translatedTV);
                    }
                });

            }
        });



    }

    public int getLanguageCode(String l){
        int lcode = 0;
        switch (l){
            case "English":
                lcode = 1;
                break;
            case "Vietnamese":
                lcode = 2;
                break;
            default:
                lcode = 0;
        }
        return lcode;
    }
}