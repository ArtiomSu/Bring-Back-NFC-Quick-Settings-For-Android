package terminal_heat_sink.bringbacknfcquicksettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class NfcQuickTile extends TileService {
    String app_rooted = "terminal_heat_sink.bringbacknfcquicksettings.app_rooted_shared_prefs_key";
    @Override
    public void onClick() {
        super.onClick();

        Tile tile = getQsTile();

        android.nfc.NfcAdapter mNfcAdapter= android.nfc.NfcAdapter.getDefaultAdapter(getApplicationContext());

        boolean rooted = false;
        if(mNfcAdapter == null){
            tile.setState(Tile.STATE_UNAVAILABLE);
            tile.setLabel("NO NFC");
        }else{
            if(mNfcAdapter.isEnabled()){
                boolean success = SystemWriter.turn_on(false,getApplicationContext());
                if(!success){
                    tile.setState(Tile.STATE_INACTIVE);
                    tile.setLabel("NOT ROOTED!");
                }else {
                    rooted = true;
                    tile.setState(Tile.STATE_INACTIVE);
                    tile.setLabel("NFC");
                    tile.setSubtitle("Off");
                }
            }else{
                boolean success = SystemWriter.turn_on(true,getApplicationContext());
                if(!success){
                    tile.setState(Tile.STATE_INACTIVE);
                    tile.setLabel("NOT ROOTED!");
                }else {
                    rooted = true;
                    tile.setState(Tile.STATE_ACTIVE);
                    tile.setLabel("NFC");
                    tile.setSubtitle("On");
                }
            }
        }


        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                "terminal_heat_sink.bringbacknfcquicksettings", Context.MODE_PRIVATE);

        String app_first_run = "terminal_heat_sink.bringbacknfcquicksettings.app_first_run_shared_prefs_key";
        boolean launched_first_time = prefs.getBoolean(app_first_run,false);
        prefs.edit().putBoolean(app_rooted, rooted).apply();
        if(!launched_first_time){
            prefs.edit().putBoolean(app_first_run, true).apply();
            boolean success = SystemWriter.turn_off_magisk_notifications(getApplicationContext());
            if(!success){
                tile.setState(Tile.STATE_INACTIVE);
                tile.setLabel("NOT ROOTED!");
            }
        }
        tile.updateTile();
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
        Tile tile = getQsTile();
        if(mNfcAdapter == null){
            tile.setState(Tile.STATE_UNAVAILABLE);
            tile.setLabel("NO NFC");
        }else {
            SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                    "terminal_heat_sink.bringbacknfcquicksettings", Context.MODE_PRIVATE);
            if (mNfcAdapter.isEnabled()) {
                tile.setState(Tile.STATE_ACTIVE);
                tile.setLabel("NFC");
                tile.setSubtitle("On");
            } else {
                tile.setState(Tile.STATE_INACTIVE);
                tile.setLabel("NFC");
                tile.setSubtitle("Off");
            }
            boolean rooted = prefs.getBoolean(app_rooted, false);
            if(!rooted){
                tile.setState(Tile.STATE_INACTIVE);
                tile.setLabel("NOT ROOTED!");
            }
        }
        tile.updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

}
