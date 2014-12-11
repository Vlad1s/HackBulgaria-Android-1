package com.android.vlad.drawablebrush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class Board extends ImageView implements View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {
    private Bitmap mBrush;
    private Bitmap mBoard;
    private Canvas mBoardCanvas;
    private Paint mPaint;

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
        ViewTreeObserver vto = this.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(this);

        mPaint = new Paint();
        mPaint.setAlpha(128);
    }

    public void setBrush(Bitmap brush) {
        this.mBrush = brush;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBoard, 0, 0, null);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:

                for (int i = 0; i < motionEvent.getPointerCount(); i++) {
                    mBoardCanvas.drawBitmap(mBrush, motionEvent.getX(i) - mBrush.getWidth() / 2, motionEvent.getY(i) - mBrush.getHeight() / 2, mPaint);
                    this.invalidate();
                }

                break;
        }

        return true;
    }

    @Override
    public void onGlobalLayout() {
        mBoard = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        mBoardCanvas = new Canvas(mBoard);
    }
}

