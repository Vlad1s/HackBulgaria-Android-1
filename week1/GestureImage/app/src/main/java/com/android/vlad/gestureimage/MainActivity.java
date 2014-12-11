package com.android.vlad.gestureimage;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
    private ImageView mImageView;
    private GestureDetectorCompat mDetector;

    private PointF mPrevFingerA = new PointF();
    private PointF mFingerA = new PointF();
    private PointF mPrevFingerB = new PointF();
    private PointF mFingerB = new PointF();
    private PointF mPrevCenter = new PointF();
    private PointF mCenter = new PointF();

    private boolean mIsGestureAllowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDetector = new GestureDetectorCompat(this, new GestureListener());

        mImageView = (ImageView) findViewById(R.id.pic);
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                // Used only to find if the gesture has originated in the bounds of the image
                mIsGestureAllowed = true;
                return false;
            }
        });

        View container = findViewById(R.id.container);
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!mIsGestureAllowed) {
                    return true;
                }

                mDetector.onTouchEvent(motionEvent);

                int pointerIndex;

                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        pointerIndex = motionEvent.getActionIndex();
                        mPrevFingerA.set(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex));

                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        pointerIndex = motionEvent.getActionIndex();

                        if (motionEvent.getPointerId(pointerIndex) == 0) {
                            mPrevFingerA.set(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex));
                        }

                        if (motionEvent.getPointerId(pointerIndex) == 1) {
                            mPrevFingerB.set(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex));
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:
                        updatePoints(motionEvent);

                        if (motionEvent.getPointerCount() == 1) {
                            translateInOneFingerMode(motionEvent);
                        }

                        if (motionEvent.getPointerCount() == 2) {
                            translate();
                            rotate();
                            scale();
                        }

                        mPrevFingerA.set(mFingerA);
                        mPrevFingerB.set(mFingerB);

                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // Last finger is up
                        if (motionEvent.getPointerCount() == 1) {
                            mIsGestureAllowed = false;
                        }

                        break;
                }

                return true;
            }
        });
    }

    private void updatePoints(MotionEvent motionEvent) {
        // Only the first two fingers are considered in the calculations
        int pointerId;

        for (int i = 0; i < motionEvent.getPointerCount(); ++i) {
            pointerId = motionEvent.getPointerId(i);
            if (pointerId == 0) {
                mFingerA.set(motionEvent.getX(i), motionEvent.getY(i));
            }

            if (pointerId == 1) {
                mFingerB.set(motionEvent.getX(i), motionEvent.getY(i));
            }
        }
    }

    private void translateInOneFingerMode(MotionEvent motionEvent) {
        int pointerIndex = motionEvent.getActionIndex();
        int pointerId = motionEvent.getPointerId(pointerIndex);

        if (pointerId == 0) {
            mImageView.setTranslationX(mImageView.getTranslationX() + mFingerA.x - mPrevFingerA.x);
            mImageView.setTranslationY(mImageView.getTranslationY() + mFingerA.y - mPrevFingerA.y);
        }

        if (pointerId == 1) {
            mImageView.setTranslationX(mImageView.getTranslationX() + mFingerB.x - mPrevFingerB.x);
            mImageView.setTranslationY(mImageView.getTranslationY() + mFingerB.y - mPrevFingerB.y);
        }
    }

    private void translate() {
        mPrevCenter.set((mPrevFingerA.x + mPrevFingerB.x) / 2, (mPrevFingerA.y + mPrevFingerB.y) / 2);
        mCenter.set((mFingerA.x + mFingerB.x) / 2, (mFingerA.y + mFingerB.y) / 2);

        mImageView.setTranslationX(mImageView.getTranslationX() + mCenter.x - mPrevCenter.x);
        mImageView.setTranslationY(mImageView.getTranslationY() + mCenter.y - mPrevCenter.y);
    }

    private void rotate() {
        double prevAngle = Math.atan2(mPrevFingerB.y - mPrevFingerA.y, mPrevFingerB.x - mPrevFingerA.x);
        double angle = Math.atan2(mFingerB.y - mFingerA.y, mFingerB.x - mFingerA.x);

        mImageView.setRotation(mImageView.getRotation() + (float) Math.toDegrees(angle - prevAngle));
    }

    private void scale() {
        float prevDistance = (float) Math.sqrt(Math.pow(mPrevFingerB.x - mPrevFingerA.x, 2) + Math.pow(mPrevFingerB.y - mPrevFingerA.y, 2));
        float distance = (float) Math.sqrt(Math.pow(mFingerB.x - mFingerA.x, 2) + Math.pow(mFingerB.y - mFingerA.y, 2));

        mImageView.setScaleX(mImageView.getScaleX() * distance / prevDistance);
        mImageView.setScaleY(mImageView.getScaleY() * distance / prevDistance);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            // Reset to default
            mImageView.setTranslationX(0);
            mImageView.setTranslationY(0);
            mImageView.setRotation(0.0f);
            mImageView.setScaleX(1);
            mImageView.setScaleY(1);

            return true;
        }
    }
}
