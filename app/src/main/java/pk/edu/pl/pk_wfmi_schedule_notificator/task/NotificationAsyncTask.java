package pk.edu.pl.pk_wfmi_schedule_notificator.task;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.snappydb.SnappydbException;

import java.io.IOException;
import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.activity.MainActivity;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.TimetableManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationAsyncTask extends AsyncTask<Void, Void, Timetable> {
    private static final String TAG = "NotificationAsyncTask";

    private Storage storage;
    private TimetableManager timetableManager;

    @SuppressLint("StaticFieldLeak")
    private Context context;

    public NotificationAsyncTask(Context context) throws SnappydbException, IOException {
        this.context = context;
        storage = new Storage(context);
        timetableManager = new TimetableManager(storage, context);
    }

    @Override
    protected Timetable doInBackground(Void... voids) {
        Log.d(TAG, "NotificationAsyncTask started");
        try {
            return timetableManager.update();
        } catch (Exception e) {
            Log.e(TAG, "NotificationAsyncTask error occurred", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Timetable timetable) {
        if (timetable != null) {
            Log.d(TAG, "New schedule appeared. Calling notification");
            sendNotification(context);
        }
        Log.d(TAG, "NotificationAsyncTask finished");
    }

    private void sendNotification(Context context) {
        Log.d(TAG, "Sending notification");

        String CHANNEL_ID = "pk_notifier_channel_01";

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
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

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (mNotifyMgr != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,
                        "PK Notifier", NotificationManager.IMPORTANCE_HIGH);
                mNotifyMgr.createNotificationChannel(mChannel);
            }

            mNotifyMgr.notify(1, mBuilder.build());
        }
    }

}
