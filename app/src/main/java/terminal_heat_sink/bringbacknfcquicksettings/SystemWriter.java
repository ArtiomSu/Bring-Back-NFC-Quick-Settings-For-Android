package terminal_heat_sink.bringbacknfcquicksettings;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.io.DataOutputStream;
import java.io.IOException;

public class SystemWriter {

    private static void write_to_sys(String command, Context context){
        Process p;
        try {
            // Preform su to get root privledges
            p = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes(command);
            // Close the terminal
            os.writeBytes("exit\n");
            os.flush();
            try {
                p.waitFor();
                if (p.exitValue() != 255) {

                    if(p.exitValue() == 0){
                        Log.i("SystemWriter","wrote successfully "+command);

                    }else{
                        Log.i("SystemWriter","failed to write");
                        Toast toast = Toast.makeText(context, "Could not write please allow AsusRogPhone2RGB root access in magisk", Toast.LENGTH_LONG);
                        toast.show();
                    }



                }
                else {
                    Log.i("SystemWriter","not rooted 1");
                    Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (InterruptedException e) {
                Log.i("SystemWriter","not rooted 2");
                Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (IOException e) {
            Log.i("SystemWriter","not rooted 3");
            Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static void turn_on(boolean on, Context context){
        if(on){
            write_to_sys("svc nfc enable \n",context);
        }else{
            write_to_sys("svc nfc disable \n",context);
        }
    }


    public static void turn_off_magisk_notifications(Context context){
        write_to_sys("magisk --sqlite \"UPDATE policies SET notification = 0 WHERE package_name LIKE 'terminal_heat_sink.bringbacknfcquicksettings'\" \n",context);
    }


}
