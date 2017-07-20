package tndn.app.nyam.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by YounDH on 2016-08-31.
 */
public class LogHome {
    public void send(Context context, String where) {
        StringRequest req = new StringRequest(new TDUrls().logHomeURL + "?where=" + where+new UserLog().getLog(context), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("logHome", error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }
}
