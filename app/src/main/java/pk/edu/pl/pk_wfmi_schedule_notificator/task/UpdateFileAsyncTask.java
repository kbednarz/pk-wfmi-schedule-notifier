package pk.edu.pl.pk_wfmi_schedule_notificator.task;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.TimetableManager;

public class UpdateFileAsyncTask extends AsyncTask<Void, Void, Timetable> {
    public final static String FILTER = "TIMETABLE_UPDATE_FILTER";
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
            Intent intent = new Intent(FILTER);
            intent.putExtra("error", true);
            intent.putExtra("errorMsg", e.getMessage());
            context.sendBroadcast(intent);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Timetable timetables) {
        super.onPostExecute(timetables);
        Intent intent = new Intent(FILTER);
        if (timetables != null) {
            Log.d(TAG, "Updating current timetable list");
            intent.putExtra("isNewerAppeared", true);
        }
        context.sendBroadcast(intent);
    }
}
