package tndn.app.nyam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.IsOnline;
import tndn.app.nyam.utils.SaveImagetoStorage;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;

/**
 * Created by YounDH on 2016-06-28.
 */
public class BannerInnerActivity extends AppCompatActivity {

    /**
     * service
     */
    private ProgressDialog pDialog;

    private NetworkImageView banner;
    private ImageLoader mImageLoader;

    ArrayList<HashMap<String, String>> banners;

    String data_idx = "";
    String qr_idx = "";
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_inner);
/********************************************
 for actionbar
 ********************************************/

        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);

        actionbar_text.setText(getResources().getString(R.string.app_name));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
/********************************************
 for actionbar
 ********************************************/

        banner = (NetworkImageView) findViewById(R.id.img_banner_inner);

        banners = new ArrayList<HashMap<String, String>>();
        mImageLoader = AppController.getInstance().getImageLoader();



                pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);
        i = getIntent();
        if (Intent.ACTION_VIEW.equals(i.getAction())) {
            //카테고리에서 아이템 클릭이나 홈에서 아이템 클릭
            String id = i.getData().getQueryParameter("id");
            errorBannerInfo(id);
//            if (id.equals("")) {
//                banner.setDefaultImageResId(R.mipmap.img_logo);
//
//            } else {
//                getBannerInfo(id);
//            }
        } else {
            banner.setDefaultImageResId(R.mipmap.img_logo);
        }


    }

    private void errorBannerInfo(String id) {

        //카테고리에서 아이템 클릭이나 홈에서 아이템 클릭
        if (id.equals("rentcar")||id.equals("4")) {
            banner.setDefaultImageResId(R.mipmap.img_rentcar);
            banner.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new SaveImagetoStorage().saveImagetoStorage(getApplicationContext(), R.mipmap.qr_rentcar, "tndn-qr-rentcar");
                    return false;
                }
            });

        } else if (id.equals("aidibao")||id.equals("3")) {
            banner.setDefaultImageResId(R.mipmap.img_aidibao);
            banner.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new SaveImagetoStorage().saveImagetoStorage(getApplicationContext(), R.mipmap.qr_aidibao, "tndn-qr-aidibao");
                    return false;
                }
            });

        } else if (id.equals("simya")||id.equals("5")) {
            banner.setDefaultImageResId(R.mipmap.img_simya);
            banner.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new SaveImagetoStorage().saveImagetoStorage(getApplicationContext(), R.mipmap.qr_simya, "tndn-qr-simya");
                    return false;
                }
            });

        } else {
//            banner.setDefaultImageResId(R.mipmap.img_logo);
        }
    }

    private void getBannerInfo(final String id) {

        if (!new IsOnline().onlineCheck(this)) {                  //internet check failed start
            errorBannerInfo(id);
            Toast.makeText(this, "Internet Access Failed", Toast.LENGTH_SHORT).show();
        } else { //internet check success start
            showpDialog();
            String url = new TDUrls().getBannerInfoURL + "?id=" + id + new UserLog().getLog(getApplicationContext());
            JsonObjectRequest objreq = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject res) {
                    try {
                        if (res.getString("result").equals("failed")) {//if result failed
                            errorBannerInfo(id);
                            Toast.makeText(getApplicationContext(),
                                    "Internet Access Failed", Toast.LENGTH_SHORT).show();
                        } else {

                            JSONArray images = res.getJSONArray("data");
                            for (int i = 0; i < images.length(); i++) {
                                HashMap<String, String> hBanner = new HashMap<String, String>();

                                JSONObject obj = images.getJSONObject(i);
                                Iterator<String> itr = obj.keys();
                                while (itr.hasNext()) {
                                    String key = itr.next();
                                    String value = obj.getString(key);

                                    switch (key) {
                                        case "id":
                                            if (value.equals("") || value.equals(null) || value.equals("null") || value.equals("NULL") || value == null)
                                                value = "0";
                                            hBanner.put("id", value);
                                            break;
                                        case "idx_image_file_path":
                                            if (value.equals("") || value.equals(null) || value.equals("null") || value.equals("NULL") || value == null)
                                                value = "0";
                                            hBanner.put("idx_image_file_path", value);
                                            break;
                                        case "qr_code_flag":
                                            if (value.equals("") || value.equals(null) || value.equals("null") || value.equals("NULL") || value == null)
                                                value = "";
                                            hBanner.put("qr_code_flag", value);

                                            break;

                                    }//end switch
                                }//end while
                                banners.add(hBanner);
                            }//end for

                        }//end else (result)
                    } catch (JSONException e) {
                        errorBannerInfo(id);
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        hidepDialog();
                    } //end jsonexception catch

//data setting

                    for (int j = 0; j < banners.size(); j++) {
                        if (banners.get(j).get("qr_code_flag").equals("0")) {
                            data_idx = banners.get(j).get("idx_image_file_path");
                        } else if (banners.get(j).get("qr_code_flag").equals("1")) {
                            qr_idx = banners.get(j).get("idx_image_file_path");
                        } else {
                            //end else
                            banner.setDefaultImageResId(R.mipmap.img_logo);
                        }
                    }//end for
                    banner.setImageUrl(new TDUrls().getImageURL + "?id=" + data_idx, mImageLoader);
                    banner.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (!qr_idx.equals("")) {
                                new SaveImagetoStorage().saveImagetoStorage(getApplicationContext(), new TDUrls().getImageURL + "?id=" + qr_idx, "tndn-qr-" + banners.get(0).get("id"));
                                return false;
                            }
                            return false;
                        }
                    });
                    hidepDialog();
                }//end response

            }, new Response.ErrorListener() {   //end request

                @Override
                public void onErrorResponse(VolleyError error) {
                    errorBannerInfo(id);
                    Toast.makeText(getApplicationContext(),
                            "Internet Access Failed", Toast.LENGTH_SHORT).show();
                    //hide the progress dialog
                    hidepDialog();
                }
            });

            // Adding request to request queue

            AppController.getInstance().addToRequestQueue(objreq);
        }//end internet check success
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
