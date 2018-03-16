package pk.edu.pl.pk_wfmi_schedule_notificator.task;


import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

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
    private Toast errorToast;

    public UpdateFileAsyncTask(Storage storage, ScheduleAdapter adapter, SwipeRefreshLayout
            mSwipeRefreshLayout, Toast errorToast) {
        this.adapter = adapter;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.errorToast = errorToast;
        timetableManager = new TimetableManager(storage);
    }

    @Override
    protected List<Timetable> doInBackground(Void... voids) {
        try {
            return timetableManager.fetchNewest();
        } catch (UnknownHostException e) {
            Log.e(TAG, e.getMessage(), e);
            errorToast.setText("Network error");
            errorToast.show();
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
