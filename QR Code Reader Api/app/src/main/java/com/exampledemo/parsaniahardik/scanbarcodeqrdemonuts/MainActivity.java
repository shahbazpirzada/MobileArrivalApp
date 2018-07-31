package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.annotation.TargetApi;
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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import me.dm7.barcodescanner.zbar.ZBarScannerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    private Button btn,btn2;
    private ZBarScannerView mScannerView;

    View mDecorView;
    GlobalClass globalClass;

    private static final String TAG = "MainActivity";
    JobScheduler mJobScheduler;

    ImageView imageViewModify;
    TextView statusText;
    TextView BranchName;
    TextView counterValue;

    TextView tryingConnect;

    TextView tvTime, tvDate;
    java.util.Date noteTS;
    SimpleDateFormat simpleDateFormat;

    Typeface tf;

    ConstraintLayout layout;



    private DevicePolicyManager mDpm;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        globalClass = GlobalClass.getInstance();
        LoadDatabaseData();
        System.out.println("color in background: "+globalClass.getBackgroundColor());



        btn = (Button) findViewById(R.id.btn);
        btn2 = (Button) findViewById(R.id.btn2);

        counterValue = (TextView) findViewById(R.id.CounterTimerID);
        counterValue.setVisibility(View.GONE);

        tryingConnect = (TextView) findViewById(R.id.TryingConnect);
        tryingConnect.setVisibility(View.GONE);

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDate = (TextView) findViewById(R.id.tvDate);
        mDecorView = getWindow().getDecorView();

        tf = Typeface.createFromAsset(getAssets(),"raleway_medium.ttf");

        counterValue.setTypeface(tf);
        tryingConnect.setTypeface(tf);
       // tvTime.setTypeface(tf);
       // tvDate.setTypeface(tf);
        btn.setTypeface(tf);
        btn2.setTypeface(tf);
        imageViewModify = (ImageView) findViewById(R.id.imageViewPic);

        onWindowFocusChanged(true);

        MakeOwner();

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
        BranchName = (TextView) findViewById(R.id.branchID1);


        statusText.setTypeface(tf);
        BranchName.setTypeface(tf);


        imageViewModify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //     Toast.makeText(MainActivity.this, "click on image button", Toast.LENGTH_SHORT).show();
                showDialogueBox();
            }
        });

        mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdmin = new ComponentName(this, BasicDeviceAdminReceiver.class);

        if (!mDpm.isAdminActive(deviceAdmin)) {
           // Toast.makeText(this, getString(R.string.not_device_admin), Toast.LENGTH_SHORT).show();
        }

        if (mDpm.isDeviceOwnerApp(getPackageName())) {
            mDpm.setLockTaskPackages(deviceAdmin, new String[]{getPackageName()});



        } else {
               // Toast.makeText(this, getString(R.string.not_device_owner), Toast.LENGTH_SHORT).show();
        }
        if (mDpm.isLockTaskPermitted(this.getPackageName())) {
            startLockTask();

        }
        mDecorView = getWindow().getDecorView();


        //show date and time...
        runTimeDate();

        provisionOwner();

        layout = (ConstraintLayout) findViewById(R.id.drawer_layout);
        layout.setBackgroundColor(globalClass.getBackgroundColor());




    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadDatabaseData();
        ChangeImageLogo();
        layout.setBackgroundColor(globalClass.getBackgroundColor());
    }

    public void ChangeImageLogo() {


        String path = globalClass.getImagePath();
        imageViewModify.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    public void LoadDatabaseData() {

        List<SettingData> listIp = SettingData.listAll(SettingData.class);
        for (SettingData ips : listIp) {

            globalClass.setUrlPost(ips.getUrlLink());
            globalClass.setUserName(ips.getUserNameData());
            globalClass.setPassword(ips.getPasswordData());
            globalClass.setBranchId(ips.getBranchID());
            globalClass.setEntryPoint(ips.getEntryPoint());
            globalClass.setImagePath(ips.getImagePath());
            globalClass.setBackgroundColor(ips.getColorBackground());

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
        MakeOwner();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                BackendServiceIntent.NOTIFICATION));
        ChangeImageLogo();

        hideSystemUI();
        layout.setBackgroundColor(globalClass.getBackgroundColor());


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
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @TargetApi(Build.VERSION_CODES.M)



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

    @Override
    public void onBackPressed() {

            //super.onBackPressed();

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
                            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                            startActivity(intent);

                } else {

                    Toast.makeText(MainActivity.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);

                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent
                        = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
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
                    statusText.setTextColor(Color.parseColor("#008000"));
                    BranchName.setText(branchName);


                    counterValue.setVisibility(View.GONE);
                    tryingConnect.setVisibility(View.GONE);
                } else {
                    getCounter ();
                    tryingConnect.setVisibility(View.VISIBLE);
                    counterValue.setVisibility(View.VISIBLE);
                    statusText.setText("Disconnected");
                    statusText.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        }
    };



    //end receiver



    public void codeEnterID(View view) {

        //   Toast.makeText(this , "kjasdkjsahd", Toast.LENGTH_SHORT).show();


        if (globalClass.getUrlPost() == null || globalClass.getUserName() == null || globalClass.getPassword() == null) {

            Toast.makeText(MainActivity.this, "Please fill the parametters", Toast.LENGTH_SHORT).show();
        }
        else{

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
                            PostUrlData(editText.getText().toString());

                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent intent
                                             = new Intent(MainActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            });

            // create an alert dialog
            AlertDialog alert = alertDialogBuilder.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }

    }

    public void PostUrlData(final String data){

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                POST(data);

                return null;
            }
        }.execute();

    }


    public void POST(String Data) {

            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
            String UrlPost = "http://" + globalClass.getUrlPost() + ":8080/rest/entrypoint/branches/"+globalClass.getBranchId()+"/entryPoints/"+globalClass.getEntryPoint()+"/visits";

            String userName = globalClass.getUserName();
            String password = globalClass.getPassword();
            String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                    (userName + ":" + password).getBytes(),
                    Base64.NO_WRAP);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("appointmentId",Data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .addHeader("Authorization", base64EncodedCredentials)
                    .url(UrlPost)
                    .post(body)
                    .build();

            okhttp3.Response response = null;
            try {
                response = client.newCall(request).execute();

                int responseCode = response.code();

                if (responseCode == 200) {
                    String data = response.body().string();

                    JSONObject jsonObj = new JSONObject(data);

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

                    Intent myIntent = new Intent(this, ThirdActivity.class);
                    myIntent.putExtra("ServiceTypeMethodCall", "4");
                    startActivity(myIntent);
                }


            } catch (Exception e) {
                System.out.println("Exception is: " + e);
                Intent myIntent = new Intent(this, ThirdActivity.class);
                myIntent.putExtra("ServiceTypeMethodCall", "5");
                startActivity(myIntent);
            }



    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void provisionOwner() {
        DevicePolicyManager manager =
                (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = BasicDeviceAdminReceiver.getComponentName(this);

        if(!manager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            startActivityForResult(intent, 0);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (manager.isDeviceOwnerApp(getPackageName()))
            {
                manager.setLockTaskPackages(componentName, new String [] {getPackageName()});
                startLockTask();

            }
        }


    }


    public void getCounter (){


        new CountDownTimer(10000, 1000) {

                       public void onTick(long millisUntilFinished) {
                counterValue.setText(""+millisUntilFinished / 1000);
            }

            public void onFinish() {
              //  textView.setText("done!");
            }
        }.start();

    }


    public void runTimeDate(){

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTextView();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    private void updateTextView() {
        noteTS = Calendar.getInstance().getTime();

        simpleDateFormat = new SimpleDateFormat("h:mm a");
        String formattedDate = simpleDateFormat.format(noteTS);
        tvTime.setText(formattedDate.replace("AM", "am").replace("PM","pm"));


        simpleDateFormat = new SimpleDateFormat("EEE d/M/yyyy");

        tvDate.setText(simpleDateFormat.format(noteTS));
    }

    public void MakeOwner(){

        try {
            Runtime.getRuntime().exec("dpm set-device-owner com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts/.BasicDeviceAdminReceiver");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean result;
        switch( event.getKeyCode() ) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:

                result = true;
                break;

            default:
                result= super.dispatchKeyEvent(event);
                break;
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
            // do what you want with the power button
            return true;
        }
        return result;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Log.i("TAG", "Press Home");
            System.exit(0);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }



}
