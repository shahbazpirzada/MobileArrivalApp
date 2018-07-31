package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScanActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    //public String UrlPost = "http://192.168.1.203:8080/rest/entrypoint/branches/2/entryPoints/2/visits";
    public String UrlPost;
    public String UserName;
    public String Password;


    //camera permission is needed.

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        LoadDatabaseData();// Set the scanner view as the content view

        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera(1);          // Start camera on resume
    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {
        // Do something with  the result here
        Log.v("kkkk", result.getContents()); // Prints scan results
        Log.v("uuuu", result.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)


        // LoadingCase();


        String dataStr = result.getContents();

        if (dataStr.contains("[a-zA-Z]+") == false) {


            String data = dataStr.substring(0, dataStr.length() - 4).replaceFirst("^0+(?!$)", "");
            POST(data);

        } else {

            Toast.makeText(this, "Scan proper QR Code.", Toast.LENGTH_SHORT).show();
            onBackPressed();

        }

        //showChangeLangDialog("12","shahbaz","nadra","pitb");


//        DataPassClass dataPassClass = new DataPassClass();
//        dataPassClass.GetDataFromAct();
//        MainActivity mainActivity = new MainActivity();
//        mainActivity.DisplayMsg();


        //  MainActivity.tvresult.setText(result.getContents());
        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }

    public void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }


    public String POST(String Data) {

        LoadDatabaseData();// Set the scanner view as the content view

        UrlPost = "http://" + UrlPost + ":8080/rest/entrypoint/branches/2/entryPoints/1/visits";

        JSONObject obj1 = new JSONObject();
        try {
            obj1.put("notes", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject obj2 = new JSONObject();
        try {
            obj2.put("parameters", obj1);
            obj2.put("appointmentId", Data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Convert JSONObject to JSON to String
        enableStrictMode();
        HttpResponse response = null;
        HttpClient httpclient = null;
        httpclient = new DefaultHttpClient();

        InputStream inputStream = null;
        String result = "";
        try {

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost();

            String userName = UserName;
            String password = Password;
            String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                    (userName + ":" + password).getBytes(),
                    Base64.NO_WRAP);


            httpPost.setHeader("Authorization", base64EncodedCredentials);
            httpPost.setHeader("Content-type", "application/json");



            httpPost.setEntity(new StringEntity(obj2.toString()));

            httpPost.setURI(new URI(UrlPost));

            response = httpclient.execute(httpPost);
            int responseCode = response.getStatusLine().getStatusCode();

            if (responseCode == 200) {

                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    // Read the content stream
                    InputStream instream = entity.getContent();
                    Header contentEncoding = response.getFirstHeader("Content-Encoding");
                    if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                        instream = new GZIPInputStream(instream);
                    }

                    // convert content stream to a String
                    String resultString = convertStreamToString(instream);
                    instream.close();


                    try {
                        JSONObject jsonObj = new JSONObject(resultString);

                        String id = jsonObj.getString("id");
                        String ticketId = jsonObj.getString("ticketId");
                        String CustomerName = jsonObj.getJSONObject("parameterMap").getString("customers");
                        String BranchName = jsonObj.getJSONObject("parameterMap").getString("branchName");
                        String ServiceName = jsonObj.getJSONObject("currentVisitService").getString("serviceInternalName");


                        Intent myIntent = new Intent(this, ThirdActivity.class);
                        myIntent.putExtra("CustomerName", CustomerName);
                        myIntent.putExtra("TicketId", ticketId);
                        myIntent.putExtra("BranchName", BranchName);
                        myIntent.putExtra("ServiceName", ServiceName);
                        myIntent.putExtra("ServiceTypeMethodCall", "1");


                        startActivity(myIntent);


                        System.out.println("id " + id);
                        System.out.println("TicketID :" + ticketId);
                        System.out.println("Customer Name" + CustomerName);
                        System.out.println("Service Name: " + ServiceName);

                        // showChangeLangDialog(ticketId,CustomerName, BranchName, ServiceName);


                    } catch (JSONException e) {
                        e.printStackTrace();

                        System.out.println(e);
                    }

                }


            }

           else if (responseCode == 500) {

                Intent myIntent = new Intent(this, ThirdActivity.class);
                myIntent.putExtra("ServiceTypeMethodCall", "2");
                startActivity(myIntent);

            }

           else if (responseCode == 409)
            {

                Intent myIntent = new Intent(this, ThirdActivity.class);
                myIntent.putExtra("ServiceTypeMethodCall", "3");
                startActivity(myIntent);

            }
            else {

//                Toast.makeText(this, "Please Try Again", Toast.LENGTH_LONG).show();
//                onBackPressed();

                Intent myIntent = new Intent(this, ThirdActivity.class);
                myIntent.putExtra("ServiceTypeMethodCall", "4");
                startActivity(myIntent);
            }


        } catch (Exception e) {
            System.out.println("Exception is: " + e);
        }

        return result;

    }

    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 *
		 * (c) public domain: http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/11/a-simple-restful-client-at-android/
		 */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void showChangeLangDialog(String ticketId, String CustomerName, String BranchName, String ServiceName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom, null);
        dialogBuilder.setView(dialogView);

        final TextView edt = (TextView) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Arrived Successfully");
        dialogBuilder.setMessage("Mr/Mrs. " + CustomerName + ".\nYour Ticket Number " + ticketId + " is confirmed.\n");

        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void LoadingCase() {

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    public void LoadDatabaseData() {
        List<SettingData> listIp = SettingData.listAll(SettingData.class);
        for (SettingData ips : listIp) {

            UrlPost = ips.getUrlLink();
            UserName = ips.getUserNameData();
            Password = ips.getPasswordData();
        }
    }

}

