package pk.edu.pl.pk_wfmi_schedule_notificator.manager;


import com.google.common.collect.Iterables;
import com.snappydb.SnappydbException;

import java.util.Queue;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.web.HtmlParser;

public class TimetableManager {
    private Storage storage;
    private boolean newAppeared = false;
    private Queue<Timetable> timetables;

    public TimetableManager(Storage storage) {
        this.storage = storage;
    }

    public Queue<Timetable> fetchTimetable() throws Exception {
        Timetable timetable = checkOnSite();

        if (hasChanged(timetable)) {
            timetables.add(timetable);
            storage.saveTimetable(timetables);
        }
        return timetables;
    }

    public boolean isNewAppeared() {
        return newAppeared;
    }

    private Timetable checkOnSite() throws Exception {
        HtmlParser htmlParser = new HtmlParser("http://www.fmi.pk.edu.pl/?page=rozklady_zajec.php&nc");
        return htmlParser.fetchTimetable();
    }

    private boolean hasChanged(Timetable timetableToValidate) throws SnappydbException {
        timetables = storage.readTimetable();

        Timetable lastTimetable = Iterables.getLast(timetables, null);
        newAppeared = lastTimetable == null || !timetableToValidate.getFileName().equals(lastTimetable.getFileName());

        return newAppeared;
    }
}
