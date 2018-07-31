package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



import java.util.List;

public class SecondActivity extends AppCompatActivity {

    EditText editipaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Prepopulate the data of TextField...

        String Ip="";
        String Interval="";


    }

    public void NewFunction(View view){

        Toast.makeText(this, "fucntions", Toast.LENGTH_SHORT).show();
        }

}
