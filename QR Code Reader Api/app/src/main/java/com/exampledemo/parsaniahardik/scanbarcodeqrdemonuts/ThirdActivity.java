package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.PublicKey;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ThirdActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();

    String CustomerName;
    String TicketId;
    String BranchName;
    String ServiceName;

    String ServiceTypeMethodCall;

    JsonData obj = JsonData.getInstance();

    TextView view1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        view1 = (TextView)findViewById(R.id.text_dialog);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                backToMainAcitivity();
            }
        }, 10000);

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

        if(Integer.parseInt(ServiceTypeMethodCall) == 5 )
        {

            ErrorCaseServer();

        }

    }

    private void backToMainAcitivity() {
        Intent intent = new Intent(ThirdActivity.this, MainActivity.class);

        startActivity(intent);
    }

    public void SuccessCase(){

//        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
//                .setTitleText("You have Arrived")
//
//                .setContentText("CustomerName: "+CustomerName+"\nTicket No: "+TicketId
//                        +"\nBranch Name: "+BranchName+"\nServiceName: "+ServiceName)
//                .setConfirmText("Ok!")
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
////                        sDialog.dismissWithAnimation();
//                        Intent myIntent = new Intent(ThirdActivity.this, MainActivity.class);
//                        startActivity(myIntent);
//
//                    }
//                })
//                .show();


        obj.setCustomerName(CustomerName);
        obj.setBranchName(BranchName);
        obj.setServiceName(ServiceName);
        obj.setTicketID(TicketId);
        CustomDialogSuccessClass customDialogClass = new CustomDialogSuccessClass(ThirdActivity.this);
        customDialogClass.show();


    }

    public void ErrorCase(){


//        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
//                .setTitleText("Oops...")
//                .setContentText("Appointment not found")
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                       Intent intent = new Intent(ThirdActivity.this,MainActivity.class);
//                       startActivity(intent);
//
//                    }
//                })
//                .show();
        CustomDialogClass cdd = new CustomDialogClass(ThirdActivity.this);
        cdd.show();



    }

    public void ErrorCaseServer(){


        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Failed to connect the server")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent intent = new Intent(ThirdActivity.this,MainActivity.class);
                        startActivity(intent);

                    }
                })
                .show();

//        CustomDialogClass cdd = new CustomDialogClass(ThirdActivity.this);
//        cdd.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
