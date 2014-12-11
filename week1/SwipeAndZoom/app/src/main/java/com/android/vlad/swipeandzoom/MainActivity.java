package com.android.vlad.swipeandzoom;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    private static final int SCALE = 2;

    private GestureDetectorCompat mDetector;
    private TypedArray mImages;
    private ImageView mImageView;
    private TextView mTextView;
    private String mCountStr;
    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImages = this.getResources().obtainTypedArray(R.array.images);
        mIndex = 0;

        mImageView = (ImageView) findViewById(R.id.image);
        mImageView.setImageDrawable(mImages.getDrawable(mIndex));

        mCountStr = "/" + mImages.length();
        mTextView = (TextView) findViewById(R.id.numbering);
        mTextView.setText(mIndex + 1 + mCountStr);

        mDetector = new GestureDetectorCompat(this, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (mImageView.getScaleX() == 1 || mImageView.getScaleY() == 1) {
                mImageView.setScaleX(SCALE);
                mImageView.setScaleY(SCALE);

                mTextView.setTranslationY((mImageView.getHeight() * SCALE - mImageView.getHeight()) / 2);
            } else {
                // Reset to default
                mImageView.setScaleX(1);
                mImageView.setScaleY(1);

                mTextView.setTranslationY(0);
            }

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float sensitivity = 50;

            if ((e1.getX() - e2.getX()) > sensitivity) {
                mIndex = mIndex == mImages.length() - 1 ? 0 : mIndex + 1;
            } else if ((e2.getX() - e1.getX()) > sensitivity) {
                mIndex = mIndex == 0 ? mImages.length() - 1 : mIndex - 1;
            } else {
                return true;
            }

            mImageView.setImageDrawable(mImages.getDrawable(mIndex));
            mTextView.setText(mIndex + 1 + mCountStr);

            return true;
        }
    }
}
