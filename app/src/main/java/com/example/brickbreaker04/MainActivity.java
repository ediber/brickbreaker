package com.example.brickbreaker04;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.gameView);
        scoreTextView = findViewById(R.id.scoreTextView);

        gameView.setScoreListener(new GameView.ScoreListener() {
            @Override
            public void onScoreChanged(int score) {
                scoreTextView.setText("Score: " + score);
            }
        });

        gameView.startGame();

/*        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }

    @Override
    protected void onPause() {
        super.onPause();
    //    gameView.pause();  // Pause the game which in turn pauses the ball's thread
    }


    @Override
    protected void onResume() {
        super.onResume();
     //   gameView.resume();  // Resume the game, restart the ball's movement if it was previously running
    }
}