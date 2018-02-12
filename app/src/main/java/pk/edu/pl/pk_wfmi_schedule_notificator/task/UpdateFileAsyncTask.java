package pk.edu.pl.pk_wfmi_schedule_notificator.task;


import android.os.AsyncTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import pk.edu.pl.pk_wfmi_schedule_notificator.domain.Timetable;
import pk.edu.pl.pk_wfmi_schedule_notificator.manager.TimetableManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;

public class UpdateFileAsyncTask extends AsyncTask<Void, Void, List<Timetable>> {
    private static final Logger logger = LoggerFactory.getLogger(UpdateFileAsyncTask.class);
    private List<Timetable> timetables;
    private TimetableManager timetableManager;


    public UpdateFileAsyncTask(Storage storage, List<Timetable> timetables) {
        this.timetables = timetables;
        timetableManager = new TimetableManager(storage);
    }

    @Override
    protected List<Timetable> doInBackground(Void... voids) {
        try {
            return timetableManager.fetchNewest();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Timetable> timetables) {
        super.onPostExecute(timetables);
        if (timetables != null) {
            logger.debug("Updating current timetable list");
            this.timetables.clear();
            this.timetables.addAll(timetables);
        }
    }
}
