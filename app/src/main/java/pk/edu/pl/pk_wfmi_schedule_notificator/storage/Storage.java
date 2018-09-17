package pk.edu.pl.pk_wfmi_schedule_notificator.storage;


import android.content.Context;
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

public class Storage {
    private static final String TAG = "Storage";
    private static final String DB_TIMETABLE_KEY = "timetable";

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

    public Timetable readTimetable() throws SnappydbException {
        Log.d(TAG, "Reading timetable");

        if (db.exists(DB_TIMETABLE_KEY)) {
            return db.getObject(DB_TIMETABLE_KEY, Timetable.class);
        } else {
            Log.d(TAG, "Storage is empty");
            return null;
        }
    }

    public void saveTimetable(Timetable timetable) throws SnappydbException {
        Log.d(TAG, "Saving timetable");
        db.put("timetable", timetable);
    }

}
