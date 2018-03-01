package pk.edu.pl.pk_wfmi_schedule_notificator.storage;


import android.content.Context;
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

public class Storage {
    private static final String TAG = "Storage";
    DB db;

    public Storage(Context context) throws SnappydbException {
        db = DBFactory.open(context);
    }

    public void close() throws SnappydbException {
        db.close();
    }

    public void destroy() throws SnappydbException {
        db.destroy();
    }

    public List<Timetable> readTimetable() throws SnappydbException {
        List<Timetable> timetables = new ArrayList<>();
        Log.d(TAG, "Reading timetables");

        if (db.exists("timetable")) {
            Timetable[] array = db.getObjectArray("timetable", Timetable.class);
            timetables = new ArrayList<>(Arrays.asList(array));
        } else {
            Log.d(TAG, "Storage is empty");
        }

        return timetables;
    }

    public void saveTimetable(List<Timetable> timetables) throws SnappydbException {
        Log.d(TAG, "Saving timetables");
        db.put("timetable", timetables.toArray());
    }

}
