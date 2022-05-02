package terminal_heat_sink.bringbacknfcquicksettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class NfcQuickTile extends TileService {

    private String app_first_run = "terminal_heat_sink.bringbacknfcquicksettings.app_first_run_shared_prefs_key";

    @Override
    public void onClick() {
        super.onClick();

        Tile tile = getQsTile();

        android.nfc.NfcAdapter mNfcAdapter= android.nfc.NfcAdapter.getDefaultAdapter(getApplicationContext());
        boolean enabled = mNfcAdapter.isEnabled();

        if(enabled){
            SystemWriter.turn_on(false,getApplicationContext());
            tile.setState(Tile.STATE_INACTIVE);
            tile.setLabel("NFC");
            tile.setSubtitle("Off");
        }else{
            SystemWriter.turn_on(true,getApplicationContext());
            tile.setState(Tile.STATE_ACTIVE);
            tile.setLabel("NFC");
            tile.setSubtitle("On");
        }
        tile.updateTile();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                "terminal_heat_sink.bringbacknfcquicksettings", Context.MODE_PRIVATE);

        boolean launched_first_time = prefs.getBoolean(app_first_run,false);
        if(!launched_first_time){
            prefs.edit().putBoolean(app_first_run, true).apply();
            SystemWriter.turn_off_magisk_notifications(getApplicationContext());
        }

    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        update_tile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        update_tile();

    }

    private void update_tile(){
        android.nfc.NfcAdapter mNfcAdapter= android.nfc.NfcAdapter.getDefaultAdapter(getApplicationContext());
        boolean enabled = mNfcAdapter.isEnabled();
        Tile tile = getQsTile();

        if(enabled){
            tile.setState(Tile.STATE_ACTIVE);
            tile.setLabel("NFC");
            tile.setSubtitle("On");
        }else{
            tile.setState(Tile.STATE_INACTIVE);
            tile.setLabel("NFC");
            tile.setSubtitle("Off");
        }
        tile.updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

}
