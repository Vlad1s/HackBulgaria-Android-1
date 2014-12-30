package com.android.vlad.multipleactivities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText data = (EditText) findViewById(R.id.data);

        Button button_dial = (Button) findViewById(R.id.button_dial);
        button_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataValid("\\+?[0-9]{3,20}", data.getText().toString())) {
                    submitIntent(Intent.ACTION_DIAL, "tel:" + data.getText().toString());
                }
            }
        });

        Button button_browse = (Button) findViewById(R.id.button_browse);
        button_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataValid("([a-z]\\.)*[a-z]+\\.[a-z]+", data.getText().toString())) {
                    submitIntent(Intent.ACTION_VIEW, "https://" + data.getText().toString());
                }
            }
        });

        Button button_set_alarm = (Button) findViewById(R.id.button_set_alarm);
        button_set_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataStr = data.getText().toString();
                if (isDataValid("(([0-1]?[0-9])|(2[0-3])):[0-5][0-9]", dataStr)) {
                    Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                            .putExtra(AlarmClock.EXTRA_MESSAGE, "Alarm")
                            .putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(dataStr.substring(0, dataStr.indexOf(':'))))
                            .putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(dataStr.substring(dataStr.indexOf(':') + 1)));

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean isDataValid(String patternStr, String matcherStr) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(matcherStr);

        if (!matcher.matches()) {
            Toast.makeText(this, "Wrong input data! Enter new one!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void submitIntent(String action, String data) {
        Intent intent = new Intent(action);
        intent.setData(Uri.parse(data));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
