package com.example.brickbreaker04.activities;

import android.os.Bundle;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.brickbreaker04.R;
import com.example.brickbreaker04.ScoresAdapter;
import com.example.brickbreaker04.UserScore;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewScoresActivity extends BaseActivity {

    private RecyclerView scoresRecyclerView;
    private ScoresAdapter scoresAdapter;
    private List<UserScore> scoreList;
    private FirebaseFirestore db;
    private Button buttonSortHighToLow;
    private Button buttonSortLowToHigh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_scores);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setListeners();

        db = FirebaseFirestore.getInstance();
        fetchScoresFromFirestore();
    }

    @Override
    protected void initializeViews() {
        scoresRecyclerView = findViewById(R.id.scoresRecyclerView);
        scoresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoreList = new ArrayList<>();
        scoresAdapter = new ScoresAdapter(scoreList, currentUser.getId());
        scoresRecyclerView.setAdapter(scoresAdapter);

        buttonSortHighToLow = findViewById(R.id.button_sort_high_to_low);
        buttonSortLowToHigh = findViewById(R.id.button_sort_low_to_high);
    }

    @Override
    protected void setListeners() {


        buttonSortHighToLow.setOnClickListener(v -> {
            Collections.sort(scoreList, Collections.reverseOrder());
            scoresAdapter.setScoreList(scoreList);
            scoresAdapter.notifyDataSetChanged();
        });

        buttonSortLowToHigh.setOnClickListener(v -> {
            Collections.sort(scoreList);
            scoresAdapter.setScoreList(scoreList);
            scoresAdapter.notifyDataSetChanged();
        });
    }

    private void fetchScoresFromFirestore() {
        db.collection("scores")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        scoreList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userId = document.getString("userId");
                            int score = document.getLong("score").intValue();
                            fetchUserDetails(userId, score);
                        }
                    } else {
                        // Handle the error
                    }
                });
    }

    private void fetchUserDetails(String userId, int score) {
        db.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = document.getString("username");
                            String id = document.getString("id");
                            String name = document.getString("name");
                            scoreList.add(new UserScore(id, username, name, score));
                        }
                        // Notify the adapter that the data has changed AND REFRESH THE UI
                        scoresAdapter.setScoreList(scoreList);
                        scoresAdapter.notifyDataSetChanged();
                    } else {
                        // Handle the error or no result found
                    }
                });
    }
}
