package pk.edu.pl.pk_wfmi_schedule_notificator.manager;


import com.snappydb.SnappydbException;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.web.HtmlParser;

public class TimetableManager {
    private Storage storage;
    private boolean newAppeared = false;

    public TimetableManager(Storage storage) {
        this.storage = storage;
    }

    public Timetable fetchTimetable() throws Exception {
        Timetable timetable = checkOnSite();

        if (hasChanged(timetable)) {
            storage.saveTimetable(timetable);
        }
        return timetable;
    }

    public boolean isNewAppeared() {
        return newAppeared;
    }

    private Timetable checkOnSite() throws Exception {
        HtmlParser htmlParser = new HtmlParser("http://www.fmi.pk.edu.pl/?page=rozklady_zajec.php&nc");
        return htmlParser.fetchTimetable();
    }

    private boolean hasChanged(Timetable itemToValidate) throws SnappydbException {
        Timetable oldItem = storage.readTimetable();

        newAppeared = oldItem == null || !itemToValidate.getFileName().equals(oldItem.getFileName());

        return newAppeared;
    }
}
