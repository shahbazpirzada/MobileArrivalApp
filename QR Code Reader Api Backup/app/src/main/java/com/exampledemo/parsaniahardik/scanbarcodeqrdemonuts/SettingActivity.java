package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    // Image loading result to pass to startActivityForResult method.
    private static int LOAD_IMAGE_RESULTS = 1;

    EditText editUrl;
    EditText UserName;
    EditText Password;
    String pathFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        editUrl = (EditText) findViewById(R.id.editText);
        UserName= (EditText) findViewById(R.id.userName);
        Password= (EditText) findViewById(R.id.passwordText);



        String LinkUrl="";
        String UserName1="";
        String Password1="";

        List<SettingData> listIp = SettingData.listAll(SettingData.class);
        for (SettingData ips:listIp) {
            LinkUrl = ips.getUrlLink();
            UserName1 = ips.getUserNameData();
            Password1 = ips.getPasswordData();
        }

        editUrl.setText(LinkUrl);
        UserName.setText(UserName1);
        Password.setText(Password1);
    }


    public void SaveDataDB(View view){


        long Count = SettingData.count(SettingData.class);



        if(editUrl.getText().toString().matches("")  || UserName.getText().toString().matches("")  || Password.getText().toString().matches("")   ||  pathFile == ""   ) {

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
                    ipAddressInverval.setImagePath(pathFile);
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
                ipini.setImagePath(pathFile);
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

}
