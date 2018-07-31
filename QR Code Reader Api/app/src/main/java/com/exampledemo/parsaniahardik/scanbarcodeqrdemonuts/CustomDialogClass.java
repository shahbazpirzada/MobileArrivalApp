package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Stech on 4/5/2018.
 */

public class CustomDialogClass extends Dialog implements View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    private int id;
    TextView tx;
    Typeface tf;
    RelativeLayout layout;

    GlobalClass globalClass = GlobalClass.getInstance();


    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        tx = (TextView) findViewById(R.id.text_dialog);
        tx.setText("Appointment not found");
        tx.setTypeface(tf);

        yes = (Button) findViewById(R.id.btn_yes);
        yes.setOnClickListener(this);
        layout = (RelativeLayout) findViewById(R.id.custom1);
        layout.setBackgroundColor(globalClass.getBackgroundColor());


    }


    @Override
    public void onClick(View view) {
        Intent i = new Intent(getContext(), MainActivity.class);
        c.startActivity(i);
    }
}
