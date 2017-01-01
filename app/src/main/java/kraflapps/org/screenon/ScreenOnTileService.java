package kraflapps.org.screenon;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import static kraflapps.org.screenon.Consts.KILL_SERVICE;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ScreenOnTileService extends TileService {

    private static final String LOG_TAG = ScreenOnTileService.class.getSimpleName();

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
            stopBackgroundService();
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
            startBackgroundService();
        }
    }

    private void startBackgroundService() {
        Log.d(LOG_TAG, "Starting background service..");
        Intent intent = new Intent(this, ScreenOnService.class);
        startService(intent);
    }

    private void stopBackgroundService() {
        Log.d(LOG_TAG, "Stopping background service..");
        Intent intent = new Intent(this, ScreenOnService.class);
        intent.putExtra(KILL_SERVICE, true);
        startService(intent);
    }
}
