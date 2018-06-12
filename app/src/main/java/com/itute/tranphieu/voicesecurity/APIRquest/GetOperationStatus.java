package com.itute.tranphieu.voicesecurity.APIRquest;

import android.icu.text.LocaleDisplayNames;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Dictionary;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetOperationStatus extends AsyncTask<String, Void, StatusClass> {
    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
    String subscriptionKey;

    public GetOperationStatus(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    @Override
    protected StatusClass doInBackground(String... strings) {
        Request request = new Request.Builder()
                .url(strings[0])
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .build();

        StatusClass statusResult = new StatusClass();

        boolean needCheckAgain = true;

        while (needCheckAgain) {
            try {
                Response response = okHttpClient.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                switch (jsonObject.getString("status")) {
                    case "notstarted":
                    case "running":
                        //continue;
                        break;

                    case "failed":
                        statusResult.setStatusSucceeded(false);
                        statusResult.setFailedMessage(jsonObject.getString("message"));
                        return statusResult;

                    case "succeeded":
                        statusResult.setStatusSucceeded(true);
                        JSONObject processingResult = jsonObject.getJSONObject("processingResult");
                        try {
                            statusResult.setRemainingEnrollmentSpeechTime(processingResult.getDouble("remainingEnrollmentSpeechTime"));
                            statusResult.setEnrolling(true);
                            return statusResult;
                        } catch (JSONException e) {
                            //e.printStackTrace();
                            statusResult.setEnrolling(false);
                            String identifiedProfileId = processingResult.getString("identifiedProfileId");
                            statusResult.setIdentifiedProfileId(identifiedProfileId);
                            return statusResult;
                        }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return statusResult;
    }
}