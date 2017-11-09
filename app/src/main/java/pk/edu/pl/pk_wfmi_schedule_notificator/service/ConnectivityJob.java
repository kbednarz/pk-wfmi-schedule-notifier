package pk.edu.pl.pk_wfmi_schedule_notificator.service;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pk.edu.pl.pk_wfmi_schedule_notificator.manager.TimetableManager;
import pk.edu.pl.pk_wfmi_schedule_notificator.receiver.AlarmReceiver;
import pk.edu.pl.pk_wfmi_schedule_notificator.storage.Storage;
import pk.edu.pl.pk_wfmi_schedule_notificator.validation.NotificationAsyncTask;

public class ConnectivityJob extends JobService {
    private Logger log = LoggerFactory.getLogger(AlarmReceiver.class);

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        log.debug("Starting job");
        Storage storage = new Storage(getApplicationContext());
        TimetableManager timetableManager = new TimetableManager(storage);

        NotificationAsyncTask notificationAsyncTask = new NotificationAsyncTask
                (timetableManager, getApplicationContext());
        notificationAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        jobFinished(jobParameters, false);
        return true;
    }
}
