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

import java.util.Objects;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.AlarmManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.TimetableManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.task.UpdateFileAsyncTask;

public class TimetableFragment extends Fragment {
    private static final String TAG = "TimetableFragment";
    private TimetableManager timetableManager;
    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_timetable, container, false);
            timetableManager = new TimetableManager(getActivity());
            mSwipeRefreshLayout = view.findViewById(R.id.fragment_timetable);
            mSwipeRefreshLayout.setOnRefreshListener(this::updateSchedule);

            getActivity().registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    boolean error = intent.getBooleanExtra("error", false);
                    if (error) {
                        String detail = intent.getStringExtra("errorMsg");
                        onError(detail);
                    } else {
                        if (intent.getBooleanExtra("isNewerAppeared", false)) {
                            updateTimetableView();
                        } else {
                            onFinish();
                        }
                    }
                }
            }, new IntentFilter(UpdateFileAsyncTask.FILTER));

            updateTimetableView();
            updateSchedule();

            scheduleNextCheck();
        } catch (Exception e) {
            Log.e(TAG, "Exception in Timetable fragment", e);
            onError(e.getMessage());
        }
        return view;
    }

    private void updateTimetableView() {
        try {
            Timetable timetable = timetableManager.getLatest();

            if (timetable != null) {
                TextView scheduleName = view.findViewById(R.id.scheduleFileNameTextView);
                scheduleName.setText(timetable.getFileName());

                TextView lastUpdate = view.findViewById(R.id.lastUpdateDateTextView);
                lastUpdate.setText(timetable.getLastUpdate().toString());
            }
        } catch (SnappydbException e) {
            Log.e(TAG, "Database error", e);
            onError("Can't update");
        } finally {
            onFinish();
        }
    }

    private void onFinish() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void onError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void updateSchedule() {
        mSwipeRefreshLayout.setRefreshing(true);
        UpdateFileAsyncTask updateFileAsyncTask = new UpdateFileAsyncTask(timetableManager, getActivity());
        updateFileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
