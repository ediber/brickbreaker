package com.example.brickbreaker04.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.brickbreaker04.R;
import java.util.List;

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder> {

    private final List<Score> scores;

    public ScoresAdapter(List<Score> scores) {
        this.scores = scores;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score score = scores.get(position);
        holder.usernameTextView.setText(score.getUsername());
        holder.phoneTextView.setText(score.getPhone());
        holder.scoreTextView.setText(String.valueOf(score.getScore()));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView phoneTextView;
        TextView scoreTextView;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
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
