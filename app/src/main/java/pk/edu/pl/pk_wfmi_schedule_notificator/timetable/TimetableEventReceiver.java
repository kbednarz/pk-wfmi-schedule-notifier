package pk.edu.pl.pk_wfmi_schedule_notificator.timetable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimetableEventReceiver extends BroadcastReceiver {
    public final static String EVENT_FILTER = "TIMETABLE_UPDATE_FILTER";
    public final static String ERROR = "error";
    public final static String ERROR_MESSAGE = "errorMessage";
    public final static String NEWER_APPEARED = "newerAppeared";

    private TimetableFragment fragment;

    public TimetableEventReceiver(TimetableFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean error = intent.getBooleanExtra(ERROR, false);
        if (error) {
            String detail = intent.getStringExtra(ERROR_MESSAGE);
            fragment.onError(detail);
        } else {
            handleSuccess(intent);
        }
    }

    private void handleSuccess(Intent intent) {
        fragment.updateTimetableView();
    }
}
