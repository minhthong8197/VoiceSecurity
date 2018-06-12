package com.itute.tranphieu.voicesecurity.APIRquest;

import android.os.AsyncTask;
import android.text.LoginFilter;
import android.util.Log;

import com.itute.tranphieu.voicesecurity.Record.ByteArray;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by minht on 25-May-18.
 */

public class CreateEnrollment extends AsyncTask<String, Void, String> {
    String subscriptionKey;
    String pathname;
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .build();

    public CreateEnrollment(String subscriptionKey, String pathname) {
        this.subscriptionKey = subscriptionKey;
        this.pathname = pathname;
    }

    @Override
    protected String doInBackground(String... strings) {
        RequestBody requestBody = RequestBody.create(null, ByteArray.convertFileToByteArray(pathname));

        Request request = new Request.Builder()
                .url(strings[0])
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            //JSONArray jsonArray = response.body().string();
            if (response.code() > 202) return "";
            else {
                //Log.d("\nOperation-Location ", response.headers().get("Operation-Location").toString());
                return response.headers().get("Operation-Location").toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        //Log.d("\n\n\nCreateEnrollment:", s);
//        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
//        txtResponse.setText(s);
//        try {
//            getOperationStatus(s);
//            Thread.sleep(5000);
//            getOperationStatus(s);
//            Thread.sleep(5000);
//            getOperationStatus(s);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
