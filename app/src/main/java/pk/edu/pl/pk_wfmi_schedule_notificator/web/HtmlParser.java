package pk.edu.pl.pk_wfmi_schedule_notificator.web;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

public class HtmlParser {
    private String pageUrl;

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
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String name = link.toString();
            if (name.contains("NIESTACJONARNE")) {
                return link;
            }
        }
        throw new Exception("XLS file not found in DOM");
    }

    public Timetable fetchTimetable() throws Exception {
        Element link = fetchXlsFile();
        String url = link.attr("href").replaceAll(" ", "%20");

        Timetable timetable = new Timetable();
        timetable.setFileName(getFilename(link.toString()));
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
