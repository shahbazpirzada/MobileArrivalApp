package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import java.security.PublicKey;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ThirdActivity extends AppCompatActivity {


    String CustomerName;
    String TicketId;
    String BranchName;
    String ServiceName;

    String ServiceTypeMethodCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent intent = getIntent();

        ServiceTypeMethodCall = intent.getStringExtra("ServiceTypeMethodCall");

        if(Integer.parseInt(ServiceTypeMethodCall) == 1 )
        {
            CustomerName = intent.getStringExtra("CustomerName");
            TicketId = intent.getStringExtra("TicketId");
            BranchName = intent.getStringExtra("BranchName");
            ServiceName = intent.getStringExtra("ServiceName");
            SuccessCase();
        }

        if(Integer.parseInt(ServiceTypeMethodCall) == 2 )
        {

            ErrorCase();

        }

        if(Integer.parseInt(ServiceTypeMethodCall) == 3 )
        {

            ErrorCase();

        }

        if(Integer.parseInt(ServiceTypeMethodCall) == 4 )
        {

            ErrorCase();

        }
    }

    public void SuccessCase(){

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("You have Arrived")

                .setContentText("CustomerName: "+CustomerName+"\nTicket No: "+TicketId
                        +"\nBranch Name: "+BranchName+"\nServiceName: "+ServiceName)
                .setConfirmText("Ok!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
                        Intent myIntent = new Intent(ThirdActivity.this, MainActivity.class);
                        startActivity(myIntent);

                    }
                })
                .show();

    }

    public void ErrorCase(){


        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Appointment not found")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                       Intent intent = new Intent(ThirdActivity.this,MainActivity.class);
                       startActivity(intent);

                    }
                })
                .show();
    }

}
