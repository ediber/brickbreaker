package com.example.brickbreaker04;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class GameView extends View {
    // Game objects and properties
    private Paint paint;

    public GameView(Context context) {
        super(context);
        init(); // Initialize game components
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw game components
        canvas.drawRect(100, 100, 200, 200, paint); // Example brick
    }
}
