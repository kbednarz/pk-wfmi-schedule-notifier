package pk.edu.pl.pk_wfmi_schedule_notificator.task;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.TimetableManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

public class UpdateFileAsyncTask extends AsyncTask<Void, Void, Timetable> {
    public final static String FILTER = "TIMETABLE_UPDATE_FILTER";
    private static final String TAG = "UpdateFileAsyncTask";
    private TimetableManager timetableManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context context;

    public UpdateFileAsyncTask(Storage storage, SwipeRefreshLayout
            mSwipeRefreshLayout, Context context) throws IOException {
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.context = context;

        timetableManager = new TimetableManager(storage, context);
    }

    @Override
    protected Timetable doInBackground(Void... voids) {
        try {
            return timetableManager.update();
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
    protected void onPostExecute(Timetable timetables) {
        super.onPostExecute(timetables);
        if (timetables != null) {
            Log.d(TAG, "Updating current timetable list");
//            adapter.setTimetables(timetables);
            Intent intent = new Intent(FILTER);
            context.sendBroadcast(intent);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
