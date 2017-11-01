package pk.edu.pl.pk_wfmi_schedule_notificator.validation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ChangeValidatorTest {
    @InjectMocks
    private ChangeValidator changeValidator;

    @Mock
    private Storage storage;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validate() throws Exception {
        // given
        Timetable readTimetable = new Timetable("file.xls", "http://pk.pl", new Date());
        when(storage.readTimetable()).thenReturn(readTimetable);
        Timetable fetchedTimetable = new Timetable("file.xls", "http://pk.pl", new Date());

        // when
        boolean result = changeValidator.isNewerVersion(fetchedTimetable);

        // then
        assertFalse(result);

        // when
        fetchedTimetable.setFileName("file_2.xls");
        result = changeValidator.isNewerVersion(fetchedTimetable);

        // then
        assertTrue(result);
    }

}