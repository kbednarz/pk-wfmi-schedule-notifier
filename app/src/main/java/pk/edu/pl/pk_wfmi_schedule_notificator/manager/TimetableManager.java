package pk.edu.pl.pk_wfmi_schedule_notificator.manager;


import android.content.Context;

import com.snappydb.SnappydbException;

import java.io.IOException;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.util.Config;
import pk.edu.pl.pk_wfmi_schedule_notificator.web.HtmlParser;

public class TimetableManager {
    private Storage storage;
    private HtmlParser htmlParser;

    public TimetableManager(Context context) throws IOException, SnappydbException {
        this.storage = new Storage(context);

        htmlParser = new HtmlParser(Config.getProperty("schedule.url", context));
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
}
