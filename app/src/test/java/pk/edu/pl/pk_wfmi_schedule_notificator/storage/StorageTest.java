package pk.edu.pl.pk_wfmi_schedule_notificator.storage;

import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class StorageTest {
    @Test
    public void saveAndReadList() throws IOException, ClassNotFoundException {
        // Given
        Storage storage = new Storage();
        Timetable savedTimetable = new Timetable("file.xls","http://test", new Date());

        // When file not exists
        if (storage.storageFile.exists()) storage.storageFile.delete();
        Timetable readTimetable = storage.readTimetable();

        // then timetable is null
        assertNull(readTimetable);

        // when save and read
        storage.saveTimetable(savedTimetable);
        readTimetable = storage.readTimetable();

        // Then
        assertEquals(savedTimetable, readTimetable);
    }


}