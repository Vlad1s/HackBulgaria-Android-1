package com.android.vlad.flappy;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadResults extends AsyncTask<String, Void, String> {
    private WeakReference<Context> mContext;

    public UploadResults(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... params) {
        String result;

        try {
            URL url = new URL("http://95.111.103.224:8080/Flappy/scores");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            httpCon.setRequestProperty("Content-type", "application/json");
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write("{ \"name\" : \"" + params[0]
                    + "\" , \"mail\" : \"" + params[1]
                    + "\" , \"whereFrom\" : \"" + params[2]
                    + "\" , \"score\" : " + params[3] + "}");
            out.flush();
            out.close();

            result = "Your score has been uploaded!";
        } catch (IOException e) {
            result = "Score upload failed! Unable to connect to server!";
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (mContext.get() != null) {
            Toast.makeText(mContext.get(), s, Toast.LENGTH_LONG).show();
        }
    }
}
