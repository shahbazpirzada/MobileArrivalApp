package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;
import com.skydoves.colorpickerpreference.ColorPickerView;

import java.util.List;

public class SettingActivity extends AppCompatActivity  implements ColorPickerDialogListener {

    // Image loading result to pass to startActivityForResult method.
    private static int LOAD_IMAGE_RESULTS = 1;

    EditText editUrl;
    EditText UserName;
    EditText Password;
    EditText branchId;
    EditText entryPoint;

    String pathFile;
    TextView heading,tx,tx2,tx3,tx4,tx5,tx6;
    Typeface tf;
    Button btn1,btn2,btn3;
    private AlertDialog alertDialog;

    private DevicePolicyManager mDpm;
    private View mDecorView;
    private ColorPickerView colorPickerView;

    int colorGlobalValue;
    ConstraintLayout layout;
    GlobalClass globalClass = GlobalClass.getInstance();
    private static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tf = Typeface.createFromAsset(getAssets(),"raleway_medium.ttf");

        editUrl = (EditText) findViewById(R.id.editText);
        UserName= (EditText) findViewById(R.id.userName);
        Password= (EditText) findViewById(R.id.passwordText);
        branchId= (EditText) findViewById(R.id.branchID1);
        entryPoint= (EditText) findViewById(R.id.entryPoint);

        heading = (TextView) findViewById(R.id.textView7);
        tx = (TextView) findViewById(R.id.textView);
        tx2 = (TextView) findViewById(R.id.textView5);
        tx3 = (TextView) findViewById(R.id.textView6);
//        tx4 = (TextView) findViewById(R.id.textView8);
        tx5 = (TextView) findViewById(R.id.textView9);
        tx6 = (TextView) findViewById(R.id.textView10);

        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button4);
        btn3 = (Button) findViewById(R.id.ExitBtn);

        editUrl.setTypeface(tf);
        UserName.setTypeface(tf);
        Password.setTypeface(tf);
        heading.setTypeface(tf);
        tx.setTypeface(tf);
        tx2.setTypeface(tf);
        tx3.setTypeface(tf);
