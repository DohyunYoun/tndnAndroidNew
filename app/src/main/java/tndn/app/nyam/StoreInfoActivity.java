package tndn.app.nyam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import tndn.app.nyam.adapter.NetworkImagePagerAdapter;
import tndn.app.nyam.data.StoreInfoData;
import tndn.app.nyam.data.StoreMenuData;
import tndn.app.nyam.map.MapRoutActivity;
import tndn.app.nyam.map.MarkerLayerWidget;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.GpsInfo;
import tndn.app.nyam.utils.IsOnline;
import tndn.app.nyam.utils.MakePricePretty;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;
import tndn.app.nyam.widget.HeightWrappingViewPager;
import tndn.app.nyam.widget.VerticalTextView;
import tpmap.android.map.Bounds;
import tpmap.android.map.Coord;
import tpmap.android.map.MapEventListener;
import tpmap.android.map.Pixel;
import tpmap.android.map.TPMap;
import tpmap.android.map.overlay.Marker;
import tpmap.android.utils.Projection;

/**
 * 2016 08 16
 * new api
 * <p/>
 * getShop->getStoreInfo
 * ResDetail->StoreInfo
 */

public class StoreInfoActivity extends AppCompatActivity  {

    String url;
    private ProgressDialog pDialog;

    private ImageView store_info_road_card;
    private ImageView food_menu_menu;

    private NetworkImageView store_info_imageview;
    private ImageView store_info_quality_flag;
    private ImageView store_info_quality_flag_detail;
    private TextView store_info_name;
    private TextView store_info_category;
    private TextView store_info_address;
    //    private ImageView store_info_phone;
    private TextView store_info_budget_chn;
    private TextView store_info_budget_kor;
    private TextView store_info_detail;
    //    private TextView store_info_homepage;
    private TextView store_info_time;
    //    private TextView store_info_distance;
    private TextView store_info_phone;
    private ImageView store_info_gotomap;

    private RelativeLayout store_info_card;
    private VerticalTextView store_info_card_address;
    private VerticalTextView store_info_card_name;
    private ImageView store_info_card_close;


    //    for minimap
    private TPMap mapView;
    private Drawable mapDirection;
    private Drawable redCircle;
    private MarkerLayerWidget markerLayer;
//    private Balloon balloon;
//    private LabelLayerWidget labelLayer;


    //store_info_map


    private StoreInfoData store;
    private int id;

    private GpsInfo gps;
    double latitude;
    double longitude;

    ArrayList<HashMap<String, Integer>> idx_images;

    String from;


    //for image slider
    private HeightWrappingViewPager store_info_viewpager;
    private NetworkImagePagerAdapter mImagePagerAdapter;
    private CirclePageIndicator indicator;

    public static StoreInfoActivity storeInfoActivity;


    /**
     * FOR MENU
     */
    //카페일때는 10
    //그외일때는 0
    private int input_type;

    private int main_position;
    private int sub_position;
    private int extra_position;
    private int drink_position;


    /**
     * value
     */
    private String mainId = "1";
    boolean qulity_check = false;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:                                //정상적인 Key 인증완료
                    mapView.invalidate();
                    break;
                case 1:
                    // mapView.refreshSize();
                    break;
                case 2:                                // Key 인증 실패처리
                    String toastMsg = (String) msg.obj;
                    Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mapView.stopUserLocation();                            // 현재 위치 표시 기능을 OFF합니다.
        mapView.stopView();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        storeInfoActivity = StoreInfoActivity.this;
        /*******************************************
         for actionbar
         *******************************************/


        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);
        Button actionbar_qr_button = (Button) findViewById(R.id.actionbar_qr_button);

        actionbar_qr_button.setVisibility(View.VISIBLE);
        actionbar_text.setText(getResources().getString(R.string.app_name));
        actionbar_qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.tndn.SCAN");
                intent.putExtra("SCAN_MODE", "ALL");
                startActivityForResult(intent, 0);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //이전 menu activity 삭제
//                if (from.equals("menu") && from != null) {
//                    FoodMenuActivity foodmenuActivity = (FoodMenuActivity) FoodMenuActivity.foodmenuActivity;
//                    foodmenuActivity.finish();
//                }

                //TODO: 메뉴까지 뒤로가기할때 finish해야할것 같음
            }
        });
        /*******************************************
         for actionbar
         *******************************************/


        mapView = (TPMap) findViewById(R.id.store_info_map);
