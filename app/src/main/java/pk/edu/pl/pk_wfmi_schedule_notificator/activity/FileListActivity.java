package pk.edu.pl.pk_wfmi_schedule_notificator.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.snappydb.SnappydbException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.AlarmManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.task.UpdateFileAsyncTask;

public class FileListActivity extends Activity {
    private Logger log = LoggerFactory.getLogger(FileListActivity.class);

    private Storage storage;
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private List<Timetable> timetables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_list_files);

            storage = new Storage(getApplicationContext());
            timetables = storage.readTimetable();

            recyclerView = findViewById(R.id.recycler_view);
            adapter = new ScheduleAdapter(timetables);

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


            // check updates
            UpdateFileAsyncTask updateFileAsyncTask = new UpdateFileAsyncTask(storage, timetables);
            updateFileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            // schedule next checks
            AlarmManager alarmManager = new AlarmManager(getApplicationContext());
            alarmManager.startBackgroundService();
        } catch (Exception e) {
            log.error("Exception in main activity", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            storage.close();
        } catch (SnappydbException e) {
            log.error("Cannot close DB", e);
        }
    }

}
