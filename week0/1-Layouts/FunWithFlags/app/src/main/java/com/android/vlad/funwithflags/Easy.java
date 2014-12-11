package com.android.vlad.funwithflags;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Easy extends Activity {
    private int[] mColors;
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy);

        mColors = this.getResources().getIntArray(R.array.colors);

        TextView top = (TextView) findViewById(R.id.top);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColor(view);
            }
        });

        TextView middle = (TextView) findViewById(R.id.middle);
        middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColor(view);
            }
        });

        TextView bottom = (TextView) findViewById(R.id.bottom);
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColor(view);
            }
        });
    }

    private void changeColor(View view) {
        view.setBackgroundColor(mColors[mIndex++]);

        if (mIndex == mColors.length) {
            mIndex = 0;
        }
    }
}