//        tx4.setTypeface(tf);
        tx5.setTypeface(tf);
        tx6.setTypeface(tf);

        btn1.setTypeface(tf);
        btn2.setTypeface(tf);
        btn3.setTypeface(tf);


        String LinkUrl="";
        String UserName1="";
        String Password1="";
        String branchId1="";
        String entryPoint1="";


        List<SettingData> listIp = SettingData.listAll(SettingData.class);
        for (SettingData ips:listIp) {
            LinkUrl = ips.getUrlLink();
            UserName1 = ips.getUserNameData();
            Password1 = ips.getPasswordData();
            branchId1 =ips.getBranchID();
            entryPoint1 = ips.getEntryPoint();
        }

        editUrl.setText(LinkUrl);
        UserName.setText(UserName1);
        Password.setText(Password1);
        branchId.setText(branchId1);
        entryPoint.setText(entryPoint1);

        mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        onWindowFocusChanged(true);
        FullScreencall();
        createDialog();
        //set the color in the background of choosed color

        layout = (ConstraintLayout) findViewById(R.id.drawer_layout);
        layout.setBackgroundColor(globalClass.getBackgroundColor());

    }

    protected void onResume() {
        super.onResume();
        FullScreencall();
        layout.setBackgroundColor(globalClass.getBackgroundColor());

    }

    public void SaveDataDB(View view){


        long Count = SettingData.count(SettingData.class);



        if(editUrl.getText().toString().matches("")  || UserName.getText().toString().matches("")  || Password.getText().toString().matches("")   ||  pathFile == ""
                ||  branchId.getText().toString().matches("") ||  entryPoint.getText().toString().matches("")) {

            Toast.makeText(this, "Please Fill the Parametters", Toast.LENGTH_SHORT).show();
        }



        else
        {

            // IpAddressInverval.deleteAll(IpAddressInverval.class);


            if(Count == 0){
                // for finders using raw query.
                List<SettingData> iplist = SettingData.findWithQuery(SettingData.class, "Select * from SETTING_DATA");

                double id= 0;

                for (SettingData ip1:iplist) {
                    id= ip1.getId();

                }

                System.out.println("fetching the id"+id);

                if(id==0) {

                    SettingData ipAddressInverval = new SettingData();
                    ipAddressInverval.setUrlLink(editUrl.getText().toString());
                    ipAddressInverval.setUserNameData(UserName.getText().toString());
                    ipAddressInverval.setPasswordData(Password.getText().toString());
                    ipAddressInverval.setBranchID(branchId.getText().toString());
                    ipAddressInverval.setEntryPoint(entryPoint.getText().toString());
                    ipAddressInverval.setImagePath(pathFile);
                    ipAddressInverval.setColorBackground(colorGlobalValue);
                    ipAddressInverval.save();

                    Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
                }
            }

            else
            {
                List<SettingData> iplist = SettingData.findWithQuery(SettingData.class, "Select * from SETTING_DATA where ID=1");

                double id= 0;
               for (SettingData ip1:iplist) {
                    id= ip1.getId();
                }

                SettingData ipini = SettingData.findById(SettingData.class, (int) id);
                ipini.setUrlLink(editUrl.getText().toString());
                ipini.setUserNameData(UserName.getText().toString());
                ipini.setPasswordData(Password.getText().toString());
                ipini.setBranchID(branchId.getText().toString());
                ipini.setEntryPoint(entryPoint.getText().toString());
                ipini.setImagePath(pathFile);
                ipini.setColorBackground(colorGlobalValue);
                ipini.save(); // updates the previous entry with new values.
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
            }




        }


    }

    public void CallMe(View view)
    {

//        Toast.makeText(this, "Press me ", Toast.LENGTH_SHORT).show();

        // Create the Intent for Image Gallery.
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
        startActivityForResult(i, LOAD_IMAGE_RESULTS);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            pathFile = imagePath;


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
            Toast.makeText(this, "Image Path Extracted", Toast.LENGTH_SHORT).show();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ExitApp(View view){


        try{
           // mDpm.clearDeviceOwnerApp("com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts");
            stopLockTask();
        }catch (Exception ex)
        {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("Try catch: "+ex);
        }

//            mDpm.clearDeviceOwnerApp("com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts");
//            stopLockTask();


    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for higher api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void createDialog(){

//        final ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
//        builder.setTitle("ColorPicker Dialog");
//        builder.setPreferenceName("MyColorPickerDialog");
//        builder.setPositiveButton(getString(R.string.confirm), new ColorListener() {
//            @Override
//            public void onColorSelected(ColorEnvelope colorEnvelope) {
//
//                colorPickerView = builder.getColorPickerView();
//                colorPickerView.clearSavedData();
//                colorPickerView.setSavedColor(colorEnvelope.getColor());
//                colorGlobalValue = colorEnvelope.getColor();
//                //getWindow().getDecorView().setBackgroundColor(colorGlobalValue);
//                layout.setBackgroundColor(colorGlobalValue);
//            }
//        });
//        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//
//        alertDialog = builder.create();







    }

    public void showDialog(View view) {
        //alertDialog.show();
        com.jaredrummler.android.colorpicker.ColorPickerDialog.newBuilder()
                .setDialogType(com.jaredrummler.android.colorpicker.ColorPickerDialog.TYPE_PRESETS)
                .setAllowPresets(true )
                .setDialogId(DIALOG_ID)
                .setColor(Color.BLACK)
                .setShowAlphaSlider(true)
                .show(this);
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        switch (dialogId) {
            case DIALOG_ID:
                // We got result from the dialog that is shown when clicking on the icon in the action bar.
                //Toast.makeText(SettingActivity.this, "Selected Color: #" + Integer.toHexString(color), Toast.LENGTH_SHORT).show();
                Log.d("demo color", "New default color is: #"+ Integer.toHexString(color) );
                colorGlobalValue = color;
                layout.setBackgroundColor(color);
                break;
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
