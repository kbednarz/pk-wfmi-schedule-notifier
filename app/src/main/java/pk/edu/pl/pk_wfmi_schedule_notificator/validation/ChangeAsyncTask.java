package pk.edu.pl.pk_wfmi_schedule_notificator.validation;


import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.TimetableManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

public class ChangeAsyncTask extends AsyncTask<Void, Void, Timetable> {
    private static final Logger logger = LoggerFactory.getLogger(ChangeAsyncTask.class);
    private ArrayAdapter<String> arrayAdapter;
    private TimetableManager timetableManager;


    public ChangeAsyncTask(Storage storage, ArrayAdapter<String> arrayAdapter) {
        this.arrayAdapter = arrayAdapter;
        timetableManager = new TimetableManager(storage);
    }

    @Override
    protected Timetable doInBackground(Void... voids) {
        try {
            return timetableManager.fetchTimetable();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Timetable t) {
        super.onPostExecute(t);
        if (t != null) {
            logger.debug("Overriding current schedule list with name: {}", t.getFileName());

            arrayAdapter.clear();
            arrayAdapter.add(t.getFileName());
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
