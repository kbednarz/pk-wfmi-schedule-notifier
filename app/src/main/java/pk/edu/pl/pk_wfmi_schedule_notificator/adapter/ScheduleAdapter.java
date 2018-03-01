package pk.edu.pl.pk_wfmi_schedule_notificator.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.R;
import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleHolder> {
    List<Timetable> timetables;

    public ScheduleAdapter(List<Timetable> timetables) {
        this.timetables = timetables;
    }

    @Override
    public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_schedule, parent, false);

        return new ScheduleHolder(itemView, timetables);
    }

    @Override
    public void onBindViewHolder(ScheduleHolder holder, int position) {
        Timetable timetable = timetables.get(position);
        holder.setText(timetable.getFileName());
    }

    @Override
    public int getItemCount() {
        return timetables.size();
    }

    public void setTimetables(List<Timetable> timetables) {
        this.timetables = timetables;
        notifyDataSetChanged();
    }

}
