package tndn.app.nyam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Iterator;

import tndn.app.nyam.adapter.MagazineSquareAdapter;
import tndn.app.nyam.data.StoreListData;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.widget.ExpandableHeightGridView;
import tndn.app.nyam.utils.GpsInfo;
import tndn.app.nyam.utils.IsOnline;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;

public class MagazineHomeActivity extends AppCompatActivity {


    private ImageView main_function01;

    private LinearLayout main_jejuweekly_01;
    private LinearLayout main_jejuweekly_02;

    private String intentURL;


    //squre pic
    private ImageView magazine_home_square_more;
    private ExpandableHeightGridView magazine_home_square_gridview;
    private MagazineSquareAdapter mSquareAdapter;


    //    four site
    private NetworkImageView magazine_home_site_01;
    private TextView magazine_home_site_content_01;
    private NetworkImageView magazine_home_site_02;
    private TextView magazine_home_site_content_02;
    private NetworkImageView magazine_home_site_03;
    private TextView magazine_home_site_content_03;
    private NetworkImageView magazine_home_site_04;
    private TextView magazine_home_site_content_04;
    private ImageView magazine_home_four_site_more;


    private ImageLoader mImageLoader;

    //    for gps
    private GpsInfo gps;
    double latitude;
    double longitude;

    int page = 0;
    private ProgressDialog pDialog;

    //    for sites
    ArrayList<StoreListData> stores;
    private String url;
    /**
     * value
     */
    private String localizationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine_home);

        /*********************************************
         for actionbar
         *********************************************/

        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);
//        Button actionbar_qr_button = (Button) findViewById(R.id.actionbar_qr_button);
        Button actionbar_refresh_button = (Button) findViewById(R.id.actionbar_refresh_button);

//        actionbar_qr_button.setVisibility(View.VISIBLE);
        actionbar_refresh_button.setVisibility(View.VISIBLE);

        actionbar_text.setText(getResources().getString(R.string.magazine));

