package pk.edu.pl.pk_wfmi_schedule_notificator.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.snappydb.SnappydbException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.AlarmManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.task.UpdateFileAsyncTask;

public class FileListActivity extends Activity {
    private Logger log = LoggerFactory.getLogger(FileListActivity.class);

    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_list_files);

            ListView filesView = findViewById(R.id.filesView);

            ArrayAdapter<Timetable> arrayAdapter = new ArrayAdapter<>(this, R.layout.listview_row, new ArrayList<Timetable>());
            filesView.setAdapter(arrayAdapter);
            filesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Timetable timetable = (Timetable) adapterView.getAdapter().getItem(i);

                    Intent openXlsIntent = new Intent(Intent.ACTION_VIEW);
                    openXlsIntent.setData(Uri.parse(timetable.getUrl()));

                    startActivity(openXlsIntent);
                    return false;
                }
            });

            storage = new Storage(getApplicationContext());

            UpdateFileAsyncTask updateFileAsyncTask = new UpdateFileAsyncTask(storage, arrayAdapter);
            updateFileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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