package com.example.brickbreaker04.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.brickbreaker04.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddResultActivity extends BaseActivity {

    private TextView scoreTextView;
    private Button saveButton;
    private Button viewScoresButton;
    private int score;
    private FirebaseFirestore db;
    private View newGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_result);

        db = FirebaseFirestore.getInstance();

        initializeViews();
        setListeners();

        // Retrieve the score from the intent
        score = getIntent().getIntExtra("score", 0);
        scoreTextView.setText("Score: " + score);
    }

    @Override
    protected void initializeViews() {

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scoreTextView = findViewById(R.id.scoreTextView);
        saveButton = findViewById(R.id.saveButton);
        viewScoresButton = findViewById(R.id.viewScoresButton);
        newGameButton = findViewById(R.id.newGameButton);
    }

    @Override
    protected void setListeners() {
        saveButton.setOnClickListener(v -> saveScore(score));

        viewScoresButton.setOnClickListener(v -> viewScores());

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddResultActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveScore(int score) {
        if (currentUser != null) { // Check if the user is logged in
            String id = currentUser.getId(); // Get the user ID

            Map<String, Object> scoreData = new HashMap<>(); // Create a map to store the score data
            scoreData.put("userId", id); // Add the user ID to the map
            scoreData.put("score", score); // Add the score to the map

            db.collection("scores")
                    .add(scoreData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(AddResultActivity.this, "Score saved successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddResultActivity.this, "Error saving score: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewScores() {
        // Implement the logic to view all scores
        // This could involve starting a new activity that displays a list of scores

        Intent intent = new Intent(this, ViewScoresActivity.class);
        startActivity(intent);
    }
}
