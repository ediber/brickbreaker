package com.example.brickbreaker04;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder> {

    private final String id;
    private List<UserScore> scores;

    public ScoresAdapter(List<UserScore> scores, String id) {
        this.scores = scores;
        this.id = id;
    }
    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    // put the data in the view holder for the user to see
    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        UserScore score = scores.get(position);
        holder.usernameTextView.setText(score.getUsername());
        holder.nameTextView.setText(score.getName());
        holder.scoreTextView.setText(String.valueOf(score.getScore()));

        if (score.getId().equals(id)) {
            holder.root.setBackgroundColor(Color.argb(50,89,191,159));
        }
    }

    // Returns the number of items in the list
    @Override
    public int getItemCount() {
        return scores.size();
    }

    public void setScoreList(List<UserScore> scoreList) {
        this.scores = scoreList;
    }

    // ViewHolder class for the RecyclerView represent each item in the list
    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        View root;
        TextView usernameTextView;
        TextView nameTextView;
        TextView scoreTextView;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            nameTextView = itemView.findViewById(R.id.phoneTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
            root = itemView.findViewById(R.id.item_score_root);
        }
    }

    public static class Score {
        private final String username;
        private final String phone;
        private final int score;

        public Score(String username, String phone, int score) {
            this.username = username;
            this.phone = phone;
            this.score = score;
        }

        public String getUsername() {
            return username;
        }

        public String getPhone() {
            return phone;
        }

        public int getScore() {
            return score;
        }
    }
}