//        actionbar_qr_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                intent.putExtra("SCAN_MODE", "ALL");
//                startActivityForResult(intent, 0);
//            }
//        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        actionbar_refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //refresh
                PreferenceManager.getInstance(getApplicationContext()).setLocationcheck("");
                startActivity(new Intent(getApplicationContext(), MagazineHomeActivity.class));
                finish();
            }
        });

        /*********************************************
         for actionbar
         *********************************************/


        initView();
        initialize();
        localizationId = PreferenceManager.getInstance(this).getLocalization();
        if (localizationId.equals(""))
            localizationId = "1";
        getStoreList(localizationId);

        main_function01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intentURL = "tndn://getStoreInfo?mainId=1&id=6860&name=Ganse客厅";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentURL));
                startActivity(intent);

            }
        });


        main_jejuweekly_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jejuchina.net/news/articleView.html?idxno=1572"));
                startActivity(intent);
            }
        });
        main_jejuweekly_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jejuchina.net/news/articleView.html?idxno=1571"));
                startActivity(intent);
            }
        });

        magazine_home_four_site_more.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if (PreferenceManager.getInstance(getApplicationContext()).getLocalization().equals("2")) {
                                                                    //수원
                                                                    intentURL = "tndn://getStoreList";
                                                                    PreferenceManager.getInstance(getApplicationContext()).setLocationId("32");

                                                                } else if (PreferenceManager.getInstance(getApplicationContext()).getLocalization().equals("3")) {
                                                                    //서울
                                                                    intentURL = "tndn://getStoreList";
                                                                    PreferenceManager.getInstance(getApplicationContext()).setLocationId("36");


                                                                } else if (PreferenceManager.getInstance(getApplicationContext()).getLocalization().equals("5")) {
                                                                    //부산
                                                                    intentURL = "tndn://getStoreList";
                                                                    PreferenceManager.getInstance(getApplicationContext()).setLocationId("40");
                                                                } else {
                                                                    //제주
                                                                    intentURL = "tndn://getStoreList";
                                                                    PreferenceManager.getInstance(getApplicationContext()).setLocalization("1");
                                                                    PreferenceManager.getInstance(getApplicationContext()).setLocationId("26");

                                                                }
                                                                PreferenceManager.getInstance(getApplicationContext()).setUserfrom("31");
                                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentURL));
                                                                startActivity(intent);
                                                            }
                                                        }

        );

        magazine_home_square_more.setOnClickListener(new View.OnClickListener()

                                                     {
                                                         @Override
                                                         public void onClick(View v) {
                                                             intentURL = "tndn://magazineSquare";
                                                             Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentURL));
                                                             startActivity(intent);
                                                         }
                                                     }

        );

    }

    private void initialize() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);

        mImageLoader = AppController.getInstance().getImageLoader();
        stores = new ArrayList<StoreListData>();
        magazine_home_square_gridview.setExpanded(true);

        gps = new GpsInfo(this);
    }

    private void initView() {

        main_function01 = (ImageView) findViewById(R.id.magazine_home_function01);

        main_jejuweekly_01 = (LinearLayout) findViewById(R.id.main_jejuweekly_01);
        main_jejuweekly_02 = (LinearLayout) findViewById(R.id.main_jejuweekly_02);

        magazine_home_square_more = (ImageView) findViewById(R.id.magazine_home_square_more);
        magazine_home_square_gridview = (ExpandableHeightGridView) findViewById(R.id.magazine_home_square_gridview);

//        four site
        magazine_home_site_01 = (NetworkImageView) findViewById(R.id.magazine_home_site_01);
        magazine_home_site_content_01 = (TextView) findViewById(R.id.magazine_home_site_content_01);
        magazine_home_site_02 = (NetworkImageView) findViewById(R.id.magazine_home_site_02);
        magazine_home_site_content_02 = (TextView) findViewById(R.id.magazine_home_site_content_02);
        magazine_home_site_03 = (NetworkImageView) findViewById(R.id.magazine_home_site_03);
        magazine_home_site_content_03 = (TextView) findViewById(R.id.magazine_home_site_content_03);
        magazine_home_site_04 = (NetworkImageView) findViewById(R.id.magazine_home_site_04);
        magazine_home_site_content_04 = (TextView) findViewById(R.id.magazine_home_site_content_04);
        magazine_home_four_site_more = (ImageView) findViewById(R.id.magazine_home_four_site_more);

    }


    /**
     * Internet Access
     */
    private void getStoreList(String localizationId) {

        if (!new IsOnline().onlineCheck(this)) {                  //internet check failed start
            Toast.makeText(this, "Internet Access Failed", Toast.LENGTH_SHORT).show();
        } else { //internet check success start
            showpDialog();
// GPS 사용유무 가져오기
            if (gps.isGetLocation()) {

                Thread gpsThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                    }
                });
                gpsThread.start();

                //http://www.tndn.net/#/map/33.505373/126.525912/33.495603/126.523251/3

                try {
                    gpsThread.join();  // 쓰레드 작업 끝날때까지 다른 작업들은 대기
                } catch (Exception e) {
                }
            } else {
                // GPS 를 사용할수 없으므로
                if (!PreferenceManager.getInstance(getApplicationContext()).getLocationcheck().equals("OK")) {
                    gps.showSettingsAlert();
                }
            }//end else system gps check

            url = new TDUrls().getStoreListURL + "?localizationId=" + localizationId + "&orderBy=random&mlat=" + latitude + "&mlon=" + longitude + new UserLog().getLog(getApplicationContext());
            JsonObjectRequest objreq = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject res) {

                    try {
                        if (res.getString("result").equals("failed")) {//else result failed
//                            findViewById(R.id.store_list_error).setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            JSONArray result = res.getJSONArray("data");
                            for (int i = 0; i < result.length(); i++) {

                                StoreListData store = new StoreListData();
                                JSONObject obj = result.getJSONObject(i);
                                Iterator<String> itr = obj.keys();

                                while (itr.hasNext()) {
                                    String key = itr.next();
                                    String value = obj.getString(key);

                                    switch (key) {
                                        case "idx_store":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "0";
                                            store.setIdx_store(Integer.parseInt(value));
                                            break;
                                        case "idx_store_classify":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "0";
                                            store.setIdx_store_classify(Integer.parseInt(value));
                                            break;
                                        case "idx_region_category":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "0";
                                            store.setIdx_region_category(Integer.parseInt(value));
                                            break;
                                        case "idx_store_major_classify":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "1";
                                            store.setIdx_store_major_classify(Integer.parseInt(value));
                                            break;

                                        case "classify_kor":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setClassify_kor(value);
                                            break;
                                        case "classify_chn":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setClassify_chn(value);
                                            break;
                                        case "category_name_kor":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setCategory_name_kor(value);
                                            break;
                                        case "category_name_chn":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setCategory_name_chn(value);
                                            break;
                                        case "name_kor":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setName_kor(value);
                                            break;
                                        case "name_chn":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setName_chn(value);
                                            break;
                                        case "address_kor":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setAddress_kor(value);
                                            break;
                                        case "address_chn":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setAddress_chn(value);
                                            break;
                                        case "main_menu_kor":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setMain_menu_kor(value);
                                            break;
                                        case "main_menu_chn":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setMain_menu_chn(value);
                                            break;
                                        case "tel_1":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setTel_1(value);
                                            break;
                                        case "tel_2":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setTel_2(value);
                                            break;
                                        case "tel_3":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setTel_3(value);
                                            break;
                                        case "business_hour_open":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setBusiness_hour_open(value);
                                            break;
                                        case "business_hour_closed":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setBusiness_hour_closed(value);
                                            break;
                                        case "budget":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setBudget(value);
                                            break;
                                        case "latitude":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "0";
                                            store.setLatitude(value);
                                            break;
                                        case "longitude":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "0";
                                            store.setLongitude(value);
                                            break;
                                        case "is_pay":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setIs_pay(value);
                                            break;
                                        case "menu_input_type":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "";
                                            store.setMenu_input_type(value);
                                            break;
                                        case "idx_image_file_path":
                                            if (value == null || value.equals("") || value.equals("null") || value.equals("NULL"))
                                                value = "0";
                                            store.setIdx_image_file_path(Integer.parseInt(value));
                                            break;
                                        case "distance":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL") || value.equals("0")) {
                                                store.setDistance(getApplicationContext().getString(R.string.no_distance));
                                            } else {
                                                store.setDistance(value);
                                            }
                                            break;

                                    }       //end switch
                                }   //end while
                                stores.add(store);
                            }   //end for
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        hidepDialog();
                    } //end jsonexception catch

                    //data setting
                    hidepDialog();
                    if (stores.size() < 13) {
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        setSitesRandomView(stores);
                    }
                }   //end response

            }, new Response.ErrorListener() {   //end request

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),
                            "Internet Access Failed", Toast.LENGTH_SHORT).show();
                    //hide the progress dialog
                    hidepDialog();
                }
            });

            // Adding request to request queue

            AppController.getInstance().addToRequestQueue(objreq);
        }//end internet check success

    }//end getStoreLIst


    //dialog
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void setSitesRandomView(final ArrayList<StoreListData> sites) {
        //                        squre pic

        final ArrayList<StoreListData> nine = new ArrayList<>();
        nine.add(sites.get(1));
        nine.add(sites.get(2));
        nine.add(sites.get(3));
        nine.add(sites.get(4));
        nine.add(sites.get(5));
        nine.add(sites.get(6));
        nine.add(sites.get(7));
        nine.add(sites.get(8));
        nine.add(sites.get(9));
        mSquareAdapter = new MagazineSquareAdapter(MagazineHomeActivity.this, nine);
        magazine_home_square_gridview.setAdapter(mSquareAdapter);
        magazine_home_square_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "tndn://magazineSquare?id=" + nine.get(position).getIdx_store();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
//                        four site
        magazine_home_site_01.setImageResource(R.mipmap.noimg_big_site);
        magazine_home_site_01.setImageUrl(new TDUrls().getImageURL + "?size=350&id=" + sites.get(11).getIdx_image_file_path(), mImageLoader);
        magazine_home_site_content_01.setText(sites.get(11).getName_chn());
        magazine_home_site_02.setImageResource(R.mipmap.noimg_big_site);
        magazine_home_site_02.setImageUrl(new TDUrls().getImageURL + "?size=350&id=" + sites.get(12).getIdx_image_file_path(), mImageLoader);
        magazine_home_site_content_02.setText(sites.get(12).getName_chn());
        magazine_home_site_03.setImageResource(R.mipmap.noimg_big_site);
        magazine_home_site_03.setImageUrl(new TDUrls().getImageURL + "?size=350&id=" + sites.get(13).getIdx_image_file_path(), mImageLoader);
        magazine_home_site_content_03.setText(sites.get(13).getName_chn());
        magazine_home_site_04.setImageResource(R.mipmap.noimg_big_site);
        magazine_home_site_04.setImageUrl(new TDUrls().getImageURL + "?size=350&id=" + sites.get(14).getIdx_image_file_path(), mImageLoader);
        magazine_home_site_content_04.setText(sites.get(14).getName_chn());

        magazine_home_site_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "tndn://getStoreInfo?mainId=" + sites.get(11).getIdx_store_major_classify() + "&id=" + sites.get(11).getIdx_store();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        magazine_home_site_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "tndn://getStoreInfo?mainId=" + sites.get(12).getIdx_store_major_classify() + "&id=" + sites.get(12).getIdx_store();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        magazine_home_site_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "tndn://getStoreInfo?mainId=" + sites.get(13).getIdx_store_major_classify() + "&id=" + sites.get(13).getIdx_store();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        magazine_home_site_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "tndn://getStoreInfo?mainId=" + sites.get(14).getIdx_store_major_classify() + "&id=" + sites.get(14).getIdx_store();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

    }

}
