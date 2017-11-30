package pk.edu.pl.pk_wfmi_schedule_notificator.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
            FilesAdapter<Timetable> filesAdapter = prepareAdapter(filesView);

            storage = new Storage(getApplicationContext());
            // update view with values from db
            filesAdapter.update(storage.readTimetableQueue());

            // check updates
            UpdateFileAsyncTask updateFileAsyncTask = new UpdateFileAsyncTask(storage, filesAdapter);
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

    private FilesAdapter<Timetable> prepareAdapter(ListView filesView) {
        FilesAdapter<Timetable> filesAdapter = new FilesAdapter<>(this, R.layout.listview_row, new ArrayList<Timetable>());
        filesView.setAdapter(filesAdapter);

        filesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Timetable timetable = (Timetable) adapterView.getAdapter().getItem(i);

                // show alert
                CharSequence[] items = {"Open URL"};
                AlertDialog.Builder builder = new AlertDialog.Builder(FileListActivity.this);

                builder.setTitle(timetable.getFileName());
                builder.setCancelable(true);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // open file
                        Intent openXlsIntent = new Intent(Intent.ACTION_VIEW);
                        openXlsIntent.setData(Uri.parse(timetable.getUrl()));
                        startActivity(openXlsIntent);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }
        });

        return filesAdapter;
    }
}
