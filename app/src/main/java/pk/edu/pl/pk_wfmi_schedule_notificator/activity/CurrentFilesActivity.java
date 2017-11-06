package pk.edu.pl.pk_wfmi_schedule_notificator.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.NotificationManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.web.HtmlParser;

public class CurrentFilesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_files);

        ListView filesView = findViewById(R.id.filesView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.listview_row, new ArrayList<String>());
        filesView.setAdapter(arrayAdapter);

        HtmlParser htmlParser = new HtmlParser("http://www.fmi.pk.edu.pl/?page=rozklady_zajec.php&nc", arrayAdapter);
        htmlParser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        NotificationManager notificationManager = new NotificationManager(this);
        notificationManager.startBackgroundService();
    }


}
