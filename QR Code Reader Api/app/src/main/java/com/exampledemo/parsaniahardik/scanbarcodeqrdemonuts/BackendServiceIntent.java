package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BackendServiceIntent extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    String branchName ="";

      int codeData;
    GlobalClass globalClass;
    public static final String NOTIFICATION = "com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts";



        public static final String Result ="";


    public BackendServiceIntent()
    {
        super("BackendServiceIntent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

            int getResult = hitUrlLink(intent);

            if (getResult == 200){
                result = Activity.RESULT_OK;

            }

            else
            {

                result = Activity.RESULT_CANCELED;
            }

            printResultToMainAcitivty(result ,branchName);

    }

    public int hitUrlLink(Intent intent){

        globalClass = GlobalClass.getInstance();
        String urlPath = globalClass.getUrlPost();
        String userName = globalClass.getUserName();
        String password = globalClass.getPassword();

        String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                (userName + ":" + password).getBytes(),
                Base64.NO_WRAP);


        urlPath= "http://" + urlPath + ":8080/rest/entrypoint/branches/"+globalClass.getBranchId();



        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .header("Authorization", base64EncodedCredentials)
                .url(urlPath)
                .build();

        Response response = null;
        try {

            response = client.newCall(request).execute();

            if(response.isSuccessful()){

                String jsonData = response.body().string();
                JSONObject Jobject = null;
                try {
                    Jobject = new JSONObject(jsonData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    branchName = Jobject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return response.code();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return 0;


    }


    private void printResultToMainAcitivty(int resultCode, String BranchName) {

        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(Result,resultCode);
        intent.putExtra("Name",BranchName);

        sendBroadcast(intent);

    }

}
