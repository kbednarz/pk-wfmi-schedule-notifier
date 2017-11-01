package pk.edu.pl.pk_wfmi_schedule_notificator.domain;


import java.io.Serializable;
import java.util.Date;

public class Timetable implements Serializable {
    private String fileName;
    private String url;
    private Date lastUpdate;

    public Timetable(String fileName, String url, Date lastUpdate) {
        this.fileName = fileName;
        this.url = url;
        this.lastUpdate = lastUpdate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Timetable timetable = (Timetable) o;

        if (!fileName.equals(timetable.fileName)) return false;
        if (!url.equals(timetable.url)) return false;
        return lastUpdate.equals(timetable.lastUpdate);
    }

    @Override
    public int hashCode() {
        int result = fileName.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + lastUpdate.hashCode();
        return result;
    }
}
