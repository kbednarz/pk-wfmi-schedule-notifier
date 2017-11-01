package pk.edu.pl.pk_wfmi_schedule_notificator.validation;


import java.io.IOException;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

public class ChangeValidator {
    private Storage storage = new Storage();

    public boolean isNewerVersion(Timetable itemToValidate) throws IOException, ClassNotFoundException {
        Timetable oldItem = storage.readTimetable();

        return !itemToValidate.getFileName().equals(oldItem.getFileName());
    }
}
