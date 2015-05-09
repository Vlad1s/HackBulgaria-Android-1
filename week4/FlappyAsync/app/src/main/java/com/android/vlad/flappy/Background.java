package com.android.vlad.flappy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Background extends GameObject {
    private final int MOVE_STEP = 1;

    private int backgroundX;
    private Bitmap mBitmapBackground;
    private Context mContext;

    // TODO: Redraw the background instead of changing its position - should look more fluid
    public Background(Context context) {
        this.mContext = context;

        mBitmapBackground = Bitmap.createBitmap(DrawingView.getScreenSize().x * 2, DrawingView.getScreenSize().y, Bitmap.Config.ARGB_8888);
        Canvas canvasBackground = new Canvas(mBitmapBackground);

        Bitmap unscaledBitmapClouds = BitmapFactory.decodeResource(context.getResources(), R.drawable.clouds);
        int cloudsWidth = DrawingView.getScreenSize().x * 2;
        int cloudsHeight = unscaledBitmapClouds.getHeight() * cloudsWidth / unscaledBitmapClouds.getWidth();
        Bitmap bitmapClouds = Bitmap.createScaledBitmap(unscaledBitmapClouds, cloudsWidth, cloudsHeight, false);

        // Filling the background bitmap with clouds
        int index = 0;
        int currentHeight = 0;
        while (currentHeight < mBitmapBackground.getHeight()) {
            canvasBackground.drawBitmap(bitmapClouds, 0, cloudsHeight * index, null);
            index++;
            currentHeight += cloudsHeight;
        }

        init();
    }

    @Override
    public void init() {
        backgroundX = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        backgroundX -= MOVE_STEP;

        if (Math.abs(backgroundX) >= DrawingView.getScreenSize().x) {
            init();
        }

        canvas.drawBitmap(mBitmapBackground, backgroundX, 0, null);
    }
}
