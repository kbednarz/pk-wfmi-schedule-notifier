package pk.edu.pl.pk_wfmi_schedule_notificator.web;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Date;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

public class HtmlParser {
    private final String pageUrl;
    private final String cssSelector;

    public HtmlParser(String pageUrl, String cssSelector) {
        this.pageUrl = pageUrl;
        this.cssSelector = cssSelector;
    }

    /**
     * Looks for XML files on given page
     *
     * @return found element
     */
    private Element findScheduleInDOM(Document doc) throws Exception {
        try {
            return doc.select(cssSelector).first();
        } catch (NullPointerException e) {
            throw new Exception("XLS file not found in DOM");
        }
    }

    public Timetable fetchTimetable() throws Exception {
        Document doc = Jsoup.connect(pageUrl).timeout(1000 * 10).get();
        Element link = findScheduleInDOM(doc);
        String fileUrl = link.attr("href");

        Timetable timetable = new Timetable();
        timetable.setLastUpdate(new Date());
        timetable.setUrl(doc.baseUri() + fileUrl);

        return timetable;
    }

}
