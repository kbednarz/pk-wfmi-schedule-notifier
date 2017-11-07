package pk.edu.pl.pk_wfmi_schedule_notificator.web;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {

    /**
     * Looks for XML files on given page
     *
     * @param pageUrl page address to download and parse a html
     * @return list of found files
     */
    public String fetchXlsFiles(String pageUrl) throws Exception {
        Document doc = Jsoup.connect(pageUrl).timeout(1000 * 10).get();
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String name = link.toString();
            String name2;
            if (name.contains("NIESTACJONARNE")) {
                name2 = name.substring(0, name.indexOf("xls") + 3);
                name2 = name2.substring(name2.lastIndexOf("/") + 1);
                return name2;
            }
        }
        throw new Exception("XLS file not found in DOM");
    }
}
