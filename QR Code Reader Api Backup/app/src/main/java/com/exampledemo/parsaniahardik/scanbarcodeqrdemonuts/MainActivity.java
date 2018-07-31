package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.PublicKey;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.zip.GZIPInputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private ZBarScannerView mScannerView;

    View mDecorView;
    GlobalClass globalClass;

    private static final String TAG = "MainActivity";
    JobScheduler mJobScheduler;

    ImageView imageViewModify;
    TextView statusText;
    TextView BranchName;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        globalClass = GlobalClass.getInstance();
        LoadDatabaseData();

        btn = (Button) findViewById(R.id.btn);
//        imageView = (ImageView) findViewById(R.id.imageView1);
//        lineText = (TextView) findViewById(R.id.textView8);
//        tickmarkpic = (ImageView)findViewById(R.id.imageView2);
        mDecorView = getWindow().getDecorView();
        //hideSystemUI();

        onWindowFocusChanged(true);

//        showSystemUI();
        //  DisplayMsg();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalClass.getUrlPost() == null || globalClass.getUserName() == null || globalClass.getPassword() == null) {

                    Toast.makeText(MainActivity.this, "Please fill the parametters", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                    startActivity(intent);
                }


            }
        });


        ChangeImageLogo();

        mJobScheduler = (JobScheduler)
                getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(1,
                new ComponentName(getPackageName(),
                        JobSchedulerService.class.getName()));
        builder.setPeriodic(15000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        if (mJobScheduler.schedule(builder.build()) <= 0) {
            Log.e(TAG, "onCreate: Some error while scheduling the job");
        }

        statusText = (TextView) findViewById(R.id.statusID);
        BranchName = (TextView) findViewById(R.id.branchID);


    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadDatabaseData();
        ChangeImageLogo();
    }

    public void ChangeImageLogo() {

        imageViewModify = (ImageView) findViewById(R.id.imageViewPic);
        String path = globalClass.getImagePath();
        imageViewModify.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    public void LoadDatabaseData() {

        List<SettingData> listIp = SettingData.listAll(SettingData.class);
        for (SettingData ips : listIp) {

            globalClass.setUrlPost(ips.getUrlLink());
            globalClass.setUserName(ips.getUserNameData());
            globalClass.setPassword(ips.getPasswordData());
            globalClass.setImagePath(ips.getImagePath());

        }
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    //    // This snippet shows the system bars. It does this by removing all the flags
//// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
        unregisterReceiver(receiver);
    }


    @Override
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);

        ChangeImageLogo();
        provisionOwner();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                BackendServiceIntent.NOTIFICATION));
        ChangeImageLogo();
        startLockTask();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void provisionOwner() {
        DevicePolicyManager manager =
                (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = BasicDeviceAdminReceiver.getComponentName(this);

        if (!manager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            startActivityForResult(intent, 0);
            return;
        }

        if (manager.isDeviceOwnerApp(getPackageName()))
            manager.setLockTaskPackages(componentName, new String[]{getPackageName()});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kiosk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        showDialogueBox();
        //stopLockTask();
    }


    public void showDialogueBox() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter Password");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                // Toast.makeText(MainActivity.this, m_Text, Toast.LENGTH_SHORT).show();

                if (m_Text.equals("stech")) {

                    stopLockTask();
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);

                } else {

                    Toast.makeText(MainActivity.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    //boradCast Receiver....

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int resultCode = bundle.getInt(BackendServiceIntent.Result);
                String branchName = bundle.getString("Name");
                if (resultCode == RESULT_OK) {

                    statusText.setText("Connected");
                    BranchName.setText(branchName);

                } else {

                    statusText.setText("Disconnected");
                }
            }
        }
    };



    //end receiver



    public void codeEnterID(View view) {

        //   Toast.makeText(this , "kjasdkjsahd", Toast.LENGTH_SHORT).show();

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.QRCode1);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        Toast.makeText(MainActivity.this, "helo "+editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        POST(editText.getText().toString());

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public String POST(String Data) {
        enableStrictMode();
        String UrlPost = "http://" + globalClass.getUrlPost() + ":8080/rest/entrypoint/branches/2/entryPoints/1/visits";

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
//        enableStrictMode();
        HttpResponse response = null;
        HttpClient httpclient = null;
        httpclient = new DefaultHttpClient();

        InputStream inputStream = null;
        String result = "";
        try {

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost();

            String userName = globalClass.getUserName();
            String password = globalClass.getPassword();
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


            } else if (responseCode == 500) {

                Intent myIntent = new Intent(this, ThirdActivity.class);
                myIntent.putExtra("ServiceTypeMethodCall", "2");
                startActivity(myIntent);

            } else if (responseCode == 409) {

                Intent myIntent = new Intent(this, ThirdActivity.class);
                myIntent.putExtra("ServiceTypeMethodCall", "3");
                startActivity(myIntent);

            } else {

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


    public void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

}
