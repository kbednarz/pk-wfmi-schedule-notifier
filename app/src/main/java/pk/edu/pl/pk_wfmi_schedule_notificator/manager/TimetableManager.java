package pk.edu.pl.pk_wfmi_schedule_notificator.manager;


import com.google.common.collect.Iterables;

import java.util.Queue;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.web.HtmlParser;

public class TimetableManager {
    private Storage storage;

    public TimetableManager(Storage storage) {
        this.storage = storage;
    }

    public Queue<Timetable> fetchNewest() throws Exception {
        Queue<Timetable> timetables = storage.readTimetableQueue();
        Timetable fetchedTimetable = checkOnSite();

        if (hasChanged(timetables, fetchedTimetable)) {
            timetables.add(fetchedTimetable);
            storage.saveTimetableQueue(timetables);

            return timetables;
        }
        return null;
    }

    private Timetable checkOnSite() throws Exception {
        HtmlParser htmlParser = new HtmlParser("http://www.fmi.pk.edu.pl/?page=rozklady_zajec.php&nc");
        return htmlParser.fetchTimetable();
    }

    private boolean hasChanged(Queue<Timetable> timetables, Timetable timetableToValidate) {
        Timetable lastTimetable = Iterables.getLast(timetables, null);
        return lastTimetable == null || !timetableToValidate.getFileName().equals(lastTimetable.getFileName());
    }
}
