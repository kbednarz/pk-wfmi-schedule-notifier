package pk.edu.pl.pk_wfmi_schedule_notificator.manager;


import java.io.IOException;
import java.util.Date;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.web.HtmlParser;

public class TimetableManager {
    private Storage storage;

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

    public boolean hasNewerAppeared() throws Exception {
        Timetable timetable = checkOnSite();

        return hasChanged(timetable);
    }

    private Timetable checkOnSite() throws Exception {
        HtmlParser htmlParser = new HtmlParser();

        String fileName = htmlParser.fetchXlsFiles("http://www.fmi.pk.edu.pl/?page=rozklady_zajec.php&nc");

        return new Timetable(fileName, "", new Date());

    }

    private boolean hasChanged(Timetable itemToValidate) throws IOException, ClassNotFoundException {
        Timetable oldItem = storage.readTimetable();

        return oldItem == null || !itemToValidate.getFileName().equals(oldItem.getFileName());
    }
}
