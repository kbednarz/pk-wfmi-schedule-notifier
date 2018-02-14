package pk.edu.pl.pk_wfmi_schedule_notificator.fragment;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snappydb.SnappydbException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.activity.FileListActivity;
import pk.edu.pl.pk_wfmi_schedule_notificator.activity.ScheduleAdapter;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.AlarmManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.task.UpdateFileAsyncTask;

public class TimetableFragment extends Fragment {
    private Logger log = LoggerFactory.getLogger(FileListActivity.class);

    private Storage storage;
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private List<Timetable> timetables;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.activity_list_files, container, false);
//
//        return rootView;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            // super.onCreate(savedInstanceState);
//            getActivity().setContentView(R.layout.activity_list_files);
            View view = inflater.inflate(R.layout.activity_list_files, container, false);

            storage = new Storage(getActivity());
            timetables = storage.readTimetable();

            recyclerView = view.findViewById(R.id.recycler_view);
            adapter = new ScheduleAdapter(timetables);

            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            manager.setReverseLayout(true);
            recyclerView.setLayoutManager(manager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

            // check updates
            UpdateFileAsyncTask updateFileAsyncTask = new UpdateFileAsyncTask(storage, adapter);
            updateFileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            // schedule next checks
            AlarmManager alarmManager = new AlarmManager(getActivity());
            alarmManager.startBackgroundService();

            return view;
        } catch (Exception e) {
            log.error("Exception in Timetable fragment", e);
            return null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            storage.close();
        } catch (SnappydbException e) {
            log.error("Cannot close DB", e);
        }
    }

}
