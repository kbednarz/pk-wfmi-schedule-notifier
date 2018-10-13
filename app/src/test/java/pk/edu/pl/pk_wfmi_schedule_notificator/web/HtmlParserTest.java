package pk.edu.pl.pk_wfmi_schedule_notificator.web;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class HtmlParserTest {
    private String URL = "https://fmi.pk.edu.pl/?page=rz";


    @Test
    public void fetchXlsFilesTest() throws Exception {
        HtmlParser htmlParser = new HtmlParser(URL);

        Timetable result = htmlParser.fetchTimetable();

        assertNotNull(result);
    }

}