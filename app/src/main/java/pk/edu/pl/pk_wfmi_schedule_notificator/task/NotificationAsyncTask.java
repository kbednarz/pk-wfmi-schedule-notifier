package pk.edu.pl.pk_wfmi_schedule_notificator.task;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.snappydb.SnappydbException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.activity.MainActivity;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.TimetableManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationAsyncTask extends AsyncTask<Void, Void, List<Timetable>> {
    private Logger log = LoggerFactory.getLogger(NotificationAsyncTask.class);

    private Storage storage;
    private TimetableManager timetableManager;

    @SuppressLint("StaticFieldLeak")
    private Context context;

    public NotificationAsyncTask(Context context) throws SnappydbException {
        this.context = context;
        storage = new Storage(context);
        timetableManager = new TimetableManager(storage);
    }

    @Override
    protected List<Timetable> doInBackground(Void... voids) {
        log.debug("NotificationAsyncTask started");
        try {
            return timetableManager.fetchNewest();
        } catch (Exception e) {
            log.error("NotificationAsyncTask error occurred", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Timetable> timetable) {
        if (timetable != null) {
            log.debug("New schedule appeared. Calling notification");
            sendNotification(context);
        }

        closeStorage();

        log.debug("NotificationAsyncTask finished");
    }

    private void sendNotification(Context context) {
        log.debug("Sending notification");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "channel_xd")
                        .setSmallIcon(R.drawable.notification_small_icon)
                        .setContentTitle("PK WFMI")
                        .setContentText("New schedule appeared!");

        Intent resultIntent = new Intent(context, MainActivity.class);

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

    private void closeStorage() {
        try {
            storage.close();
        } catch (SnappydbException e) {
            log.error("Cannot close DB", e);
        }
    }
}
