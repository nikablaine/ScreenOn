package kraflapps.org.screenon;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.PowerManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import static kraflapps.org.screenon.Consts.KEEP_ON_KEY;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ScreenOnTileService extends TileService {

    private static final String LOG_TAG = ScreenOnTileService.class.getSimpleName();
    private PowerManager.WakeLock mWakeLock;

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "Destroying..");
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        changeToInactive();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        changeToInactive();
    }

    @Override
    public void onClick() {
        super.onClick();
        changeState(getState());
    }

    int getState() {
        Tile tile = getQsTile();
        return tile == null ? Tile.STATE_UNAVAILABLE : tile.getState();
    }

    void changeState(int state) {
        switch (state) {
            case Tile.STATE_ACTIVE:
                changeToInactive();
                break;
            case Tile.STATE_INACTIVE:
                changeToActive();
                break;
            default:
                break;
        }
    }

    private void changeToInactive() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setIcon(Icon.createWithResource(this,
                    R.drawable.ic_screen_off));
            tile.setLabel(getString(R.string.screen_off));
            tile.setContentDescription(
                    getString(R.string.on_description));
            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
            releaseWakeLock();
        }
    }

    private void changeToActive() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setIcon(Icon.createWithResource(this,
                    R.drawable.ic_screen_on));
            tile.setLabel(getString(R.string.screen_on));
            tile.setContentDescription(
                    getString(R.string.off_description));
            tile.setState(Tile.STATE_ACTIVE);
            tile.updateTile();
            acquireWakeLock();
        }
    }

    private void acquireWakeLock() {
        /*Log.d(LOG_TAG, "Acquiring wake lock..");
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                "ScreenOnWakelockTag");
        mWakeLock.acquire();
        Log.d(LOG_TAG, "Lock acquired: " + mWakeLock);*/
        Log.d(LOG_TAG, "Sending broadcast..");
        Intent intent = new Intent(this, WakefulReceiver.class);
        intent.putExtra(KEEP_ON_KEY, true);
        sendBroadcast(intent);
    }

    private void releaseWakeLock() {
        /*if (mWakeLock != null) {
            Log.d(LOG_TAG, "Releasing wake lock: " + mWakeLock);
            mWakeLock.release();
        }*/
        Intent intent = new Intent(this, WakefulReceiver.class);
        intent.putExtra(KEEP_ON_KEY, false);
        sendBroadcast(intent);
    }
}
