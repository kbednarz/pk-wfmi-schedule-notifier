package pk.edu.pl.pk_wfmi_schedule_notificator.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "AlarmReceiver", Toast.LENGTH_SHORT).show();
    }
}
