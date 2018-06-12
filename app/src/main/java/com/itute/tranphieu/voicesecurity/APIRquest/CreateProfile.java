package com.itute.tranphieu.voicesecurity.APIRquest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by minht on 25-May-18.
 */

public class CreateProfile extends AsyncTask<String, Void, String> {
    String subscriptionKey;
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();

    public CreateProfile(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    @Override
    protected String doInBackground(String... strings) {
        JSONObject body = new JSONObject();
        try {
            body.put("locale", "en-us");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(strings[0])
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .post(RequestBody.create(null, body.toString()))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                if (response.code() > 202) return "";//jsonObject.getString("message");
                else return jsonObject.getString("identificationProfileId");
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        //Log.d("\n\n\nCreateProfile:", s);
    }
}
