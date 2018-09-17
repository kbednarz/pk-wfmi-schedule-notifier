package pk.edu.pl.pk_wfmi_schedule_notificator.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.snappydb.SnappydbException;

import java.io.IOException;
import java.util.Objects;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.AlarmManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.task.UpdateFileAsyncTask;

public class TimetableFragment extends Fragment {
    private static final String TAG = "TimetableFragment";
    private Storage storage;
    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_timetable, container, false);
            storage = new Storage(getActivity());
            mSwipeRefreshLayout = view.findViewById(R.id.fragment_timetable);
            mSwipeRefreshLayout.setOnRefreshListener(() -> updateSchedule(mSwipeRefreshLayout));

            getActivity().registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    updateTimetableView();
                }
            }, new IntentFilter(UpdateFileAsyncTask.FILTER));

            updateTimetableView();
            updateSchedule(mSwipeRefreshLayout);

            scheduleNextCheck();
        } catch (Exception e) {
            Log.e(TAG, "Exception in Timetable fragment", e);
        }
        return view;
    }

    private void updateTimetableView() {
        try {
            Timetable timetable = storage.readTimetable();

            if (timetable != null) {
                TextView scheduleName = view.findViewById(R.id.scheduleFileNameTextView);
                scheduleName.setText(timetable.getFileName());

                TextView lastUpdate = view.findViewById(R.id.lastUpdateDateTextView);
                lastUpdate.setText(timetable.getLastUpdate().toString());
            }

            mSwipeRefreshLayout.setRefreshing(false);
        } catch (SnappydbException e) {
            Log.e(TAG, "Database error", e);
            Toast.makeText(getActivity(), "Can't update", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSchedule(SwipeRefreshLayout mSwipeRefreshLayout) {
        try {
            mSwipeRefreshLayout.setRefreshing(true);
            UpdateFileAsyncTask updateFileAsyncTask = new UpdateFileAsyncTask(storage, mSwipeRefreshLayout, getActivity());
            updateFileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (IOException e) {
            Log.e(TAG, "Update error", e);
            Toast.makeText(getActivity(), "Can't update", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleNextCheck() {
        AlarmManager alarmManager = new AlarmManager(getActivity());
        alarmManager.startBackgroundService();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Objects.requireNonNull(getActivity().getActionBar()).setTitle(R.string.schedules_section);
    }

}
