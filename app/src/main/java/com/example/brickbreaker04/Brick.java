package com.example.brickbreaker04;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class Brick {
    private Rect rect;
    private Paint paint;
    private int score;

    int[] colors = {
            Color.RED, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.CYAN // Random colors except blue
    };

    public Brick(int brickLeft, int brickTop, int brickWidth, int brickHeight) {
        this.rect = new Rect(brickLeft, brickTop, brickLeft + brickWidth, brickTop + brickHeight);;
        Random random = new Random();
        paint = new Paint();
        int index = random.nextInt(colors.length);
        this.paint.setColor(colors[index]);
        this.score = (index+1) * 50;
    }

    ///
    public Rect getRect() {
        return rect;
    }

    public Paint getPaint() {
        return paint;
    }

    public int getScore() {
        return score;
    }
}
