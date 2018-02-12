package pk.edu.pl.pk_wfmi_schedule_notificator.storage;


import android.content.Context;

import com.google.common.collect.EvictingQueue;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public List<Timetable> readTimetable() throws SnappydbException {
        List<Timetable> timetables = new ArrayList<>();

        if (db.exists("timetable")) {
            Timetable[] array = db.getObjectArray("timetable", Timetable.class);
            timetables = Arrays.asList(array);
        }

        return timetables;
    }

    public void saveTimetableQueue(Queue<Timetable> timetable) throws SnappydbException {
        logger.trace("Saving timetables");

        db.put("timetable", timetable.toArray());
    }

    public Queue<Timetable> readTimetableQueue() throws SnappydbException {
        logger.trace("Reading timetables");

        Queue<Timetable> timetableQueue = EvictingQueue.create(5);

        if (db.exists("timetable")) {
            Timetable[] timetableArray = db.getObjectArray("timetable", Timetable.class);
            timetableQueue.addAll(Arrays.asList(timetableArray));
        }

        return timetableQueue;
    }
}
