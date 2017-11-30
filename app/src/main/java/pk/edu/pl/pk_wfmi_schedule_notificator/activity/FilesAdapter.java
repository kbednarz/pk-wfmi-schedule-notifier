package pk.edu.pl.pk_wfmi_schedule_notificator.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Queue;


public class FilesAdapter<T> extends ArrayAdapter<T> {
    public FilesAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
    }

    public void update(Queue<T> ts) {
        clear();

        for (T t : ts) {
            add(t);
        }

        notifyDataSetChanged();
    }
}
