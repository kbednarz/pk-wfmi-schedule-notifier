package pk.edu.pl.pk_wfmi_schedule_notificator.web;


import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Looks for XML files on given page
 */
public class HtmlParser extends AsyncTask<Void, Void, String> {
    private static final Logger logger = LoggerFactory.getLogger(HtmlParser.class);

    private String url;
    private ArrayAdapter<String> arrayAdapter;

    public HtmlParser(String url, ArrayAdapter<String> arrayAdapter) {
        this.url = url;
        this.arrayAdapter = arrayAdapter;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            logger.debug("Checking for new schedule");
            Document doc = Jsoup.connect(url).timeout(1000 * 10).get();

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String name = link.toString();
                String name2;
                if (name.indexOf("NIESTACJONARNE") != -1) {
                    name2 = name.substring(0, name.indexOf("xls") + 3);
                    name2 = name2.substring(name2.lastIndexOf("/") + 1);

                    return name2;
                }
            }

        } catch (IOException e) {
            logger.error("Network error",e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            logger.debug("Overriding current schedule list with name: {}", s);

            arrayAdapter.clear();
            arrayAdapter.add(s);
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
