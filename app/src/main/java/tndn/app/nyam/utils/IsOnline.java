package tndn.app.nyam.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by YounDitt on 2015-10-17.
 */
public class IsOnline {
    public boolean onlineCheck(Context context){
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(mobile ==null || wifi == null){
            return true;
        }
        if (mobile.isConnected() || wifi.isConnected()) {
            // WIFI, 3G 어느곳에도 연결되지 않았을때
            return true;
        } else {
            return false;
        }
    }

}
