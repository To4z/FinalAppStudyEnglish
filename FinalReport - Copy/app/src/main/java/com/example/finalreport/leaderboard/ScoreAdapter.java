package com.example.finalreport.leaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalreport.R;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewAdapter> {

    List<ScoreData> list;
    Context context;

    public ScoreAdapter(List<ScoreData> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ScoreViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.score_list_item,parent,false);
        return new ScoreViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewAdapter holder, int position) {
        ScoreData currentItem = list.get(position);
        holder.name.setText(currentItem.getName());
        holder.score.setText(String.valueOf(currentItem.getScore()));
        holder.rank.setText(String.valueOf(list.size() - position));
        Glide.with(context).load(currentItem.getImg()).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ScoreViewAdapter extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, score, rank;
        public ScoreViewAdapter(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.score_user_img);
            name = itemView.findViewById(R.id.score_user_name);
            score = itemView.findViewById(R.id.score_user_result);
            rank = itemView.findViewById(R.id.score_user_rank);
        }
    }
}
