package pk.edu.pl.pk_wfmi_schedule_notificator.storage;


import android.content.Context;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

public class Storage {
    private static final Logger logger = LoggerFactory.getLogger(Storage.class);

    protected DB db;

    public Storage(Context context) throws SnappydbException {
        db = DBFactory.open(context);
    }

    public void close() throws SnappydbException {
        db.close();
    }

    public void destroy() throws SnappydbException {
        db.destroy();
    }

    public void saveTimetable(Timetable timetable) throws SnappydbException {
        db.put("timetable", timetable);
    }

    public Timetable readTimetable() throws SnappydbException {
        if (db.exists("timetable"))
            return db.get("timetable", Timetable.class);
        else return null;
    }
}
