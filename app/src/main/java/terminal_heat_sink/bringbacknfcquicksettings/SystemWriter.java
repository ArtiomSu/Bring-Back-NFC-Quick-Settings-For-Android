package terminal_heat_sink.bringbacknfcquicksettings;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.io.DataOutputStream;
import java.io.IOException;

public class SystemWriter {

    private static boolean write_to_sys(String command, Context context){
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
                        return false;
                    }



                }
                else {
                    Log.i("SystemWriter","not rooted 1");
                    Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                    toast.show();
                    return false;
                }
            } catch (InterruptedException e) {
                Log.i("SystemWriter","not rooted 2");
                Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
        } catch (IOException e) {
            Log.i("SystemWriter","not rooted 3");
            Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return  true;
    }

    public static boolean turn_on(boolean on, Context context){
        if(on){
            return write_to_sys("svc nfc enable \n",context);
        }else{
            return write_to_sys("svc nfc disable \n",context);
        }
    }


    public static boolean turn_off_magisk_notifications(Context context){
        return write_to_sys("magisk --sqlite \"UPDATE policies SET notification = 0 WHERE package_name LIKE 'terminal_heat_sink.bringbacknfcquicksettings'\" \n",context);
    }


}
