package pk.edu.pl.pk_wfmi_schedule_notificator.receiver;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pk.edu.pl.pk_wfmi_schedule_notificator.job.NotificationJob;
import pk.edu.pl.pk_wfmi_schedule_notificator.validation.NotificationAsyncTask;

public class AlarmReceiver extends BroadcastReceiver {
    private Logger log = LoggerFactory.getLogger(AlarmReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        log.debug("Receiving alarm broadcast");

        if (isOnline(context)) {
            try {
                log.trace("Device is online. Checking timetable");
                NotificationAsyncTask notificationAsyncTask = new NotificationAsyncTask(context);
                notificationAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
                log.error("AlarmReceiver exception occurred", e);
                scheduleJob(context);
            }
        } else {
            log.trace("Device is offline. Scheduling job");
            scheduleJob(context);
        }
    }

    private void scheduleJob(Context context) {
        log.trace("Scheduling job");

        ComponentName serviceComponent = new ComponentName(context, NotificationJob.class);
        JobInfo.Builder builder = new JobInfo.Builder(10, serviceComponent);
        builder.setMinimumLatency(1000); // wait at least
//        builder.setOverrideDeadline(3 * 1000); // maximum delay
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    private boolean isOnline(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

}
