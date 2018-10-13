package pk.edu.pl.pk_wfmi_schedule_notificator.manager;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.snappydb.SnappydbException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.timetable.TimetableEventReceiver;
import pk.edu.pl.pk_wfmi_schedule_notificator.util.Config;
import pk.edu.pl.pk_wfmi_schedule_notificator.web.HtmlParser;

import static android.content.ContentValues.TAG;

public class TimetableManager {
    private Storage storage;
    private HtmlParser htmlParser;
    private final String URL;
    private final Context context;

    public TimetableManager(Context context) throws IOException, SnappydbException {
        this.context = context;
        this.storage = new Storage(context);
        URL = Config.getProperty("schedule.url", context);
        htmlParser = new HtmlParser(URL);
    }

    public Timetable getLatest() throws SnappydbException {
        return storage.readTimetable();
    }

    public Timetable update() throws Exception {
        Timetable currentTimetable = storage.readTimetable();
        Timetable fetchedTimetable = htmlParser.fetchTimetable();

        if (hasChanged(currentTimetable, fetchedTimetable)) {
            storage.saveTimetable(fetchedTimetable);

            return fetchedTimetable;
        } else {
            currentTimetable.setLastUpdate(fetchedTimetable.getLastUpdate());
            storage.saveTimetable(currentTimetable);
            return null;
        }
    }

    private boolean hasChanged(Timetable lastTimetable, Timetable timetableToValidate) {
        return lastTimetable == null || !timetableToValidate.getUrl().equals(lastTimetable.getUrl());
    }

    public void downloadFile() {
        new Thread(() -> {
            try {
                Timetable latest = getLatest();
                Connection.Response response = Jsoup.connect(latest.getUrl()).ignoreContentType(true).execute();
                String header = response.header("Content-Disposition");
                String filename = header.substring(header.indexOf("filename=\"") + 10, header.length() - 1);

                File storageDirectory = Environment.getExternalStorageDirectory();
                File scheduleFile = new File(storageDirectory, filename);
                try (FileOutputStream outputStream = new FileOutputStream(scheduleFile)) {
                    outputStream.write(response.bodyAsBytes());
                }

                latest.setFileName(filename);
                storage.saveTimetable(latest);

                Intent intent = new Intent(TimetableEventReceiver.EVENT_FILTER);
                context.sendBroadcast(intent);
            } catch (Exception e) {
                Log.e(TAG, "downloadFile: error", e);
            }
        }).start();
    }

    public void openFile() {
        try {
            Timetable latest = getLatest();
            String fileName = latest.getFileName();
            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            Intent newIntent = new Intent(Intent.ACTION_VIEW);

            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            String mimeType = myMime.getMimeTypeFromExtension(ext);

            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            Uri uri = Uri.fromFile(file);
            newIntent.setDataAndType(uri, mimeType);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }
}
