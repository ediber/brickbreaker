package com.example.brickbreaker04;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
    private Paint paint;
    private Rect paddle;
    private int paddleWidth = 300;
    private int paddleHeight = 50;
    private int paddleSpeed = 20;
    private int screenWidth, screenHeight;
    private boolean moveLeft, moveRight;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            update();
            handler.postDelayed(this, 30);
        }
    };

    public GameView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);

        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        int paddleX = screenWidth / 2 - paddleWidth / 2;
        int paddleY = screenHeight - paddleHeight - 20; // 20 pixels from the bottom
        paddle = new Rect(paddleX, paddleY, paddleX + paddleWidth, paddleY + paddleHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(paddle, paint); // Draw the paddle
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
}
