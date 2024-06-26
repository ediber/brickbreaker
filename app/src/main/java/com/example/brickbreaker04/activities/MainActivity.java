package com.example.brickbreaker04.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brickbreaker04.GameView;
import com.example.brickbreaker04.MusicService;
import com.example.brickbreaker04.R;

public class MainActivity extends BaseActivity {

    private static final int GAME_DURATION_SECONDS = 30 * 1000; // 20 seconds in milliseconds

    private GameView gameView;
    private TextView scoreTextView;
    private TextView timerTextView;
    private TextView levelTextView;

    private CountDownTimer countDownTimer;
    private long remainingTime = GAME_DURATION_SECONDS; // Initialize remainingTime

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setListeners();

        // Start the game and the timer
        gameView.startGame();
        startTimer(remainingTime);

        startService(new Intent(this, MusicService.class));
    }

    @Override
    protected void initializeViews() {
        // Initialize views
        gameView = findViewById(R.id.gameView);
        scoreTextView = findViewById(R.id.scoreTextView);
        timerTextView = findViewById(R.id.timerTextView);
        levelTextView = findViewById(R.id.levelTextView);
        levelTextView.setText("Level: " + 1); // Set initial level text
    }

    @Override
    protected void setListeners() {
        // Set up the score listener
        gameView.setListener(new GameView.Listener() {
            @Override
            public void onScoreChanged(int score) {
                scoreTextView.setText("Score: " + score);
            }

            @Override
            public void onLevelChanged(int level) {
                levelTextView.setText("Level: " + level);
                Toast.makeText(MainActivity.this, "Level Up! X2 on points", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGameOver() {
                stopService(new Intent(MainActivity.this, MusicService.class));
            }
        });
    }

    private void startTimer(long millisInFuture) {
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            public void onTick(long millisUntilFinished) {
          //      remainingTime = millisUntilFinished;
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
            }

            public void onFinish() {
                timerTextView.setText("00:00");
                Toast.makeText(MainActivity.this, "Time's up! speed increases", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "Time's up! speed increases");
                gameView.speedUp();
                startTimer(remainingTime);
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (remainingTime > 0) {
      //      startTimer(remainingTime);
        } else {
            timerTextView.setText("00:00");
       //     gameView.gameOver();
        }
    }
}
