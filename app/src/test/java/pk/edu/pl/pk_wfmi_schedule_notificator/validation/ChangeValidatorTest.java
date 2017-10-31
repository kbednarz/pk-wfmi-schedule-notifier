package pk.edu.pl.pk_wfmi_schedule_notificator.validation;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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
        List<String> readList = new LinkedList<>(Arrays.asList("stary1.xls", "stary2.xls"));
        when(storage.readList()).thenReturn(readList);

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
    }

}