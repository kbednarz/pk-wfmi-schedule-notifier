package pk.edu.pl.pk_wfmi_schedule_notificator.job;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pk.edu.pl.pk_wfmi_schedule_notificator.receiver.AlarmReceiver;
import pk.edu.pl.pk_wfmi_schedule_notificator.task.NotificationAsyncTask;

public class NotificationJob extends JobService {
    private Logger log = LoggerFactory.getLogger(AlarmReceiver.class);

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        log.debug("Starting NotificationJob");
        try {
            NotificationAsyncTask notificationAsyncTask = new NotificationAsyncTask
                    (getApplicationContext());
            notificationAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return true;
        } catch (Exception e) {
            log.error("NotificationJob error occurred", e);
            return false; // todo: check meaning of this status
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        jobFinished(jobParameters, false);
        return true;
    }
}
