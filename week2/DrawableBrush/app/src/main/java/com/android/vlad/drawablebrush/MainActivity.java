package com.android.vlad.drawablebrush;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Board board = (Board) findViewById(R.id.board);
        final int BRUSH_WIDTH = 256;
        final int BRUSH_HEIGHT = 256;

        final GradientDrawable active = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{Color.WHITE, Color.GREEN});
        active.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        active.setGradientCenter(0.5f, 0.5f);
        active.setGradientRadius((BRUSH_WIDTH + BRUSH_HEIGHT) / 2);

        final GradientDrawable inactive = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{Color.WHITE, Color.RED});
        inactive.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        inactive.setGradientCenter(0.5f, 0.5f);
        inactive.setGradientRadius((BRUSH_WIDTH + BRUSH_HEIGHT) / 2);

        final ImageView tick = (ImageView) findViewById(R.id.tick);
        final ImageView pencil = (ImageView) findViewById(R.id.pencil);
        final ImageView rocket = (ImageView) findViewById(R.id.rocket);

        Bitmap brushTick = BitmapFactory.decodeResource(getResources(), R.drawable.tick);
        final Bitmap brushTickScaled = Bitmap.createScaledBitmap(brushTick, BRUSH_WIDTH, BRUSH_HEIGHT, false);
        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                board.setBrush(brushTickScaled);
                tick.setBackground(active);
                pencil.setBackground(inactive);
                rocket.setBackground(inactive);
            }
        });

        Bitmap brushPencil = BitmapFactory.decodeResource(getResources(), R.drawable.pencil);
        final Bitmap brushPencilScaled = Bitmap.createScaledBitmap(brushPencil, BRUSH_WIDTH, BRUSH_HEIGHT, false);
        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                board.setBrush(brushPencilScaled);
                tick.setBackground(inactive);
                pencil.setBackground(active);
                rocket.setBackground(inactive);
            }
        });

        Bitmap brushRocket = BitmapFactory.decodeResource(getResources(), R.drawable.rocket);
        final Bitmap brushRocketScaled = Bitmap.createScaledBitmap(brushRocket, BRUSH_WIDTH, BRUSH_HEIGHT, false);
        rocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                board.setBrush(brushRocketScaled);
                tick.setBackground(inactive);
                pencil.setBackground(inactive);
                rocket.setBackground(active);
            }
        });

        // Setting a default brush
        board.setBrush(brushTickScaled);
        tick.setBackground(active);
        pencil.setBackground(inactive);
        rocket.setBackground(inactive);
    }
}
