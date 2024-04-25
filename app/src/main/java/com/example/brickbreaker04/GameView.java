package com.example.brickbreaker04;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {
    private Paint paint;
    private Rect paddle;
    private int paddleWidth = 300;
    private int paddleHeight = 50;
    private int paddleSpeed = 20;
    private int screenWidth, screenHeight;
    private boolean moveLeft, moveRight;
    private Handler handler = new Handler();

    private ArrayList<Rect> bricks = new ArrayList<>();
    private int brickWidth = 180;
    private int brickHeight = 50;
    private int brickPadding = 10;
    private int numBricksPerRow = 5;
    private int numBrickRows = 4;
    private ArrayList<Paint> brickPaints = new ArrayList<>();
    private Random random = new Random();
    private Ball ball;
    private Thread ballThread;


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            update();
            handler.postDelayed(this, 30);
        }
    };

    public GameView(Context context) {
        super(context);

        // Ensuring we get a layout pass before starting the game
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Ensure we only call this once by removing the listener immediately
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                screenWidth = getWidth();
                screenHeight = getHeight();

                init();
            }
        });
    }

    private void init() {
        paint = new Paint();

        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        int paddleX = screenWidth / 2 - paddleWidth / 2;
        int paddleY = screenHeight - paddleHeight - 20; // 20 pixels from the bottom
        paddle = new Rect(paddleX, paddleY, paddleX + paddleWidth, paddleY + paddleHeight);

        paint.setColor(Color.BLUE); // Set paddle color to blue
        initBricks(); // Initialize the bricks
        initBall();
    }


    private void initBricks() {
        int[] colors = {
                Color.RED, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.CYAN // Random colors except blue
        };

        int brickTop = 50; // Start 50 pixels down from the top of the screen
        for (int row = 0; row < numBrickRows; row++) {
            int brickLeft = brickPadding;
            for (int col = 0; col < numBricksPerRow; col++) {
                Rect brick = new Rect(brickLeft, brickTop, brickLeft + brickWidth, brickTop + brickHeight);
                bricks.add(brick);

                Paint brickPaint = new Paint();
                brickPaint.setColor(colors[random.nextInt(colors.length)]);
                brickPaints.add(brickPaint);

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
            canvas.drawRect(bricks.get(i), brickPaints.get(i));
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
        ballThread = new Thread(ball);
        ballThread.start();
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
}
