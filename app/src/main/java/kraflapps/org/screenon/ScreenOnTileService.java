package kraflapps.org.screenon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.preference.PreferenceManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import static kraflapps.org.screenon.Consts.KILL_SERVICE;

/**
 * Tile service responsible for QS tile handling.
 *
 * @author Veronika Rodionova nika.blaine@gmail.com
 */
public class ScreenOnTileService extends TileService {

    private static final String LOG_TAG = ScreenOnTileService.class.getSimpleName();
    private static final String STATE = "ScreenOnState";

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

    @Override
    public void onStartListening() {
        super.onStartListening();
        keepState(getState());
    }

    boolean getState() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean(STATE, false);
    }

    void keepState(boolean state) {
        if (state) {
            changeToActive();
        } else {
            changeToInactive();
        }
    }

    void changeState(boolean state) {
        if (state) {
            changeToInactive();
        } else {
            changeToActive();
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
            inactiveToPreferences();
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
            activeToPreferences();
            startBackgroundService();
        }
    }

    private void activeToPreferences() {
        stateToPreferences(true);
    }

    private void inactiveToPreferences() {
        stateToPreferences(false);
    }

    private void stateToPreferences(boolean state) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(STATE, state);
        editor.apply();
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
