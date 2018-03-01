package pk.edu.pl.pk_wfmi_schedule_notificator.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

public class ScheduleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    List<Timetable> timetables;
    public TextView title;

    public ScheduleHolder(View view, List<Timetable> timetables) {
        super(view);
        title = view.findViewById(R.id.row);
        title.setOnClickListener(this);

        this.timetables = timetables;
    }

    public void setText(String title) {
        this.title.setText(title);
    }

    @Override
    public void onClick(View view) {
        Timetable timetable = timetables.get(getLayoutPosition());
        
        Toast.makeText(view.getContext(), "position = " + timetable.getFileName(), Toast.LENGTH_SHORT)
                .show();
    }
}