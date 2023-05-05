package com.example.finalreport.chaptertopics;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Topic implements Parcelable {
    private String topicName;
    private int image;
    private int soundPath;


    public Topic(String topicName, int image, int soundPath) {
        this.topicName = topicName;
        this.image = image;
        this.soundPath = soundPath;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(int soundPath) {
        this.soundPath = soundPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    protected Topic(Parcel in) {
        topicName = in.readString();
        image = in.readInt();
        soundPath = in.readInt();
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(topicName);
        dest.writeInt(image);
        dest.writeInt(soundPath);
    }
}

