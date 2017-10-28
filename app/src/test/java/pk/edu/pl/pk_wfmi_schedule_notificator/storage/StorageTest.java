package pk.edu.pl.pk_wfmi_schedule_notificator.storage;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class StorageTest {
    @Test
    public void saveAndReadList() throws IOException, ClassNotFoundException {
        // Given
        Storage storage = new Storage();
        List<String> savedList = new ArrayList<>();
        savedList.add("test1");
        savedList.add("test2");

        // When
        storage.saveList(savedList);
        List<String> readList = storage.readList();

        // Then
        assertEquals(savedList, readList);
    }


}