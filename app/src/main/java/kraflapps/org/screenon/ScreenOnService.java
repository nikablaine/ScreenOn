package kraflapps.org.screenon;

import android.app.IntentService;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.SCREEN_BRIGHT_WAKE_LOCK;

/**
 * @author Veronika Rodionova nika.blaine@gmail.com
 */

public class ScreenOnService extends IntentService {

    public static final String LOG_TAG = ScreenOnService.class.getSimpleName();
    private PowerManager.WakeLock mWakeLock;

    public ScreenOnService() {
        super("ScreenOnService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra(Consts.KILL_SERVICE, false)) {
            releaseWakeLock();
            Log.d(LOG_TAG, "Starting non-sticky");
            return START_NOT_STICKY;
        } else {
            acquireWakeLock();
            Log.d(LOG_TAG, "Starting sticky");
            return START_STICKY;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Destroying ScreenOnService");
        releaseWakeLock();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    private void acquireWakeLock() {
        Log.d(LOG_TAG, "Acquiring wake lock..");
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(ACQUIRE_CAUSES_WAKEUP | SCREEN_BRIGHT_WAKE_LOCK,
                "ScreenOnWakelockTag");
        mWakeLock.acquire();
        Log.d(LOG_TAG, "Lock acquired: " + mWakeLock);
    }

    private void releaseWakeLock() {
        if (mWakeLock != null) {
            Log.d(LOG_TAG, "Releasing wake lock: " + mWakeLock);
            mWakeLock.release();
        } else {
            Log.d(LOG_TAG, "Wake lock not found");
        }
    }
}
