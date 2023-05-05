package com.example.finalreport.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.finalreport.R;
import com.example.finalreport.chaptertopics.TopicActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HomeFragment() {
        // Required empty public constructor
    }

    CardView heading1,heading2, heading3, heading4,heading5;
    ImageView image1,image2,image3,image4,image5;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        heading1 = view.findViewById(R.id.heading1);
        heading2 = view.findViewById(R.id.heading2);
        heading3 = view.findViewById(R.id.heading3);
        heading4 = view.findViewById(R.id.heading4);
        heading5 = view.findViewById(R.id.heading5);

        image1  = view.findViewById(R.id.image1);
        image2  = view.findViewById(R.id.image2);
        image3  = view.findViewById(R.id.image3);
        image4  = view.findViewById(R.id.image4);
        image5  = view.findViewById(R.id.image5);

        loadImage();

        heading1.setOnClickListener(this);
        heading2.setOnClickListener(this);
        heading3.setOnClickListener(this);
        heading4.setOnClickListener(this);
        heading5.setOnClickListener(this);

        return view;
    }

    private void loadImage() {

        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/skyfun-english.appspot.com/o/img_card1.png?alt=media&token=260d0d09-0fff-415b-afaf-cb83e63fe6df")
                .into(image1);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/skyfun-english.appspot.com/o/img_card2.png?alt=media&token=c6d8e533-e46d-46c1-88ba-f474bf625c1d")
                .into(image2);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/skyfun-english.appspot.com/o/img_card3.png?alt=media&token=2cb7f9a6-7aef-47e2-b308-05b7b48ec574")
                .into(image3);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/skyfun-english.appspot.com/o/img_card4.png?alt=media&token=74dc690c-c661-459f-ab0a-8ad10790308b")
                .into(image4);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/skyfun-english.appspot.com/o/img_card5.png?alt=media&token=c245846e-940d-455b-8393-01149006c56a")
                .into(image5);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), TopicActivity.class);

        switch (v.getId()){
            case R.id.heading1:
                intent.putExtra("chapterName", "heading1");
                startActivity(intent);
                break;
            case R.id.heading2:
                intent.putExtra("chapterName", "heading2");
                startActivity(intent);
                break;
            case R.id.heading3:
                intent.putExtra("chapterName", "heading3");
                startActivity(intent);
                break;
            case R.id.heading4:
                intent.putExtra("chapterName", "heading4");
                startActivity(intent);
                break;
            case R.id.heading5:
                intent.putExtra("chapterName", "heading5");
                startActivity(intent);
                break;
        }
    }
}