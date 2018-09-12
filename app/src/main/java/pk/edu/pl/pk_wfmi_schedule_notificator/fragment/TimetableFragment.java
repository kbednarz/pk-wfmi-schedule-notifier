package pk.edu.pl.pk_wfmi_schedule_notificator.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snappydb.SnappydbException;

import java.io.IOException;
import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.AlarmManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.task.UpdateFileAsyncTask;

public class TimetableFragment extends Fragment {
    private static final String TAG = "TimetableFragment";
    private Storage storage;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_timetable, container, false);
            storage = new Storage(getActivity());

            getActivity().registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        System.out.println("@@ received");
                        updateTimetableView();
                    } catch (SnappydbException e) {
                        Log.e(TAG, "Database error", e);
                    }
                }
            }, new IntentFilter(UpdateFileAsyncTask.FILTER));


            updateTimetableView();

            SwipeRefreshLayout mSwipeRefreshLayout = view.findViewById(R.id.fragment_timetable);
            scheduleJobs();

            mSwipeRefreshLayout.setOnRefreshListener(() -> updateSchedule(mSwipeRefreshLayout));
            updateSchedule(mSwipeRefreshLayout);
        } catch (Exception e) {
            Log.e(TAG, "Exception in Timetable fragment", e);
        }
        return view;
    }

    private void updateTimetableView() throws SnappydbException {
        List<Timetable> timetables = storage.readTimetable();
        if (timetables != null && !timetables.isEmpty()) {
            TextView scheduleName = view.findViewById(R.id.scheduleFileNameTextView);
            scheduleName.setText(timetables.get(0).getFileName());
        }
    }

    private void updateSchedule(SwipeRefreshLayout mSwipeRefreshLayout) {
        try {
            mSwipeRefreshLayout.setRefreshing(true);
            UpdateFileAsyncTask updateFileAsyncTask = new UpdateFileAsyncTask(storage, mSwipeRefreshLayout, getActivity());
            updateFileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (IOException e) {
            Log.e(TAG, "Update error", e);
        }
    }

    private void scheduleJobs() {
        // schedule next checks
        AlarmManager alarmManager = new AlarmManager(getActivity());
        alarmManager.startBackgroundService();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().getActionBar().setTitle(R.string.schedules_section);
    }

}
