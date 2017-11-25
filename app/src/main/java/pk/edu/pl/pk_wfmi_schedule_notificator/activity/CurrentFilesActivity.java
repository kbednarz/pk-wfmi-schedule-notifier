package pk.edu.pl.pk_wfmi_schedule_notificator.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.snappydb.SnappydbException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.AlarmHandler;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.validation.ChangeAsyncTask;

public class CurrentFilesActivity extends Activity {
    private Logger log = LoggerFactory.getLogger(CurrentFilesActivity.class);

    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_current_files);

            ListView filesView = findViewById(R.id.filesView);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.listview_row, new ArrayList<String>());
            filesView.setAdapter(arrayAdapter);

            storage = new Storage(getApplicationContext());

            ChangeAsyncTask changeAsyncTask = new ChangeAsyncTask(storage, arrayAdapter);
            changeAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            AlarmHandler alarmHandler = new AlarmHandler(getApplicationContext());
            alarmHandler.startBackgroundService();
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
