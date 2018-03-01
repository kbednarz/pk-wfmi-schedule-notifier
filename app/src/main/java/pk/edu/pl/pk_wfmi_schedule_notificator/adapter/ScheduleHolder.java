package pk.edu.pl.pk_wfmi_schedule_notificator.adapter;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.web.DownloadService;

public class ScheduleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    List<Timetable> timetables;
    public TextView title;
    DownloadService downloadService;

    public ScheduleHolder(View view, List<Timetable> timetables) {
        super(view);

        this.title = view.findViewById(R.id.row);
        this.title.setOnClickListener(this);

        this.timetables = timetables;
        this.downloadService = new DownloadService(view.getContext());
    }

    public void setText(String title) {
        this.title.setText(title);
    }

    @Override
    public void onClick(View view) {
        final Timetable timetable = timetables.get(getLayoutPosition());

        // prepare alert
        CharSequence[] items = {"Download"};
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle(timetable.getFileName());
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                downloadService.downloadSchedule(timetable);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}