package com.example.brickbreaker04;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Ball implements Runnable {
    private int x, y;
    private int diameter;
    private int velocityX, velocityY;
    private Paint paint;
    private boolean running = true;
    private final GameView gameView;

    public Ball(Context context, GameView gameView, int startX, int startY, int diameter) {
        this.gameView = gameView;
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

            // Check for boundary collisions
            if (x <= 0 || x + diameter >= gameView.getWidth()) {
                velocityX *= -1; // Reverse the horizontal direction upon hitting the wall
            }
            if (y <= 0 || y + diameter >= gameView.getHeight()) {
                velocityY *= -1; // Reverse the vertical direction upon hitting the wall or floor
            }

            gameView.postInvalidate(); // Request to redraw the gameView

            try {
                Thread.sleep(30); // Delay to temper the game speed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x + diameter / 2, y + diameter / 2, diameter / 2, paint);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDiameter() {
        return diameter;
    }

    public void stop() {
        running = false;
    }
}
