package pk.edu.pl.pk_wfmi_schedule_notificator.task;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.adapter.ScheduleAdapter;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.TimetableManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

public class UpdateFileAsyncTask extends AsyncTask<Void, Void, List<Timetable>> {
    private static final String TAG = "UpdateFileAsyncTask";
    private ScheduleAdapter adapter;
    private TimetableManager timetableManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context context;

    public UpdateFileAsyncTask(Storage storage, ScheduleAdapter adapter, SwipeRefreshLayout
            mSwipeRefreshLayout, Context context) throws IOException {
        this.adapter = adapter;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.context = context;

        timetableManager = new TimetableManager(storage, context);
    }

    @Override
    protected List<Timetable> doInBackground(Void... voids) {
        try {
            return timetableManager.fetchNewest();
        } catch (UnknownHostException e) {
            Log.e(TAG, e.getMessage(), e);
            Toast.makeText(context, "Network error", Toast.LENGTH_LONG).show();
            return null;
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
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
