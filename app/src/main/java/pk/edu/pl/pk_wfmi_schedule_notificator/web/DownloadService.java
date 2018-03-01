package pk.edu.pl.pk_wfmi_schedule_notificator.web;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

public class DownloadService {
    private Context context;

    public DownloadService(Context context) {
        this.context = context;
    }

    public void downloadSchedule(Timetable timetable) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(timetable.getUrl()));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, timetable.getFileName());
        DownloadManager manager = (DownloadManager) context.getSystemService(Context
                .DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

}
