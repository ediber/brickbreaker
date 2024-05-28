package com.example.brickbreaker04.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.brickbreaker04.R;

public class AddResultActivity extends BaseActivity {

    private TextView scoreTextView;
    private EditText nameEditText;
    private Button saveButton;
    private Button viewScoresButton;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_result);

        initializeViews();
        setListeners();

        // Retrieve the score from the intent
        score = getIntent().getIntExtra("score", 0);
        scoreTextView.setText("Score: " + score);


    }

    @Override
    protected void initializeViews() {
        scoreTextView = findViewById(R.id.scoreTextView);
        nameEditText = findViewById(R.id.nameEditText);
        saveButton = findViewById(R.id.saveButton);
        viewScoresButton = findViewById(R.id.viewScoresButton);
    }

    @Override
    protected void setListeners() {

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScore();
            }
        });

        viewScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewScores();
            }
        });
    }

    private void saveScore() {
        String name = nameEditText.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the score and name
        // You can implement the actual save logic here (e.g., save to a database or shared preferences)

        Toast.makeText(this, "Score saved!", Toast.LENGTH_SHORT).show();
    }

    private void viewScores() {
        // Implement the logic to view all scores
        // This could involve starting a new activity that displays a list of scores

        Intent intent = new Intent(this, ViewScoresActivity.class);
        startActivity(intent);
    }
}
