package pk.edu.pl.pk_wfmi_schedule_notificator.validation;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.activity.CurrentFilesActivity;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.TimetableManager;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationAsyncTask extends AsyncTask<Void, Void, Timetable> {
    private Logger log = LoggerFactory.getLogger(NotificationAsyncTask.class);

    private TimetableManager timetableManager;
    private Context context;

    public NotificationAsyncTask(TimetableManager timetableManager, Context context) {
        this.timetableManager = timetableManager;
        this.context = context;
    }

    @Override
    protected Timetable doInBackground(Void... voids) {
        log.debug("Service started");
        try {
            return timetableManager.takeNewerIfAppeared();
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    protected void onPostExecute(Timetable timetable) {
        if (timetable != null) {
            sendNotification(context);
        }
        log.debug("Service finished");
    }

    private void sendNotification(Context context) {
        log.debug("Sending notification");

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
