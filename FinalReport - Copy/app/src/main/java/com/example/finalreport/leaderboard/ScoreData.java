package com.example.finalreport.leaderboard;

import android.widget.Button;

public class ScoreData {

    String name,img;
    long score;


    public ScoreData() {
    }

    public ScoreData(String name, String img, long score) {
        this.name = name;
        this.img = img;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
