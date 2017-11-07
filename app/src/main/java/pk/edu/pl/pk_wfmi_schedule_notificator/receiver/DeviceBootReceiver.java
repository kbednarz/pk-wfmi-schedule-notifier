package pk.edu.pl.pk_wfmi_schedule_notificator.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pk.edu.pl.pk_wfmi_schedule_notificator.manager.NotificationManager;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            NotificationManager notificationManager = new NotificationManager(context);
            notificationManager.startBackgroundService();

        }
    }
}
