package pk.edu.pl.pk_wfmi_schedule_notificator.storage;


import android.content.Context;

import com.google.common.collect.EvictingQueue;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;

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

    public void saveTimetable(Queue<Timetable> timetable) throws SnappydbException {
        logger.trace("Saving timetables");

        db.put("timetable", timetable);
    }

    public Queue<Timetable> readTimetable() throws SnappydbException {
        logger.trace("Reading timetables");

        if (db.exists("timetable"))
            return db.get("timetable", EvictingQueue.class);
        else return EvictingQueue.create(5);
    }
}
