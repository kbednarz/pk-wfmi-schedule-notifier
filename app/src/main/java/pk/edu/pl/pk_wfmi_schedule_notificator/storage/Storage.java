package pk.edu.pl.pk_wfmi_schedule_notificator.storage;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

public class Storage {
    private static final String PREFS_NAME = "STORAGE.tmp";

    public void saveTimetable(Timetable timetable) throws IOException {
        FileOutputStream fos = new FileOutputStream(PREFS_NAME);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(timetable);
        oos.close();
    }

    public Timetable readTimetable() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(PREFS_NAME);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Timetable timetable = (Timetable) ois.readObject();
        ois.close();

        return timetable;
    }
}