//        mapView.dispatchMapEvent(this);


        initialize();
        initView();


        Intent i = getIntent();
        if (Intent.ACTION_VIEW.equals(i.getAction())) {
            //카테고리에서 아이템 클릭이나 홈에서 아이템 클릭
            Uri uri = i.getData();
            Log.e("tt", uri.getQueryParameter("id"));
            String tmp = uri.getQueryParameter("id");
            if (uri.getQueryParameter("id") == null || uri.getQueryParameter("id").equals("") || uri.getQueryParameter("id").equals("undefined") || uri.getQueryParameter("id").equals("null"))
                id = 6;
            else
                id = Integer.parseInt(uri.getQueryParameter("id"));

            if (uri.getQueryParameter("from") != null) {
                from = uri.getQueryParameter("from");
            }

            mainId = uri.getQueryParameter("mainId");
            if (mainId == null || mainId.equals("") || mainId.equals("undefined") || mainId.equals("null"))
                mainId = "1";

        } else {
            //에러 혹은 큐알로 바로 찍어서 들어왔을때
//            idx = i.getIntExtra("IDX", 0);
            id = 6;
            from = null;
            mainId = "1";
        }

        if (mainId.equals("1")) {
            findViewById(R.id.store_info_ll).setVisibility(View.VISIBLE);
        }

        food_menu_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "tndn://getStoreMenu?id=" + id;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
