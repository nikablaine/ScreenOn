package kraflapps.org.screenon;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * @author Veronika Rodionova nika.blaine@gmail.com
 */

public class WakefulReceiver extends WakefulBroadcastReceiver {

    public static final String LOG_TAG = WakefulReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        // Start the service, keeping the device awake while the service is
        // launching. This is the Intent to deliver to the service.
        Log.d(LOG_TAG, "Received a broadcast with intent: " + intent);
        Intent service = new Intent(context, ScreenOnService.class);
        startWakefulService(context, service);
    }
}
