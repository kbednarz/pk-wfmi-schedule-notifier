package pk.edu.pl.pk_wfmi_schedule_notificator.task;


import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.adapter.ScheduleAdapter;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.TimetableManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

public class UpdateFileAsyncTask extends AsyncTask<Void, Void, List<Timetable>> {
    private static final String TAG = "UpdateFileAsyncTask";
    private ScheduleAdapter adapter;
    private TimetableManager timetableManager;


    public UpdateFileAsyncTask(Storage storage, ScheduleAdapter adapter) {
        this.adapter = adapter;
        timetableManager = new TimetableManager(storage);
    }

    @Override
    protected List<Timetable> doInBackground(Void... voids) {
        try {
            return timetableManager.fetchNewest();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Timetable> timetables) {
        super.onPostExecute(timetables);
        if (timetables != null) {
            Log.d(TAG, "Updating current timetable list");
            adapter.setTimetables(timetables);
        }
    }
}
