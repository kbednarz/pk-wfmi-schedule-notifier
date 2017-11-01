package pk.edu.pl.pk_wfmi_schedule_notificator.web;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HtmlParserTest {
    String url = "http://www.fmi.pk.edu.pl/?page=rozklady_zajec.php&nc";







    public void fetchXlsFiles() throws Exception {
        String xls = null;
        try{
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");
            for(Element link : links)
            {
                String name=link.toString();
                String name2;
                if(name.indexOf("NIESTACJONARNE")!=-1)
                {
                    name2=name.substring(name.indexOf("\"")+1);
                    xls = (name2.substring(0, name2.indexOf("xls")+3));
                }
                else
                    xls=null;
            }
        }catch(IOException exception){
            exception.printStackTrace();
        }
    }
}