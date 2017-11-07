package pk.edu.pl.pk_wfmi_schedule_notificator.receiver;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.activity.CurrentFilesActivity;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.TimetableManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Storage storage = new Storage(context);
        TimetableManager timetableManager = new TimetableManager(storage);
        try {
            if (timetableManager.hasNewerAppeared()) {
                sendNotification(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendNotification(Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "channel_xd")
                        .setSmallIcon(R.drawable.notification_small_icon)
                        .setContentTitle("PK WFMI")
                        .setContentText("New schedule appeared!");

        Intent resultIntent = new Intent(context, CurrentFilesActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = 001;

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

}
