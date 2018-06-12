package com.itute.tranphieu.voicesecurity.APIRquest;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by minht on 25-May-18.
 */

public class ResetEnrollment extends AsyncTask<String, Void, String> {
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();
    String subscriptionKey;

    public ResetEnrollment(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    @Override
    protected String doInBackground(String... strings) {
        Request request = new Request.Builder()
                .url(strings[0])
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .method("POST", RequestBody.create(null, ""))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() > 202) return "Error on ResetProfile " + response.code();
            else return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        //txtResponse.setText("202\n" + s);
        Log.d("\n\n\nResetEnrollment:", s);
    }
}
