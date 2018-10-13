package pk.edu.pl.pk_wfmi_schedule_notificator.web;

import org.junit.Test;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

import static org.junit.Assert.assertNotNull;

public class HtmlParserTest {
    private String URL = "https://fmi.pk.edu.pl/?page=rz";
    private String CSS_SELECTOR = "p:contains(STUDIA NIESTACJONARNE) ~ * a";


    @Test
    public void fetchXlsFilesTest() throws Exception {
        HtmlParser htmlParser = new HtmlParser(URL, CSS_SELECTOR);

        Timetable result = htmlParser.fetchTimetable();

        assertNotNull(result);
    }

}