package pk.edu.pl.pk_wfmi_schedule_notificator.web;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Date;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

public class HtmlParser {
    private final String pageUrl;

    public HtmlParser(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    /**
     * Looks for XML files on given page
     *
     * @return found element
     */
    private Element fetchXlsFile() throws Exception {
        Document doc = Jsoup.connect(pageUrl).timeout(1000 * 10).get();

        try {
            return doc.select("p:contains(STUDIA NIESTACJONARNE)").parents().first().select("li:contains(Informatyka I stopień)").select("a").first();
        } catch (NullPointerException e) {
            throw new Exception("XLS file not found in DOM");
        }
    }

    public Timetable fetchTimetable() throws Exception {
        Element link = fetchXlsFile();
        String url = link.attr("href");

        Timetable timetable = new Timetable();
        timetable.setLastUpdate(new Date());
        timetable.setUrl(url);

        return timetable;
    }

    private String getFilename(String url) {
        String name = url.substring(0, url.indexOf("xls") + 3);
        name = name.substring(name.lastIndexOf("/") + 1);
        return name;
    }

    private byte[] downloadFile(String url) throws IOException {
        Connection.Response response = Jsoup.connect(url).ignoreContentType(true).execute();
        return response.bodyAsBytes();
    }
}
