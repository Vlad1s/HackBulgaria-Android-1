package com.android.vlad.colorpreviewer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.color);
        final TextView textView = (TextView) findViewById(R.id.color_previewer);

        // This listener is redundant if EditText.addTextChangedListener is used
        Button button = (Button) findViewById(R.id.show_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "No color entered!", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    textView.setBackgroundColor(Color.parseColor(padColor(editText.getText().toString())));
                } catch (IllegalArgumentException iae) {
                    Toast.makeText(MainActivity.this, "Invalid color!", Toast.LENGTH_LONG).show();
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (editText.getText().toString().equals("")) {
                        return;
                    }

                    textView.setBackgroundColor(Color.parseColor(padColor(editText.getText().toString())));
                } catch (IllegalArgumentException iae) {
                    Toast.makeText(MainActivity.this, "Invalid color!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String padColor(String color) {
        String paddedColor = color;
        for (int i = 0; i < 7 - color.length(); i++) {
            paddedColor += '0';
        }

        return paddedColor;
    }
}
