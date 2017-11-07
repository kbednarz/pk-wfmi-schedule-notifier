package pk.edu.pl.pk_wfmi_schedule_notificator.manager;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import pk.edu.pl.pk_wfmi_schedule_notificator.receiver.AlarmReceiver;

public class NotificationManager {
    private Context context;

    public NotificationManager(Context context) {
        this.context = context;
    }

    public void startBackgroundService() {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        android.app.AlarmManager manager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        manager.setInexactRepeating(android.app.AlarmManager.RTC, System.currentTimeMillis(),
                AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
    }
}
