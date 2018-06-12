package com.itute.tranphieu.voicesecurity.APIRquest;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by minht on 25-May-18.
 */

public class API {
    static String subscriptionKey = "4ca14aa243cb420b9d435e14632df449";

    public static String createProfile() {
        String url = "https://westus.api.cognitive.microsoft.com/spid/v1.0/identificationProfiles";
        try {
            return new CreateProfile(subscriptionKey).execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String createEnrollment(String pathname, String identificationProfileId) {
        //String identificationProfileId = "51662f54-eaa2-4272-9d8a-c40aae7b3b1d";
        String url = "https://westus.api.cognitive.microsoft.com/spid/v1.0/identificationProfiles/" + identificationProfileId + "/enroll?shortAudio=False";
        try {
            String linkStatus = new CreateEnrollment(subscriptionKey, pathname).execute(url).get();
            if(linkStatus.equals("")) return "";
            else return linkStatus;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void deleteProfile(String identificationProfileId) {
        //String identificationProfileId = "5d4e586a-4a1a-4a0d-8e7e-eb9ac5be03df";
        String url = "https://westus.api.cognitive.microsoft.com/spid/v1.0/identificationProfiles/" + identificationProfileId;
        new DeleteProfile(subscriptionKey).execute(url);
    }

    public static String getAllProfile() {
        String url = "https://westus.api.cognitive.microsoft.com/spid/v1.0/identificationProfiles";
        try {
            String responseBody = new GetAllProfile(subscriptionKey).execute(url).get();
            String result = "";
            JSONArray jsonArray = new JSONArray(responseBody);
            JSONObject jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("enrollmentStatus").equals("Enrolled"))
                    result += jsonObject.getString("identificationProfileId") + " ";
            }
            result = result.trim();
            result = result.replace(" ", ",");

            //Log.d("\n\n\ngetAllProfile", response);
            //Log.d("\n\n\ngetAllProfile", result);
            return result;
        } catch (InterruptedException e) {
            //e.printStackTrace();
        } catch (ExecutionException e) {
            //e.printStackTrace();
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getNeedDeleteProfile() {
        String url = "https://westus.api.cognitive.microsoft.com/spid/v1.0/identificationProfiles";
        try {
            String responseBody = new GetAllProfile(subscriptionKey).execute(url).get();
            ArrayList<String> result = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(responseBody);
            JSONObject jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("enrollmentStatus").equals("Enrolled") == false)
                    result.add(jsonObject.getString("identificationProfileId"));
            }
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void getProfile(String identificationProfileId) {
        //String identificationProfileId = "51662f54-eaa2-4272-9d8a-c40aae7b3b1d";
        String url = "https://westus.api.cognitive.microsoft.com/spid/v1.0/identificationProfiles/" + identificationProfileId;
        new GetProfile(subscriptionKey).execute(url);
    }

    public static void resetEnrollment(String identificationProfileId) {
        //String identificationProfileId = "298c0c06-a646-41c3-a5cf-8c95504b5846";
        String url = "https://westus.api.cognitive.microsoft.com/spid/v1.0/identificationProfiles/" + identificationProfileId + "/reset";
        new ResetEnrollment(subscriptionKey).execute(url);
    }

    public static String identification(String pathname, String identificationProfileIds) {
        //String url = "https://westus.api.cognitive.microsoft.com/spid/v1.0/identify?identificationProfileIds=" + identificationProfileIds + "&shortAudio=True";
        String url = "https://westus.api.cognitive.microsoft.com/spid/v1.0/identify?identificationProfileIds=" + identificationProfileIds + "&shortAudio=True";
        try {
            //Log.d("\n\n\n", pathname);
            //Log.d("\n\n\n", identificationProfileIds);
            return new Identification(subscriptionKey, pathname).execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static StatusClass getOperationStatus(String url) {
        StatusClass statusResult = new StatusClass();
        try {
            return new GetOperationStatus(subscriptionKey).execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return statusResult;
        }
        return statusResult;
    }
}
