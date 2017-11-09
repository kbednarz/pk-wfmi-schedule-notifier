package pk.edu.pl.pk_wfmi_schedule_notificator.manager;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pk.edu.pl.pk_wfmi_schedule_notificator.receiver.AlarmReceiver;

public class AlarmHandler {
    private Logger log = LoggerFactory.getLogger(AlarmHandler.class);

    private Context context;

    public AlarmHandler(Context context) {
        this.context = context;
    }

    public void startBackgroundService() {

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

        boolean alarmUp = (PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp) {
            log.debug("Starting AlarmManager");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                    AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
        } else {
            log.debug("Alarm is already up");
        }
    }
}
