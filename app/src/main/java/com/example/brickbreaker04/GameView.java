package com.example.brickbreaker04;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {
    public interface ScoreListener {
        void onScoreChanged(int score);
    }
    private Paint paint;
    private Rect paddle;
    private int paddleWidth = 300;
    private int paddleHeight = 50;
    private int paddleSpeed = 20;
    private int screenWidth, screenHeight;
    private boolean moveLeft, moveRight;
    private Handler handler = new Handler();

    private ArrayList<Brick> bricks = new ArrayList<>();
    private int brickWidth = 180;
    private int brickHeight = 50;
    private int brickPadding = 10;
    private int numBricksPerRow = 5;
    private int numBrickRows = 4;
    private ArrayList<Paint> brickPaints = new ArrayList<>();
    private Random random = new Random();
    private Ball ball;
    private Thread ballThread;
    private int score = 0;
    private ScoreListener scoreListener;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            update();
            handler.postDelayed(this, 30);
        }
    };

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Ensuring we get a layout pass before starting the game
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Ensure we only call this once by removing the listener immediately
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                screenWidth = getWidth();
                screenHeight = getHeight();

                init(); // Initialize paddle and bricks
                initBall(); // Initialize and start ball movement
            }
        });
    }

    public void setScoreListener(ScoreListener listener) {
        this.scoreListener = listener;
    }

    private void init() {
        paint = new Paint();

        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        int paddleX = screenWidth / 2 - paddleWidth / 2;
        int paddleY = screenHeight - paddleHeight - 200; // 20 pixels from the bottom
        paddle = new Rect(paddleX, paddleY, paddleX + paddleWidth, paddleY + paddleHeight);

        paint.setColor(Color.BLUE); // Set paddle color to blue
        initBricks(); // Initialize the bricks
        initBall();
    }


    private void initBricks() {
        int brickTop = 50; // Start 50 pixels down from the top of the screen
        for (int row = 0; row < numBrickRows; row++) {
            int brickLeft = brickPadding;
            for (int col = 0; col < numBricksPerRow; col++) {
                Brick brick = new Brick(brickLeft, brickTop, brickWidth, brickHeight);
                bricks.add(brick);

                brickLeft += brickWidth + brickPadding; // Move to the right
            }
            brickTop += brickHeight + brickPadding; // Move to the next row
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the paddle
        paint.setColor(Color.BLUE); // Ensure the paddle color is set to blue
        canvas.drawRect(paddle, paint);

        // Draw each brick with its assigned color
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            canvas.drawRect(brick.getRect(), brick.getPaint());
        }

        // Draw the ball
        if (ball != null) {
            ball.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float touchX = event.getX();
                if (touchX < paddle.left) {
                    moveLeft = true;
                    moveRight = false;
                } else if (touchX > paddle.right) {
                    moveRight = true;
                    moveLeft = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                moveLeft = false;
                moveRight = false;
                break;
        }
        return true;
    }

    private void update() {
        if (moveLeft && paddle.left > 0) {
            paddle.left -= paddleSpeed;
            paddle.right -= paddleSpeed;
        }
        if (moveRight && paddle.right < screenWidth) {
            paddle.left += paddleSpeed;
            paddle.right += paddleSpeed;
        }
        invalidate(); // Redraw the canvas
    }

    public void startGame() {
        handler.postDelayed(runnable, 30);
    }

    private void initBall() {
        int startX = screenWidth / 2;
        int startY = screenHeight / 2;
        int diameter = 50;
        ball = new Ball(getContext(), this, startX, startY, diameter);

        // Delay starting the ball's movement until the view is fully ready
        handler.postDelayed(this::startBallMovement, 300); // Delay for 100 milliseconds
    }

    private void startBallMovement() {
        if (getHeight() > 0 && (ballThread == null || !ballThread.isAlive())) {
            ballThread = new Thread(ball);
            ballThread.start();
        } else {
            handler.postDelayed(this::startBallMovement, 300); // Retry until the height is greater than 0
        }
    }

    public void pause() {
        if (ball != null) {
            ball.stop(); // Stop the ball's thread
            try {
                ballThread.join(); // Ensure the thread finishes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void resume() {
        if (ball != null && !ballThread.isAlive()) {
            // Only restart the thread if it isn't already running
            ballThread = new Thread(ball);
            ballThread.start();
        }
    }


    public void gameOver() {
        if (ball != null) {
            ball.stop(); // Stop the ball's thread
        }
        post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), "Game Over: Ball hit bottom", Toast.LENGTH_LONG).show();
            }
        });
    }

    public Rect getPaddleRect() {
        return paddle; // make sure 'paddle' is up to date
    }

    public ArrayList<Brick> getBricks() {
        return new ArrayList<>(bricks);  // Return a copy to avoid concurrent modification issues
    }

    public void removeBrick(Brick brick) {
        bricks.remove(brick);
        score += brick.getScore();
        if (scoreListener != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    scoreListener.onScoreChanged(score);
                }
            });
        }
    }

    public void advanceLevel() { // TODO test it
        initBricks(); // Reinitialize bricks
        invalidate(); // Redraw the view to reflect changes
        ball.reduceSleepTime();
    }

}