//                finish();
            }
        });
        setBitmap();
        getStoreInfo(id);


        store_info_gotomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder route_dialog = new AlertDialog.Builder(StoreInfoActivity.this);
                route_dialog.setMessage(getString(R.string.getMapFrom)).setCancelable(false).setPositiveButton(getResources().getString(R.string.btn_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();


                                StringRequest req = new StringRequest(new TDUrls().getMapFromURL + "?id=" + store.getId() + "&nameKor=" + store.getName_kor() + new UserLog().getLog(getApplicationContext()), new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Intent intent = new Intent(getApplicationContext(), MapRoutActivity.class);
                                        intent.putExtra("STORE", store);
                                        PreferenceManager.getInstance(getApplicationContext()).setFrominfo("OK");
                                        startActivity(intent);
                                        finish();
                                    }

                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("getMapFrom", error.getMessage());
                                    }
                                });
                                AppController.getInstance().addToRequestQueue(req);
                            }
                        }).setNegativeButton(getResources().getString(R.string.btn_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = route_dialog.create();
                alert.show();
            }
        });


        store_info_road_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_info_card.setVisibility(View.VISIBLE);
            }
        });

        store_info_card_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_info_card.setVisibility(View.GONE);
            }
        });


    }

    private void initialize() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);

        mapView.setMultiLang(TPMap.LangType.TYPE_CHIKOR);
        mapView.setMapResolution(TPMap.NORMAL_SMALL_FONT);            // resolution BIG Setting
        markerLayer = new MarkerLayerWidget();

        markerLayer.setEnableBalloon(false);            // Marker Balloon 기능 ON

        mapView.getOverlays().add(markerLayer);
        gps = new GpsInfo(this);


    }

    private void initView() {

//        store_info_imageview = (NetworkImageView) findViewById(R.id.store_info_imageview);
        store_info_quality_flag = (ImageView) findViewById(R.id.store_info_quality_flag);
        store_info_quality_flag_detail = (ImageView) findViewById(R.id.store_info_quality_flag_detail);
        store_info_name = (TextView) findViewById(R.id.store_info_name);
        store_info_category = (TextView) findViewById(R.id.store_info_category);
        store_info_address = (TextView) findViewById(R.id.store_info_address);
//        store_info_phone = (ImageView) findViewById(R.id.store_info_phone);
        store_info_budget_chn = (TextView) findViewById(R.id.store_info_budget_chn);
        store_info_budget_kor = (TextView) findViewById(R.id.store_info_budget_kor);
        store_info_detail = (TextView) findViewById(R.id.store_info_detail);
//        store_info_homepage = (TextView) findViewById(R.id.store_info_homepage);
        store_info_time = (TextView) findViewById(R.id.store_info_time);
        store_info_phone = (TextView) findViewById(R.id.store_info_phone);
//        store_info_distance = (TextView) findViewById(R.id.store_info_distance);
        store_info_gotomap = (ImageView) findViewById(R.id.store_info_gotomap);

        store_info_road_card = (ImageView) findViewById(R.id.store_info_road_card);
        food_menu_menu = (ImageView) findViewById(R.id.food_menu_menu);


        store_info_card = (RelativeLayout) findViewById(R.id.store_info_card);
        store_info_card_address = (VerticalTextView) findViewById(R.id.store_info_card_address);
        store_info_card_name = (VerticalTextView) findViewById(R.id.store_info_card_name);
        store_info_card_close = (ImageView) findViewById(R.id.store_info_card_close);


        store_info_viewpager = (HeightWrappingViewPager) findViewById(R.id.store_info_viewpager);
        indicator = (CirclePageIndicator) findViewById(R.id.store_info_indicator);
    }


    private void getStoreInfo(final int id) {

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
            url = new TDUrls().getStoreInfoURL + "?id=" + id + "&mlat=" + latitude + "&mlon=" + longitude + new UserLog().getLog(getApplicationContext());
            JsonObjectRequest objreq = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject res) {
                    try {
                        store = new StoreInfoData();

                        if (res.getString("result").equals("failed")) {//if result failed
                            Toast.makeText(getApplicationContext(),
                                    "Internet Access Failed", Toast.LENGTH_SHORT).show();
                        } else {

                            /**
                             * FOR IMAGES
                             */
                            idx_images = new ArrayList<HashMap<String, Integer>>();
                            JSONArray images = res.getJSONArray("images");
                            for (int i = 0; i < images.length(); i++) {

                                JSONObject obj = images.getJSONObject(i);
                                Iterator<String> itr = obj.keys();
                                HashMap<String, Integer> map = new HashMap<>();


                                while (itr.hasNext()) {
                                    String key = itr.next();
                                    String value = obj.getString(key);

                                    switch (key) {
                                        case "idx_image_file_path":
                                            if (value.equals("") || value.equals(null) || value.equals("null") || value.equals("NULL") || value == null)
                                                value = "0";
                                            map.put("idx_image_file_path", Integer.parseInt(value));
                                            break;
                                        case "info_flag":
                                            if (value.equals("") || value.equals(null) || value.equals("null") || value.equals("NULL") || value == null)
                                                value = "0";
                                            map.put("info_flag", Integer.parseInt(value));
                                            break;
                                    }//end switch
                                }//end while
                                idx_images.add(map);
                            }//end for
                            store.setImages(idx_images);

                            /**
                             * FOR INFO
                             */
                            JSONObject info = res.getJSONArray("info").getJSONObject(0);
                            String tmpValue = "";

                            tmpValue = info.getString("id");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setId(Integer.parseInt(tmpValue));

                            tmpValue = info.getString("classify_kor");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setClassify_kor(tmpValue);

                            tmpValue = info.getString("classify_chn");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setClassify_chn(tmpValue);

                            tmpValue = info.getString("category_name_kor");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setCategory_name_kor(tmpValue);

                            tmpValue = info.getString("category_name_chn");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setCategory_name_chn(tmpValue);

                            tmpValue = info.getString("name_kor");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setName_kor(tmpValue);

                            tmpValue = info.getString("name_chn");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setName_chn(tmpValue);

                            tmpValue = info.getString("address_kor");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setAddress_kor(tmpValue);

                            tmpValue = info.getString("address_chn");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setAddress_chn(tmpValue);

                            tmpValue = info.getString("main_menu_kor");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setMain_menu_kor(tmpValue);

                            tmpValue = info.getString("main_menu_chn");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setMain_menu_chn(tmpValue);

                            tmpValue = info.getString("tel_1");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setTel_1(tmpValue);

                            tmpValue = info.getString("tel_2");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setTel_2(tmpValue);

                            tmpValue = info.getString("tel_3");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setTel_3(tmpValue);

                            tmpValue = info.getString("business_hour_open");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setBusiness_hour_open(tmpValue);

                            tmpValue = info.getString("business_hour_closed");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setBusiness_hour_closed(tmpValue);

                            tmpValue = info.getString("detail_kor");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setDetail_kor(tmpValue);

                            tmpValue = info.getString("detail_chn");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setDetail_chn(tmpValue);

                            tmpValue = info.getString("budget");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setBudget(tmpValue);

                            tmpValue = info.getString("latitude");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "0";
                            store.setLatitude(tmpValue);

                            tmpValue = info.getString("longitude");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "0";
                            store.setLongitude(tmpValue);
                            tmpValue = info.getString("quality_flag");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "0";
                            store.setQuality_flag(tmpValue);

                            tmpValue = info.getString("is_pay");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setIs_pay(Integer.parseInt(tmpValue));

                            tmpValue = info.getString("menu_input_type");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setMenu_input_type(tmpValue);

                            tmpValue = info.getString("distance");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setDistance(tmpValue);

                            /**
                             * FOR MENU
                             */
                            ArrayList<StoreMenuData> menus = new ArrayList<StoreMenuData>();
                            JSONArray result = res.getJSONArray("menus");
                            for (int i = 0; i < result.length(); i++) {

                                StoreMenuData menu = new StoreMenuData();
                                JSONObject objmenu = result.getJSONObject(i);
                                Iterator<String> itr = objmenu.keys();

                                while (itr.hasNext()) {
                                    String keymenu = itr.next();
                                    String valuemenu = objmenu.getString(keymenu);

                                    switch (keymenu) {
                                        case "id":
                                            menu.setId(Integer.parseInt(valuemenu));
                                            break;
                                        case "idx_store":
                                            if (valuemenu.equals("") || valuemenu.equals(null) || valuemenu.equals("null") || valuemenu.equals("NULL") || valuemenu == null)
                                                valuemenu = "";
                                            menu.setIdx_store(Integer.parseInt(valuemenu));
                                            break;
                                        case "menu_price":
                                            String temp = valuemenu.replaceAll(",", "");
                                            if (temp.equals("") || temp == null) {
                                                temp = "0";
                                            }
                                            menu.setMenu_price(temp);
                                            break;
                                        case "menu_price_for_ice":
                                            String temp_ice = valuemenu.replaceAll(",", "");
                                            if (temp_ice.equals("") || temp_ice.equals(null) || temp_ice.equals("null") || temp_ice.equals("NULL") || temp_ice == null)
                                                temp_ice = "0";
                                            menu.setMenu_price_for_ice(temp_ice);

                                            break;
                                        case "menu_size":
                                            if (valuemenu.equals("") || valuemenu.equals(null) || valuemenu.equals("null") || valuemenu.equals("NULL") || valuemenu == null)
                                                valuemenu = "";
                                            menu.setMenu_size(valuemenu);
                                            break;
                                        case "menu_category":
                                            if (valuemenu.equals("") || valuemenu.equals(null) || valuemenu.equals("null") || valuemenu.equals("NULL") || valuemenu == null)
                                                valuemenu = "";

                                            if (valuemenu.equals("1")) {
                                                //주메뉴
                                                if (main_position == -1) {
                                                    main_position = i;
                                                }
                                            } else if (valuemenu.equals("2")) {
                                                //부메뉴
                                                if (sub_position == -1) {
                                                    sub_position = i;
                                                }
                                            } else if (valuemenu.equals("3")) {
                                                //엑스트라메뉴
                                                if (extra_position == -1) {
                                                    extra_position = i;
                                                }
                                            } else if (valuemenu.equals("4")) {
                                                //드링크
                                                if (drink_position == -1) {
                                                    drink_position = i;
                                                }
                                            }

                                            menu.setMenu_category(valuemenu);
                                            break;
                                        case "is_best_menu":
                                            if (valuemenu.equals("") || valuemenu.equals(null) || valuemenu.equals("null") || valuemenu.equals("NULL") || valuemenu == null)
                                                valuemenu = "0";
                                            menu.setIs_best_menu(Integer.parseInt(valuemenu));
                                            break;
                                        case "is_image_menu":
                                            if (valuemenu.equals("") || valuemenu.equals(null) || valuemenu.equals("null") || valuemenu.equals("NULL") || valuemenu == null)
                                                valuemenu = "0";
                                            menu.setIs_image_menu(Integer.parseInt(valuemenu));
                                            break;
                                        case "menu_name_kor":
                                            if (valuemenu.equals("") || valuemenu.equals(null) || valuemenu.equals("null") || valuemenu.equals("NULL") || valuemenu == null)
                                                valuemenu = "";
                                            menu.setMenu_name_kor(valuemenu);
                                            break;
                                        case "menu_name_chn":
                                            if (valuemenu.equals("") || valuemenu.equals(null) || valuemenu.equals("null") || valuemenu.equals("NULL") || valuemenu == null)
                                                valuemenu = "";
                                            menu.setMenu_name_chn(valuemenu);
                                            break;
                                        case "menu_name_eng":
                                            if (valuemenu.equals("") || valuemenu.equals(null) || valuemenu.equals("null") || valuemenu.equals("NULL") || valuemenu == null)
                                                valuemenu = "";
                                            menu.setMenu_name_eng(valuemenu);
                                            break;
                                        case "menu_name_jpn":
                                            if (valuemenu.equals("") || valuemenu.equals(null) || valuemenu.equals("null") || valuemenu.equals("NULL") || valuemenu == null)
                                                valuemenu = "";
                                            menu.setMenu_name_jpn(valuemenu);
                                            break;
                                        case "idx_image_file_path":
                                            if (valuemenu.equals("") || valuemenu.equals(null) || valuemenu.equals("null") || valuemenu.equals("NULL") || valuemenu == null)
                                                valuemenu = "0";
                                            menu.setIdx_image_file_path(Integer.parseInt(valuemenu));

                                    }       //end switch
                                }   //end while
                                menus.add(menu);
                            }   //end for
                            store.setMenus(menus);

                            //TODO phone & 환율 적용한 budget & QR로 들어왔을때 즉 url로 들어왔을때 데이터 처리 어떻게?
                            if (store.getImages().size() == 0) {
                                HashMap<String, Integer> map = new HashMap<>();
                                map.put("idx_image_file_path", 0);
                                map.put("info_plag", 0);
                                idx_images.add(map);
                                store.setImages(idx_images);
                            }


                            mImagePagerAdapter = new NetworkImagePagerAdapter(getApplicationContext(), store.getImages(), "food");
                            store_info_viewpager.setAdapter(mImagePagerAdapter);
                            indicator.setViewPager(store_info_viewpager);


                            if (store.getQuality_flag().equals("1")) {

                                store_info_quality_flag.setVisibility(View.VISIBLE);
                                store_info_quality_flag.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (qulity_check) {
                                            store_info_quality_flag_detail.setVisibility(View.GONE);
                                            qulity_check = false;

                                        } else {
                                            store_info_quality_flag_detail.setVisibility(View.VISIBLE);
                                            qulity_check = true;
                                        }
                                    }
                                });
                                store_info_quality_flag_detail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        store_info_quality_flag_detail.setVisibility(View.GONE);
                                        qulity_check = false;

                                    }
                                });

                            }
                            store_info_name.setText(store.getName_kor() + "  " + store.getName_chn());
                            store_info_category.setText(store.getCategory_name_chn());
                            store_info_address.setText(store.getAddress_kor());

                            store_info_budget_chn.setText(new MakePricePretty().makePricePretty(getApplicationContext(), store.getBudget(), false).replace("¥ ", ""));
                            store_info_budget_kor.setText(store.getBudget());
                            store_info_detail.setText(store.getDetail_chn());

                            store_info_card_name.setText(store.getName_kor() + "  " + store.getName_chn());
                            store_info_card_address.setText(store.getAddress_kor());

                            String opentime;
                            String closetime;
                            if (store.getBusiness_hour_open().length() == 4) {
                                opentime = store.getBusiness_hour_open().substring(0, 2) + ":" + store.getBusiness_hour_open().substring(2);
                            } else {
                                opentime = store.getBusiness_hour_open();
                            }
                            if (store.getBusiness_hour_closed().length() == 4) {
                                closetime = store.getBusiness_hour_closed().substring(0, 2) + ":" + store.getBusiness_hour_closed().substring(2);
                            } else {
                                closetime = store.getBusiness_hour_closed();
                            }
                            store_info_time.setText(opentime + " ~ " + closetime);

                            store_info_phone.setText(store.getTel_1() + "-" + store.getTel_2() + "-" + store.getTel_3());
                            store_info_phone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + store.getTel_1() + "-" + store.getTel_2() + "-" + store.getTel_3()));
                                    startActivity(intent);
                                }
                            });


                            Coord transCoordination = mapView.getProjection().transCoordination(Projection.WGS84,
                                    Projection.UTMK, new Coord(Float.parseFloat(store.getLongitude()), Float.parseFloat(store.getLatitude())));
                            mapView.setMapCenter(transCoordination);                                // 입렫된 위치로 MAP의 중앙이 이동
                            Coord getcoord = mapView.getMapCenter();
                            Marker marker = new Marker(getcoord, getResources().getDrawable(R.mipmap.ic_mapmarker_small));

                            markerLayer.addItem(marker);

//                            mapView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
////                                Toast.makeText(getApplicationContext(), shop.getLatitude() + "_" + shop.getLongitude(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
                        }//end else (result)
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        hidepDialog();
                    } //end jsonexception catch
                    hidepDialog();
//data setting


                }//end response

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
    }   //end getStoreInfo


    private void setBitmap() {
        mapDirection = getApplicationContext().getResources().getDrawable(R.mipmap.map_circinus);
        redCircle = getApplicationContext().getResources().getDrawable(R.mipmap.blue_circle);

        float totalsize = Debug.getNativeHeapAllocatedSize()
                + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        Log.i("LTG",
                "current size 1:" + (totalsize / 1024.0 / 1024.0) + "/ "
                        + Environment.getRootDirectory());

    }



//dialog

    private void showpDialog() {
        if (!StoreInfoActivity.this.isFinishing()) {
            if (!pDialog.isShowing())
                pDialog.show();
        }
    }

    private void hidepDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }
}
