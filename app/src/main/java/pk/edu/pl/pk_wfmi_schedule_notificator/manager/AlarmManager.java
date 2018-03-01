package pk.edu.pl.pk_wfmi_schedule_notificator.manager;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pk.edu.pl.pk_wfmi_schedule_notificator.receiver.AlarmReceiver;

public class AlarmManager {
    private static final String TAG = "AlarmManager";

    private Context context;

    public AlarmManager(Context context) {
        this.context = context;
    }

    public void startBackgroundService() {

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

        boolean alarmUp = (PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp) {
            Log.d(TAG, "Scheduling Alarm");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            android.app.AlarmManager manager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            manager.setInexactRepeating(android.app.AlarmManager.RTC, System.currentTimeMillis(),
                    android.app.AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
        } else {
            Log.d(TAG, "Alarm is already up");
        }
    }
}
