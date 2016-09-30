package tndn.app.nyam.utils;

import android.app.Activity;
import android.widget.Toast;

import tndn.app.nyam.R;

/**
 * How to use
 *
 * private BackPressCloseHandler backPressCloseHandler;
 * onCreate(){
 * ..
 *     backPressCloseHandler = new BackPressCloseHandler(this);
 *     ..
 * }
 *
 * @Override
public void onBackPressed() {
backPressCloseHandler.onBackPressed();
}
 */
public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;

    private Toast toast;


    private Activity activity;


    public BackPressCloseHandler(Activity context) {

        this.activity = context;

    }


    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {

            backKeyPressedTime = System.currentTimeMillis();

            showGuide();

            return;

        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {

            activity.moveTaskToBack(true);
            activity.finish();
            android.os.Process.killProcess(android.os.Process.myPid());

            toast.cancel();

        }

    }


    private void showGuide() {

        toast = Toast.makeText(activity, activity.getResources().getString(R.string.backclose),

                Toast.LENGTH_SHORT);

        toast.show();

    }

}