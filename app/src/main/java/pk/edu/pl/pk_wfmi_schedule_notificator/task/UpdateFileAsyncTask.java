package pk.edu.pl.pk_wfmi_schedule_notificator.task;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.TimetableManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.timetable.TimetableEventReceiver;

public class UpdateFileAsyncTask extends AsyncTask<Void, Void, Timetable> {
    private static final String TAG = "UpdateFileAsyncTask";
    private TimetableManager timetableManager;
    private Context context;

    public UpdateFileAsyncTask(TimetableManager timetableManager, Context context) {
        this.context = context;
        this.timetableManager = timetableManager;
    }

    @Override
    protected Timetable doInBackground(Void... voids) {
        try {
            return timetableManager.update();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            Intent intent = new Intent(TimetableEventReceiver.EVENT_FILTER);
            intent.putExtra(TimetableEventReceiver.ERROR, true);
            intent.putExtra(TimetableEventReceiver.ERROR_MESSAGE, e.getMessage());
            context.sendBroadcast(intent);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Timetable timetables) {
        super.onPostExecute(timetables);
        Intent intent = new Intent(TimetableEventReceiver.EVENT_FILTER);
        if (timetables != null) {
            Log.d(TAG, "Updating current timetable list");
            intent.putExtra(TimetableEventReceiver.NEWER_APPEARED, true);
        }
        context.sendBroadcast(intent);
    }
}
