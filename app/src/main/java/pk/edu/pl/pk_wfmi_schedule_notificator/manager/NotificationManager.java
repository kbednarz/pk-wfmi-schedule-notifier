package pk.edu.pl.pk_wfmi_schedule_notificator.manager;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

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
        long interval = 3000;

        manager.setInexactRepeating(android.app.AlarmManager.RTC, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
    }
}
