package pk.edu.pl.pk_wfmi_schedule_notificator.storage;


import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

public class Storage {
    protected final File storageFile;

    public Storage(Context context) {
        storageFile = new File(context.getFilesDir(), "STORAGE.tmp");
    }

    public void saveTimetable(Timetable timetable) throws IOException {
        if (!storageFile.exists()) {
            storageFile.createNewFile();
        }


        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(storageFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(timetable);
        } finally {
            if (oos != null) oos.close();
            if (fos != null) fos.close();
        }

    }

    public Timetable readTimetable() throws IOException, ClassNotFoundException {
        if (storageFile.exists()) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                fis = new FileInputStream(storageFile);
                ois = new ObjectInputStream(fis);

                return (Timetable) ois.readObject();
            } finally {
                if (ois != null) ois.close();
                if (fis != null) fis.close();
            }
        } else {
            return null;
        }
    }
}
