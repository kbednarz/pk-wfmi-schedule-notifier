package pk.edu.pl.pk_wfmi_schedule_notificator.web;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HtmlParser {
    private final static String url = "http://www.fmi.pk.edu.pl/?page=rozklady_zajec.php&nc";

    /**
     * Looks for XML files on given page
     *
     * @param pageUrl page address to download and parse a html
     * @return list of found files
     */
    public String fetchXlsFiles(String pageUrl) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String name = link.toString();
            String name2;
            if (name.indexOf("NIESTACJONARNE") != -1) {
                name2 = name.substring(name.indexOf("\"") + 1);
                return (name2.substring(0, name2.indexOf("xls") + 3));
            }

        }
        return null;
    }
}
