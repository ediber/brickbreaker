package com.example.brickbreaker04;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import java.util.ArrayList;

public class Ball implements Runnable {
    private double angle;
    private int speed;
    private int x, y;
    private int diameter;
    private int velocityX, velocityY;
    private Paint paint;
    private boolean running = true;
    private int sleepTime = 40; // Initial sleep time

    private Rect paddleRect;
    private ArrayList<Brick> bricks;
    private BallListener listener;

    public static final String TAG = "Ball";

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

        speed = 15;
        angle = Math.toRadians((Math.random() * 120) + 30); // Random angle between 30 and 150 degrees
        this.velocityX = (int) (speed * Math.cos(angle));
        this.velocityY = (int) (speed * Math.sin(angle));
    }

    @Override
    public void run() {
        while (running) {
            x += velocityX;
            y += velocityY;

            // Check for collision with the paddle
            Rect ballRect = new Rect(x, y, x + diameter, y + diameter);
            if (Rect.intersects(ballRect, paddleRect)) {
                if(! angleOnEdge(angle)){
                    double deltaAngle = Math.toRadians((Math.random() * 20) - 10); // -10 to +10 degrees
                    angle += deltaAngle;
                    velocityX = (int) (speed * Math.cos(angle));
                }
                velocityY = -velocityY; // Reverse the Y direction
            }

            // Check for collision with the walls
            if (x <= 0 || x + diameter >= listener.getWidth()) {
                if(! angleOnEdge(angle)){
                    double deltaAngle = Math.toRadians((Math.random() * 40) - 20); // -20 to +20 degrees
                    angle += deltaAngle;
                }
                velocityX = -velocityX; // Reverse the horizontal direction upon hitting the wall
            }

            if (y <= 0) {
                if(! angleOnEdge(angle)){
                    double deltaAngle = Math.toRadians((Math.random() * 40) - 20); // -20 to +20 degrees
                    angle += deltaAngle;
                    velocityX = (int) (speed * Math.cos(angle));
                }
                velocityY = -velocityY;
            } else if (y + diameter >= listener.getHeight()) {  // Use greater than or equal to
                listener.onGameOver(); // Notify GameView to handle game over
                running = false; // Stop the game loop
                Log.d(TAG, "Game loop stopped");
                break; // Stop the loop
            }

            // Check for collisions with bricks
            for (Brick brick : bricks) {
                if (Rect.intersects(ballRect, brick.getRect())) {
                    listener.removeBrick(brick);  // Handle brick removal or disabling

                    if(! angleOnEdge(angle)){
                        double deltaAngle = Math.toRadians((Math.random() * 40) - 20); // -20 to +20 degrees
                        angle += deltaAngle;
                        velocityX = (int) (speed * Math.cos(angle));
                    }
                    velocityY = -velocityY; // Reverse the Y direction

                    if(bricks.isEmpty()){ // Check if there are more bricks
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

    private boolean angleOnEdge(double angle) {
        // Normalize the angle to be within [0, 360) degrees
        angle = angle % 360;
        if (angle < 0) {
            angle += 360;
        }

        // Check if the angle is within 10 degrees of 90, 180, 270, or 360 degrees
        return Math.abs(angle - 90) < 10 || Math.abs(angle - 180) < 10 ||
                Math.abs(angle - 270) < 10 || Math.abs(angle - 360) < 10 ||
                Math.abs(angle - 0) < 10;
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
