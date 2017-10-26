package pk.edu.pl.pk_wfmi_schedule_notificator.web;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlParser {
    /**
     * Looks for XML files on given page
     * @param pageUrl page address to download and parse a html
     * @return list of found files
     */
    public List<String> fetchXlsFiles(String pageUrl){
        // use Jsoup library (https://jsoup.org)
        // stub below
        return new ArrayList<String>(Arrays.asList("plik1.xls","plik2.xls"));
    }
}
