package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BasicDeviceAdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        Toast.makeText(context, context.getString(R.string.device_admin_enabled), Toast.LENGTH_SHORT).show();
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return context.getString(R.string.device_admin_warning);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context, context.getString(R.string.device_admin_disabled), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLockTaskModeEntering(Context context, Intent intent, String pkg) {
        Toast.makeText(context, context.getString(R.string.kiosk_mode_enabled), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLockTaskModeExiting(Context context, Intent intent) {
        Toast.makeText(context, context.getString(R.string.kiosk_mode_disabled), Toast.LENGTH_SHORT).show();
    }
    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext(), BasicDeviceAdminReceiver.class);
    }

}
