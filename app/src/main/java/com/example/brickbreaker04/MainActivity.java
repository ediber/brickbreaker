package com.example.brickbreaker04;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int GAME_DURATION_SECONDS = 20 * 1000; // 20 seconds in milliseconds

    private GameView gameView;
    private TextView scoreTextView;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private long remainingTime = GAME_DURATION_SECONDS; // Initialize remainingTime

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        gameView = findViewById(R.id.gameView);
        scoreTextView = findViewById(R.id.scoreTextView);
        timerTextView = findViewById(R.id.timerTextView);

        // Set up the score listener
        gameView.setScoreListener(new GameView.ScoreListener() {
            @Override
            public void onScoreChanged(int score) {
                scoreTextView.setText("Score: " + score);
            }
        });

        // Start the game and the timer
        gameView.startGame();
        startTimer(remainingTime);
    }

    private void startTimer(long millisInFuture) {
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                long minutes = (remainingTime / 1000) / 60;
                long seconds = (remainingTime / 1000) % 60;
                timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
            }

            public void onFinish() {
                timerTextView.setText("00:00");
                gameView.gameOver();
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
            startTimer(remainingTime);
        } else {
            timerTextView.setText("00:00");
            gameView.gameOver();
        }
    }
}
