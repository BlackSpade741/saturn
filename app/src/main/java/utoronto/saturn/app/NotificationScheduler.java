package utoronto.saturn.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class NotificationScheduler {
    public static final int DAILY_REMINDER_REQUEST_CODE=100;
    public static final String TAG = "NotificationScheduler";
    private static String eventName;

    /**
     * Set up the notificaiton trigger that would triggered at some specific time.
     */
    public static void setReminder(Context context, Class<?> cls, String event_name, int hour, int min) {
        // Get the current time(hour, min, sec).
        Calendar calendar = Calendar.getInstance();

        // Set the given time(hour, min, sec)
        Calendar setCalendar = Calendar.getInstance();
        setCalendar.set(Calendar.HOUR_OF_DAY, hour);
        setCalendar.set(Calendar.MINUTE, min);
        setCalendar.set(Calendar.SECOND, 0);

        eventName = event_name;

        // If the setting time is already before the current time, then add one more day.
        if(setCalendar.before(calendar))
            setCalendar.add(Calendar.DATE,1);

        // Set up the pendintIntent event to trigger the event.
        // Set up the AlarmManager to alarm the users after some days.
        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * Return  the name of the event.
     */
    public static String getContent() {
        return eventName;
    }
}