package pk.edu.pl.pk_wfmi_schedule_notificator.job;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;

import pk.edu.pl.pk_wfmi_schedule_notificator.task.NotificationAsyncTask;

public class NotificationJob extends JobService {
    private static final String TAG = "NotificationJob";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Starting NotificationJob");
        try {
            NotificationAsyncTask notificationAsyncTask = new NotificationAsyncTask
                    (getApplicationContext());
            notificationAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "NotificationJob error occurred", e);
            return false; // todo: check meaning of this status
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        jobFinished(jobParameters, false);
        return true;
    }
}
