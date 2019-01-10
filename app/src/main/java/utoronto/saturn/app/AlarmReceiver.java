package utoronto.saturn.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {
    }

    /**
     * Associate alarm service with the broadcast receiver, the Alarm service will invoke
     * this receiver on scheduled time.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("label","Alarm Receive:");
        // Set up the receiver to receive the notification to alert the user.
        Intent intent1 = new Intent(context, MyNewIntentService.class);
        context.startService(intent1);
    }
}