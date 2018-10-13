package pk.edu.pl.pk_wfmi_schedule_notificator.manager;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.snappydb.SnappydbException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

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

                try (FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
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
}
