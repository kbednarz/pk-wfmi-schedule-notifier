package pk.edu.pl.pk_wfmi_schedule_notificator.storage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.collect.EvictingQueue;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.Queue;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class StorageIntTest {
    private Storage storage;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        storage = new Storage(context);
    }

    @Test
    public void saveAndReadTimetable() throws Exception {
        // given
        Timetable timetable = new Timetable("fn", "url", new Date());
        Queue<Timetable> timetableQueue = EvictingQueue.create(5);
        timetableQueue.add(timetable);

        // prepare db
        if (storage.db.countKeys("timetable") != 0) {
            storage.db.del("timetable");
        }

        // when read timetable when none is saved
        Queue<Timetable> readTimetableQueue = storage.readTimetableQueue();

        // then should return empty array
        assertNotNull(readTimetableQueue);
        assertEquals(0, readTimetableQueue.size());

        // when save timetable
        storage.saveTimetableQueue(timetableQueue);

        // then
        assertEquals(1, storage.db.countKeys("timetable"));

        // when read just saved timetables
        readTimetableQueue = storage.readTimetableQueue();

        // then
        assertArrayEquals(timetableQueue.toArray(), readTimetableQueue.toArray());
    }


}