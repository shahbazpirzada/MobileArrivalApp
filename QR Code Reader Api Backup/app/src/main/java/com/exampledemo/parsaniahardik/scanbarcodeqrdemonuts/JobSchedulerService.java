package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

public class JobSchedulerService extends JobService {

    private static final String TAG = "JobSchedulerService";
    GlobalClass globalClass;
    public JobSchedulerService() {

    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(TAG, "onStartJob:");
        startServiceIntent();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        Log.i(TAG, "onStartJob:");
        return false;
    }

    public void startServiceIntent(){
        globalClass = GlobalClass.getInstance();
        Intent intent
                = new Intent(this,BackendServiceIntent.class);
//        intent.putExtra("Url",globalClass.getUrlPost());
//        intent.putExtra("UserName",globalClass.getUserName());
//        intent.putExtra("Password",globalClass.getPassword());
        startService(intent);

    }


}
