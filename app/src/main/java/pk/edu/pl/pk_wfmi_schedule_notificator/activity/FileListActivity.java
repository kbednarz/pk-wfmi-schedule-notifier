package pk.edu.pl.pk_wfmi_schedule_notificator.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class FileListActivity extends Activity {
    private Logger log = LoggerFactory.getLogger(FileListActivity.class);

    private Storage storage;
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_list_files);

            storage = new Storage(getApplicationContext());

            recyclerView = findViewById(R.id.recycler_view);
            adapter = new ScheduleAdapter(storage.readTimetable());

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


            // check updates
//            UpdateFileAsyncTask updateFileAsyncTask = new UpdateFileAsyncTask(storage, filesAdapter);
//            updateFileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
