package pk.edu.pl.pk_wfmi_schedule_notificator.validation;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

public class ChangeValidatorTest {
    @InjectMocks
    private ChangeValidator changeValidator;

    @Mock
    private Storage storage;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    // Todo: fix test
   /* @Test
    public void validate() throws Exception {
        // given
        List<String> readList = new LinkedList<>(Arrays.asList("stary1.xls", "stary2.xls"));
        when(storage.readTimetable()).thenReturn(readList);

        List<String> newList = new LinkedList<>(Arrays.asList("stary1.xls", "nowy.xls"));

        // when validate same lists
        List<String> result = changeValidator.validate(readList);

        // then
        assertEquals(result.size(), 0);

        // when new items appeared
        result = changeValidator.validate(newList);

        // then
        List<String> expected = Collections.singletonList("nowy.xls");
        assertThat(result, CoreMatchers.is(expected));
    }*/

}