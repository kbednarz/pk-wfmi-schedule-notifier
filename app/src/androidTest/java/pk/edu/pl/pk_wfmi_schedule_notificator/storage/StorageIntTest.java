package pk.edu.pl.pk_wfmi_schedule_notificator.storage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
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
        assertEquals(0, storage.db.countKeys("timetable"));

        // when
        storage.saveTimetable(timetable);

        // then
        assertEquals(1, storage.db.countKeys("timetable"));

        // when
        Timetable readTimetable = storage.readTimetable();

        // then
        assertEquals(timetable, readTimetable);
    }


}