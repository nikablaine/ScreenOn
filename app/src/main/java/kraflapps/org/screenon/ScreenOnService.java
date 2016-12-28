package kraflapps.org.screenon;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * @author Veronika Rodionova nika.blaine@gmail.com
 */

public class ScreenOnService extends IntentService {

    public static final String LOG_TAG = ScreenOnService.class.getSimpleName();

    public ScreenOnService() {
        super("ScreenOnService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "Handling intent: " + intent);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "Interrupted: ", e);
        }
        /*if (!intent.getBooleanExtra(Consts.KEEP_ON_KEY, false)) {
            Log.d(LOG_TAG, "Completing wakeful intent..");
            WakefulReceiver.completeWakefulIntent(intent);
        }*/
    }
}
