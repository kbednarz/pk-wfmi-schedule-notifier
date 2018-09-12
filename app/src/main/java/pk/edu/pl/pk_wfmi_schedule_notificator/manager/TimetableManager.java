package pk.edu.pl.pk_wfmi_schedule_notificator.manager;


import android.content.Context;

import com.google.common.collect.Iterables;

import java.io.IOException;
import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.util.Config;
import pk.edu.pl.pk_wfmi_schedule_notificator.web.HtmlParser;

public class TimetableManager {
    private Storage storage;
    private Context context;
    private HtmlParser htmlParser;

    public TimetableManager(Storage storage, Context context) throws IOException {
        this.storage = storage;
        this.context = context;

        htmlParser = new HtmlParser(Config.getProperty("schedule.url", context), Config.getProperty("schedule.keyword", context));
    }

    public List<Timetable> fetchNewest() throws Exception {
        List<Timetable> timetables = storage.readTimetable();
        Timetable fetchedTimetable = htmlParser.fetchTimetable();

        if (hasChanged(timetables, fetchedTimetable)) {
            timetables.add(fetchedTimetable);
            storage.saveTimetable(timetables);

            return timetables;
        }
        return null;
    }

    private boolean hasChanged(List<Timetable> timetables, Timetable timetableToValidate) {
        Timetable lastTimetable = Iterables.getLast(timetables, null);
        return lastTimetable == null || !timetableToValidate.getFileName().equals(lastTimetable.getFileName());
    }
}
