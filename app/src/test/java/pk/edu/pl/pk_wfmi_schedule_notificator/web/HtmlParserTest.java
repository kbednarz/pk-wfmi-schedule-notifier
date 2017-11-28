package pk.edu.pl.pk_wfmi_schedule_notificator.web;

import org.junit.Test;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

import static org.junit.Assert.assertNotNull;

public class HtmlParserTest {
    @Test
    public void fetchXlsFilesTest() throws Exception {
        HtmlParser htmlParser = new HtmlParser("http://www.fmi.pk.edu.pl/?page=rozklady_zajec.php&nc");

        Timetable result = htmlParser.fetchTimetable();

        assertNotNull(result);
    }

}