package tndn.app.nyam.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


/**
 * Created by YounDit on 2016-02-05.
 */
public class UserLog {
    String userfrom;
    String useris;
    String usercode;
    String os = "android";
//    String mVersion = Build.VERSION.RELEASE;


    public String getUserfrom(Context context) {
        userfrom = PreferenceManager.getInstance(context).getUserfrom();
        return userfrom;
    }


    public String getUseris(Context context) {
        useris = PreferenceManager.getInstance(context).getUseris();
        return useris;
    }


    public String getUsercode(Context context) {
        usercode = PreferenceManager.getInstance(context).getUsercode();
        return usercode;
    }

    public String getOs() {
        return os;
    }

    //    public String getLog(Context context){
//        useris = getUseris(context);
//        usercode = getUsercode();
//        os = getOs();
//        return "&useris="+useris+"&usercode="+usercode+"&os="+os;
//    }
    public String getLog(Context context) {
        useris = getUseris(context);
        userfrom = getUserfrom(context);
        usercode = getUsercode(context);
        os = getOs();
        return "&userIs=" + useris + "&userCode=" + usercode + "&os=" + os + "&userFrom=" + userfrom;
    }

    public void setUserCode(Context context) {
        UUID deviceUUID = null;
        final String androidUniqueID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            if (androidUniqueID != "") {
                deviceUUID = UUID.nameUUIDFromBytes(androidUniqueID.getBytes("utf8"));
            } else {
                final String anotherUniqueID = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                if (anotherUniqueID != null) {
                    deviceUUID = UUID.nameUUIDFromBytes(anotherUniqueID.getBytes("utf8"));
                } else {
                    deviceUUID = UUID.randomUUID();
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        PreferenceManager.getInstance(context).setUsercode(deviceUUID.toString());
    }
}
