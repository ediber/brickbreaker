package com.example.brickbreaker04;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder> {

    private List<UserScore> scores;

    public ScoresAdapter(List<UserScore> scores) {
        this.scores = scores;
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
        TextView usernameTextView;
        TextView nameTextView;
        TextView scoreTextView;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            nameTextView = itemView.findViewById(R.id.phoneTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
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
