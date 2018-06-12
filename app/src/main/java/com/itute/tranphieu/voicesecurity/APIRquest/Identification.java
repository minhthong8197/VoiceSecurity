package com.itute.tranphieu.voicesecurity.APIRquest;

import android.os.AsyncTask;
import android.util.Log;

import com.itute.tranphieu.voicesecurity.Record.ByteArray;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by minht on 25-May-18.
 */

public class Identification extends AsyncTask<String, Void, String> {
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .build();

    final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    String subscriptionKey;
    String pathname;

    public Identification(String subscriptionKey, String pathname) {
        this.subscriptionKey = subscriptionKey;
        this.pathname = pathname;
    }

    @Override
    protected String doInBackground(String... strings) {
        RequestBody requestBody = RequestBody.create(JSON, ByteArray.convertFileToByteArray(pathname));
        //Log.d("\t\t\tsau requestBody", "");
        Request request = new Request.Builder()
                .url(strings[0])
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() > 202) return "Error on Identification " + response.code();
            else {
                //Log.d("\n\n\nIdentification link:", response.headers().get("Operation-Location").toString());
                return response.headers().get("Operation-Location").toString();
            }
            //else return  response.headers().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
