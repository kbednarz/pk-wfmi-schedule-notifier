package pk.edu.pl.pk_wfmi_schedule_notificator.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snappydb.SnappydbException;

import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.adapter.ScheduleAdapter;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.AlarmManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.task.UpdateFileAsyncTask;

public class TimetableFragment extends Fragment {
    private static final String TAG = "TimetableFragment";
    private Storage storage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_timetable, container, false);

            storage = new Storage(getActivity());
            List<Timetable> timetables = storage.readTimetable();

            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            ScheduleAdapter adapter = new ScheduleAdapter(timetables);

            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            manager.setReverseLayout(true);
            manager.setStackFromEnd(true);
            recyclerView.setLayoutManager(manager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

            scheduleJobs(adapter);
        } catch (Exception e) {
            Log.e(TAG, "Exception in Timetable fragment", e);
        }
        return view;
    }

    private void scheduleJobs(ScheduleAdapter adapter) {
        // check updates
        UpdateFileAsyncTask updateFileAsyncTask = new UpdateFileAsyncTask(storage, adapter);
        updateFileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // schedule next checks
        AlarmManager alarmManager = new AlarmManager(getActivity());
        alarmManager.startBackgroundService();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().getActionBar().setTitle(R.string.schedules_section);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            storage.close();
        } catch (SnappydbException e) {
            Log.e(TAG, "Cannot close DB", e);
        }
    }

}
