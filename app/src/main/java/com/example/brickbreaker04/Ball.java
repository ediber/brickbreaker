package com.example.brickbreaker04;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

public class Ball implements Runnable {
    private int x, y;
    private int diameter;
    private int velocityX, velocityY;
    private Paint paint;
    private boolean running = true;
    private int sleepTime = 30; // Initial sleep time

    private Rect paddleRect;
    private ArrayList<Brick> bricks;
    private BallListener listener;

    public interface BallListener {
        void onGameOver();
        void removeBrick(Brick brick);
        void advanceLevel();
        void postInvalidate();
        int getWidth();
        int getHeight();
    }

    public Ball(Context context, Rect paddleRect, ArrayList<Brick> bricks, BallListener listener, int startX, int startY, int diameter) {
        this.paddleRect = paddleRect;
        this.bricks = bricks;
        this.listener = listener;
        this.x = startX;
        this.y = startY;
        this.diameter = diameter;

        this.paint = new Paint();
        this.paint.setColor(Color.BLACK); // Set the color of the ball

        int initialSpeed = 15;
        double angle = Math.toRadians((Math.random() * 120) + 30); // Random angle between 30 and 150 degrees
        this.velocityX = (int) (initialSpeed * Math.cos(angle));
        this.velocityY = (int) (initialSpeed * Math.sin(angle));
    }

    @Override
    public void run() {
        while (running) {
            x += velocityX;
            y += velocityY;

            // Check for collision with the paddle
            Rect ballRect = new Rect(x, y, x + diameter, y + diameter);
            if (Rect.intersects(ballRect, paddleRect)) {
                velocityY = -velocityY; // Reverse the Y direction
            }

            if (x <= 0 || x + diameter >= listener.getWidth()) {
                velocityX *= -1; // Reverse the horizontal direction upon hitting the wall
            }

            if (y <= 0) {
                velocityY *= -1; // Reverse vertical direction upon hitting the top wall
            } else if (y + diameter > listener.getHeight()) {  // Use greater than strictly
                listener.onGameOver(); // Notify GameView to handle game over
                break; // Stop the loop
            }

            // Check for collisions with bricks
            for (Brick brick : bricks) {
                if (Rect.intersects(ballRect, brick.getRect())) {
                    listener.removeBrick(brick);  // Handle brick removal or disabling
                    velocityY = -velocityY;  // Reverse the Y direction
                    if(bricks.isEmpty()){ // check if there are more bricks
                        listener.advanceLevel();
                    }
                    break;  // Break out of the loop to avoid multiple collisions at once
                }
            }

            listener.postInvalidate(); // Request to redraw the gameView

            try {
                Thread.sleep(sleepTime); // Control the update rate
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x + diameter / 2, y + diameter / 2, diameter / 2, paint);
    }

    public void stop() {
        running = false;
    }

    public void reduceSleepTime() {
        sleepTime = Math.max(1, sleepTime / 2); // Ensure sleepTime doesn't go below 1 millisecond
    }
}
