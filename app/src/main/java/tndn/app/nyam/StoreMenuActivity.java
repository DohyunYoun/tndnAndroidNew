
package tndn.app.nyam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tndn.app.nyam.adapter.StoreMenuAdapter;
import tndn.app.nyam.data.StoreInfoData;
import tndn.app.nyam.data.StoreMenuData;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.IsOnline;
import tndn.app.nyam.utils.LogHome;
import tndn.app.nyam.utils.MakePaymentPrice;
import tndn.app.nyam.utils.MakePricePretty;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.SaveExchangeRate;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;

public class StoreMenuActivity extends AppCompatActivity {

    private TextView menu_main;
    private TextView menu_sub;
    private TextView menu_extra;
    private TextView menu_drink;

    private String url;
    private ListView lv_menu;
    private StoreMenuAdapter mAdapter;
    private ProgressDialog pDialog;

    private TextView food_menu_total_count;
    //    private ImageButton food_menu_cart_button;
    private TextView food_menu_total_kor;
    private TextView food_menu_total_chn;
    private Button food_menu_pay_button;


    /**
     * for menupopup
     */

    private LinearLayout menu_popup;
    private ImageView menu_popup_check;

    private TextView menu_popup_name_chn;
    private TextView menu_popup_name_kor;

    private TextView menu_popup_ice_price_kor;
    private TextView menu_popup_ice_price_chn;

    private ImageView menu_popup_ice_minus_button;
    private TextView menu_popup_ice_count;
    private ImageView menu_popup_ice_plus_button;

    private TextView menu_popup_hot_price_kor;
    private TextView menu_popup_hot_price_chn;

    private ImageView menu_popup_hot_minus_button;
    private TextView menu_popup_hot_count;
    private ImageView menu_popup_hot_plus_button;

    private RelativeLayout menu_popup_ice_layout;
    private RelativeLayout menu_popup_hot_layout;
    private View menu_popup_icehot_line;


    /**
     * end for menupopup
     */

    private int totalcount;
    private int totalprice;

    private int id;
    String name;
    private ArrayList<StoreMenuData> order;

    //for log params
    private String params_menucount = "";
    private String params_idx_restaurantMenu = "";
    private String params_menuKOR = "";
    private String params_menuPrice = "";
    private String params_data = "";


    private ImageView food_menu_information;


    //카페일때는 10
    //그외일때는 0
    private int input_type;

    private int main_position;
    private int sub_position;
    private int extra_position;
    private int drink_position;
    private String fourposition;
    private int index;

    private int currentFirstVisibleItem;
    private int currentVisibleItemCount;
    private int currentScrollState;


    private int icecount = 0;
    private int hotcount = 0;


    double curr;
    double sale = 0;
    String priceKor;
    String priceChn;
    String priceKorSale;
    String priceChnSale;

    private StoreInfoData store;
    String from;


    /**
     * StoreInfo
     */
    ArrayList<HashMap<String, Integer>> idx_images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_menu);

/********************************************
 for actionbar
 ********************************************/


        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);
        Button actionbar_qr_button = (Button) findViewById(R.id.actionbar_qr_button);

        actionbar_qr_button.setVisibility(View.VISIBLE);
        actionbar_text.setText(getResources().getString(R.string.food));
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
                //이전 info activity 삭제
//                if (from.equals("info") && from != null) {
//                    StoreInfoActivity storeInfoActivity = (StoreInfoActivity) StoreInfoActivity.storeInfoActivity;
//                    storeInfoActivity.finish();
//                }
            }
        });

/********************************************
 for actionbar
 ********************************************/


        initView();
        initialize();


        Intent i = getIntent();
        if (Intent.ACTION_VIEW.equals(i.getAction())) {
            //카테고리에서 아이템 클릭이나 홈에서 아이템 클릭
            Uri uri = i.getData();
            id = Integer.parseInt(uri.getQueryParameter("id"));
            if (uri.getQueryParameter("from") != null) {
                from = uri.getQueryParameter("from");
            }

            if (uri.getQueryParameter("input_type") != null && uri.getQueryParameter("input_type").equals("10")) {
                input_type = 10;
            } else
                input_type = 0;

        } else {
            //에러 혹은 큐알로 바로 찍어서 들어왔을때
//            idx = i.getIntExtra("IDX", 0);
            id = 6;
            input_type = 0;
            name = "TNDN";
            from = null;
        }

        getStoreInfo(id);

        menu_popup_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCafeCheck(false, -1);
            }
        });

        food_menu_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = new TDUrls().getIntentURL("getStoreInfo", id) + "&from=menu";
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
                finish();
            }
        });


        food_menu_pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalcount == 0) {
                    Toast.makeText(getApplicationContext(), R.string.menu_error, Toast.LENGTH_SHORT).show();
                } else {
                    //선택된 아이들
                    order = new ArrayList<StoreMenuData>();
                    //TODO:카페일 경우와 그 외의 경우를 나눌 것
//
//                    /**
//                     * 1. 카페일경우
//                     */
//
//                    if (input_type == 10) {
//                        for (int j = 0; j < store.getMenus().size(); j++) {
//                            if (store.getMenus().get(j).getCount() != 0 && store.getMenus().get(j).getCount() > 0)
//                                order.add(store.getMenus().get(j));
//                            if (store.getMenus().get(j).getCountForIce() != 0 && store.getMenus().get(j).getCountForIce() > 0)
//                                order.add(store.getMenus().get(j));
//                        }
//
//                    } else {
//                        /**
//                         * 2. 그외일 경우
//                         */
                    for (int j = 0; j < store.getMenus().size(); j++) {
                        if (store.getMenus().get(j).getCount() != 0 && store.getMenus().get(j).getCount() > 0)
                            order.add(store.getMenus().get(j));
                    }
                    //params data setting
                    for (int j = 0; j < order.size(); j++) {
                        totalprice = totalprice + order.get(j).getCount() * Integer.parseInt(order.get(j).getMenu_price());
                        params_menucount = params_menucount + '#' + order.get(j).getCount();
                        params_idx_restaurantMenu = params_idx_restaurantMenu + '#' + order.get(j).getId();
                        params_menuKOR = params_menuKOR + '#' + order.get(j).getMenu_name_kor();
                        params_menuPrice = params_menuPrice + '#' + order.get(j).getMenu_price();
                        if (j == 0) {
                            //첫번째 데이터
                            params_data = order.get(j).getMenu_name_kor() + "#" + order.get(j).getMenu_price() + "#" + order.get(j).getCount();

                        } else {
                            params_data = params_data + "@" + order.get(j).getMenu_name_kor() + "#" + order.get(j).getMenu_price() + "#" + order.get(j).getCount();
                        }
                    }
//                    }//end 카페일반일때 데이터 셋팅 else

                    Intent i = new Intent(getApplicationContext(), FoodOrderAcitivty.class);

                    Gson gson = new Gson();
                    String jsonValue = gson.toJson(order);
                    PreferenceManager.getInstance(getApplicationContext()).setOrderdata(jsonValue);

//                    i.putParcelableArrayListExtra("ORDER", order);
                    i.putExtra("NAME", name);
                    startActivity(i);


                    //여기서 preorder하기
                    StringRequest req = new StringRequest(Request.Method.POST, new TDUrls().getStorePreOrderURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                        }
                    }, new Response.ErrorListener()

                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("menuactivity", error.getMessage());
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("id", order.get(0).getIdx_store() + "");
                            params.put("nameKor", store.getName_kor());
                            params.put("totalcount", order.size() + "");

                            new SaveExchangeRate().saveExchangeRate(getApplicationContext());
                            curr = Double.parseDouble(PreferenceManager.getInstance(getApplicationContext()).getCurrency());
                            sale = 0;
                            priceKor = food_menu_total_kor.getText().toString().replace("₩ ", "").replace(",", "");
                            priceChn = food_menu_total_chn.getText().toString().replace("¥ ", "").replace(",", "");
                            priceKorSale = Integer.parseInt(priceKor) * (100 - sale) / 100 + "";
                            priceKorSale = priceKorSale.replace(".0", "");
                            priceChnSale = new MakePaymentPrice().makePaymentPrice(priceKorSale, getApplicationContext(), curr) + "";

                            params.put("priceKor", priceKor);
                            params.put("priceChn", priceChn);
                            params.put("priceSaleKor", priceKorSale);
                            params.put("priceSaleChn", priceChnSale);
                            params.put("currency", curr + "");
                            params.put("sale", sale + "");
                            params.put("userIs", PreferenceManager.getInstance(getApplicationContext()).getUseris());
                            params.put("userFrom", PreferenceManager.getInstance(getApplicationContext()).getUserfrom());
                            params.put("os", "android");
                            params.put("userCode", PreferenceManager.getInstance(getApplicationContext()).getUsercode());
                            params.put("data", params_data);

                            return params;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(req);
                }
            }
        });


    }

    private void initialize() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);

        main_position = -1;
        sub_position = -1;
        extra_position = -1;
        drink_position = -1;


    }

    private void initView() {

        menu_main = (TextView) findViewById(R.id.food_menu_main);
        menu_sub = (TextView) findViewById(R.id.food_menu_sub);
        menu_extra = (TextView) findViewById(R.id.food_menu_extra);
        menu_drink = (TextView) findViewById(R.id.food_menu_drink);

        food_menu_total_count = (TextView) findViewById(R.id.food_menu_total_count);
        food_menu_total_kor = (TextView) findViewById(R.id.food_menu_total_kor);
        food_menu_total_chn = (TextView) findViewById(R.id.food_menu_total_chn);
        food_menu_pay_button = (Button) findViewById(R.id.food_menu_pay_button);

        food_menu_information = (ImageView) findViewById(R.id.food_menu_information);

/**
 * for menupopup
 */

        menu_popup = (LinearLayout) findViewById(R.id.menu_popup);
        menu_popup_check = (ImageView) findViewById(R.id.menu_popup_check);
        menu_popup_name_chn = (TextView) findViewById(R.id.menu_popup_name_chn);
        menu_popup_name_kor = (TextView) findViewById(R.id.menu_popup_name_kor);
        menu_popup_ice_price_kor = (TextView) findViewById(R.id.menu_popup_ice_price_kor);
        menu_popup_ice_price_chn = (TextView) findViewById(R.id.menu_popup_ice_price_chn);
        menu_popup_ice_count = (TextView) findViewById(R.id.menu_popup_ice_count);
        menu_popup_hot_price_kor = (TextView) findViewById(R.id.menu_popup_hot_price_kor);
        menu_popup_hot_price_chn = (TextView) findViewById(R.id.menu_popup_hot_price_chn);
        menu_popup_hot_count = (TextView) findViewById(R.id.menu_popup_hot_count);
        menu_popup_ice_minus_button = (ImageView) findViewById(R.id.menu_popup_ice_minus_button);
        menu_popup_ice_plus_button = (ImageView) findViewById(R.id.menu_popup_ice_plus_button);
        menu_popup_hot_minus_button = (ImageView) findViewById(R.id.menu_popup_hot_minus_button);
        menu_popup_hot_plus_button = (ImageView) findViewById(R.id.menu_popup_hot_plus_button);

        menu_popup_ice_layout = (RelativeLayout) findViewById(R.id.menu_popup_ice_layout);
        menu_popup_hot_layout = (RelativeLayout) findViewById(R.id.menu_popup_hot_layout);
        menu_popup_icehot_line = (View) findViewById(R.id.menu_popup_icehot_line);

/**
 * end for menupopup
 */

    }


    private void getStoreInfo(final int id) {

        if (!new IsOnline().onlineCheck(this)) {                  //internet check failed start
            Toast.makeText(this, "Internet Access Failed", Toast.LENGTH_SHORT).show();
        } else { //internet check success start
            showpDialog();
// GPS 사용유무 가져오기
//            if (gps.isGetLocation()) {
//
//                Thread gpsThread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        latitude = gps.getLatitude();
//                        longitude = gps.getLongitude();
//                    }
//                });
//                gpsThread.start();
//                try {
//                    gpsThread.join();  // 쓰레드 작업 끝날때까지 다른 작업들은 대기
//                } catch (Exception e) {
//                }
//
//            } else {
//                // GPS 를 사용할수 없으므로
//                if (!PreferenceManager.getInstance(getApplicationContext()).getLocationcheck().equals("OK")) {
//                    gps.showSettingsAlert();
//                }
//            }//end else system gps check
            url = new TDUrls().getStoreInfoURL + "?id=" + id + new UserLog().getLog(getApplicationContext());
            JsonObjectRequest objreq = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject res) {
                    try {
                        if (res.getString("result").equals("failed")) {//if result failed
                            Toast.makeText(getApplicationContext(),
                                    "Internet Access Failed", Toast.LENGTH_SHORT).show();
                        } else {

                            store = new StoreInfoData();

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
                                            map.put("idx_image_file_apth", Integer.parseInt(value));
                                            break;
                                        case "info_flag":
                                            if (value.equals("") || value.equals(null) || value.equals("null") || value.equals("NULL") || value == null)
                                                value = "0";
                                            map.put("info_flag", Integer.parseInt(value));
                                            break;
                                    }//end switch
                                }//end while
                                idx_images.add(map);
                            }
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
                                tmpValue = "";
                            store.setLatitude(tmpValue);

                            tmpValue = info.getString("longitude");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setLongitude(tmpValue);

                            tmpValue = info.getString("is_pay");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setIs_pay(Integer.parseInt(tmpValue));

                            tmpValue = info.getString("menu_input_type");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setMenu_input_type(tmpValue);
                            //TODO: input_type을 가져왔을때 카페형식인지 가져와서 저장해야 하는데 현재는 아이스핫이 등록된 부분이 없기때문에 스킵한다
//                            if (!tmpValue.equals(""))
//                                input_type = Integer.parseInt(tmpValue);

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


                    lv_menu = (ListView) findViewById(R.id.food_menu_listview);

//                    if (input_type == 10) {
//                        mCafeAdapter = new ResMenuCafeAdapter(getApplicationContext(), menus, FoodMenuActivity.this);
//                        lv_menu.setAdapter(mCafeAdapter);
//                    } else {
                    mAdapter = new StoreMenuAdapter(getApplicationContext(), store.getMenus(), StoreMenuActivity.this, input_type);
                    lv_menu.setAdapter(mAdapter);
//                    }
                    menu_main.setSelected(true);

                    if (main_position == -1) {
                        if (sub_position == -1) {
                            if (extra_position == -1) {
                                if (drink_position == -1) {
                                    fourposition = "0000";
                                    menu_sub.setVisibility(View.GONE);
                                    menu_extra.setVisibility(View.GONE);
                                    menu_drink.setVisibility(View.GONE);
                                } else {
                                    fourposition = "0001";
                                    menu_drink.setVisibility(View.GONE);
                                }
                            } else {
                                if (drink_position == -1) {
                                    fourposition = "0010";
                                    menu_extra.setVisibility(View.GONE);
                                } else {
                                    fourposition = "0011";
                                    menu_extra.setVisibility(View.GONE);
                                    menu_drink.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            if (extra_position == -1) {
                                if (drink_position == -1) {
                                    fourposition = "0100";
                                    menu_main.setVisibility(View.GONE);
                                    menu_extra.setVisibility(View.GONE);
                                    menu_drink.setVisibility(View.GONE);
                                } else {
                                    fourposition = "0101";
                                    menu_main.setVisibility(View.GONE);
                                    menu_extra.setVisibility(View.GONE);
                                }
                            } else {
                                if (drink_position == -1) {
                                    fourposition = "0110";
                                    menu_main.setVisibility(View.GONE);
                                    menu_drink.setVisibility(View.GONE);
                                } else {
                                    fourposition = "0111";
                                    menu_main.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else {
                        if (sub_position == -1) {
                            if (extra_position == -1) {
                                if (drink_position == -1) {
                                    fourposition = "1000";
                                    menu_sub.setVisibility(View.GONE);
                                    menu_extra.setVisibility(View.GONE);
                                    menu_drink.setVisibility(View.GONE);
                                } else {
                                    fourposition = "1001";
                                    menu_sub.setVisibility(View.GONE);
                                    menu_extra.setVisibility(View.GONE);
                                }
                            } else {
                                if (drink_position == -1) {
                                    fourposition = "1010";
                                    menu_sub.setVisibility(View.GONE);
                                    menu_drink.setVisibility(View.GONE);
                                } else {
                                    fourposition = "1011";
                                    menu_sub.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            if (extra_position == -1) {
                                if (drink_position == -1) {
                                    fourposition = "1100";
                                    menu_extra.setVisibility(View.GONE);
                                    menu_drink.setVisibility(View.GONE);
                                } else {
                                    fourposition = "1101";
                                    menu_extra.setVisibility(View.GONE);
                                }
                            } else {
                                if (drink_position == -1) {
                                    fourposition = "1110";
                                    menu_drink.setVisibility(View.GONE);
                                } else {
                                    fourposition = "1111";
                                }
                            }
                        }
                    }

                    //카테고리 선택시 해당으로 이동
                    menu_main.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lv_menu.setSelectionFromTop(main_position, 0);

                        }
                    });
                    menu_sub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lv_menu.setSelectionFromTop(sub_position, 0);

                        }
                    });
                    menu_extra.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lv_menu.setSelectionFromTop(extra_position, 0);

                        }
                    });
                    menu_drink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lv_menu.setSelectionFromTop(drink_position, 0);

                        }
                    });

                    lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Log.e("TNDN_LOG", menus.get(position).toString());

//                            Intent i = new Intent(getActivity(), ResDetailActivity.class);
//                            i.putExtra("IDX", menus.get(position).getIdx_restaurant());
//                            startActivity(i);
                        }
                    });

                    lv_menu.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
//                            View v = lv_menu.getChildAt(0);
                            currentScrollState = scrollState;
                            index = lv_menu.getFirstVisiblePosition();
                            int last = lv_menu.getLastVisiblePosition();
                            menuScroll(index, last);
//                            isScrollCompleted();
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            currentFirstVisibleItem = firstVisibleItem;
                            currentVisibleItemCount = visibleItemCount;
                            int index = lv_menu.getFirstVisiblePosition();
                            int last = lv_menu.getLastVisiblePosition();
                            menuScroll(index, last);
//                            Log.e("TNDN_LOG","fourposition   "+fourposition+"index    "+index+"   mainposition   "+main_position+"   sub_position   "+sub_position+"   extra_position   "+extra_position+"   drink_position   "+drink_position);
                        }

                        private void isScrollCompleted() {
                            if (currentVisibleItemCount > 0 && currentScrollState == SCROLL_STATE_IDLE) {
/*** In this way I detect if there's been a scroll which has completed ***/


                                menu_main.setSelected(false);
                                menu_sub.setSelected(false);
                                menu_extra.setSelected(false);
                                menu_drink.setSelected(false);
                                if (drink_position != -1) {
                                    menu_drink.setSelected(true);
                                } else {
                                    if (extra_position != -1) {
                                        menu_extra.setSelected(true);
                                    } else {
                                        if (sub_position != -1) {
                                            menu_sub.setSelected(true);
                                        } else {
                                            menu_main.setSelected(true);
                                        }
                                    }
                                }
                            }
                        }

                    });
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
    }   //end getStoreInfo


    //dialog
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void setTotalCount(boolean what, int price) {
        //true : plus
        //false : minus
        totalcount = Integer.parseInt(food_menu_total_count.getText().toString());
        totalprice = Integer.parseInt(food_menu_total_kor.getText().toString().replace(getResources().getString(R.string.curr_kor) + " ", "").replace(",", ""));


        //set total count and price
        if (what) {
            totalcount++;
            badgetCheck();
            totalprice += price;
            food_menu_total_count.setText(totalcount + "");
            food_menu_total_kor.setText(new MakePricePretty().makePricePretty(this, String.valueOf(totalprice), true));
            food_menu_total_chn.setText(new MakePricePretty().makePricePretty(this, String.valueOf(totalprice), false));

        } else if (!what) {
            if (totalcount < 0) {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            } else {
                totalcount--;
                badgetCheck();
                totalprice -= price;
                food_menu_total_count.setText(totalcount + "");
                food_menu_total_kor.setText(new MakePricePretty().makePricePretty(this, String.valueOf(totalprice), true));
                food_menu_total_chn.setText(new MakePricePretty().makePricePretty(this, String.valueOf(totalprice), false));
            }

        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }

    private void badgetCheck() {
        if (totalcount > 99) {
            food_menu_total_count.setBackgroundResource(R.drawable.shape_menu_cart_circle_big);
        } else {
            food_menu_total_count.setBackgroundResource(R.drawable.shape_menu_cart_circle);
        }
    }


    public void openCafeCheck(boolean check, final int position) {
        //check = true : 열려있음
        //check = false : 안보임
        if (check) {
            menu_popup.setVisibility(View.VISIBLE);

            menu_popup_name_chn.setText(store.getMenus().get(position).getMenu_name_chn());
            menu_popup_name_kor.setText(store.getMenus().get(position).getMenu_name_kor());

            icecount = 0;
            hotcount = 0;

            if (store.getMenus().get(position).getMenu_price().equals("0") || store.getMenus().get(position).getMenu_price().equals("")) {
                //ice가격만 있을 때
                menu_popup_ice_layout.setVisibility(View.VISIBLE);
                menu_popup_hot_layout.setVisibility(View.GONE);
                menu_popup_icehot_line.setVisibility(View.GONE);

                menu_popup_ice_price_kor.setText(new MakePricePretty().makePricePretty(getApplicationContext(), store.getMenus().get(position).getMenu_price_for_ice(), true));
                menu_popup_ice_price_chn.setText(new MakePricePretty().makePricePretty(getApplicationContext(), store.getMenus().get(position).getMenu_price_for_ice(), false));

                menu_popup_ice_count.setText(store.getMenus().get(position).getCountForIce() + "");

                if (store.getMenus().get(position).getCountForIce() != 0) {
                    menu_popup_ice_count.setTextColor(getResources().getColor(R.color.tndn_pink));
                } else {
                    menu_popup_ice_count.setTextColor(getResources().getColor(R.color.gray_9b));
                }

                menu_popup_ice_plus_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (store.getMenus().get(position).getMenu_price_for_ice().equals(0)) {
                            return;
                        }
                        icecount = store.getMenus().get(position).getCountForIce();
                        if (icecount > 29) {
                            return;
                        } else {
                            icecount++;
                            menu_popup_ice_count.setText(icecount + "");

                            store.getMenus().get(position).setCountForIce(icecount);
                            setTotalCount(true, Integer.parseInt(store.getMenus().get(position).getMenu_price_for_ice()));

                            if (store.getMenus().get(position).getCountForIce() != 0) {
                                menu_popup_ice_count.setTextColor(getResources().getColor(R.color.tndn_pink));
                            } else {
                                menu_popup_ice_count.setTextColor(getResources().getColor(R.color.gray_9b));
                            }
                        }
                    }
                });

                menu_popup_ice_minus_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (store.getMenus().get(position).getMenu_price_for_ice().equals(0)) {
                            return;
                        }
                        icecount = store.getMenus().get(position).getCountForIce();
                        if (icecount <= 0) {
                            return;
                        } else {
                            icecount--;
                            menu_popup_ice_count.setText(icecount + "");

                            store.getMenus().get(position).setCountForIce(icecount);
                            setTotalCount(false, Integer.parseInt(store.getMenus().get(position).getMenu_price_for_ice()));

                            if (store.getMenus().get(position).getCountForIce() != 0) {
                                menu_popup_ice_count.setTextColor(getResources().getColor(R.color.tndn_pink));
                            } else {
                                menu_popup_ice_count.setTextColor(getResources().getColor(R.color.gray_9b));
                            }
                        }
                    }
                });
            } else if (store.getMenus().get(position).getMenu_price_for_ice().equals("0") || store.getMenus().get(position).getMenu_price_for_ice().equals("")) {
                //hot 가격만 있을때
                menu_popup_hot_layout.setVisibility(View.VISIBLE);
                menu_popup_ice_layout.setVisibility(View.GONE);
                menu_popup_icehot_line.setVisibility(View.GONE);

                menu_popup_hot_price_kor.setText(new MakePricePretty().makePricePretty(getApplicationContext(), store.getMenus().get(position).getMenu_price(), true));
                menu_popup_hot_price_chn.setText(new MakePricePretty().makePricePretty(getApplicationContext(), store.getMenus().get(position).getMenu_price(), false));

                menu_popup_hot_count.setText(store.getMenus().get(position).getCount() + "");
                if (store.getMenus().get(position).getCount() != 0) {
                    menu_popup_hot_count.setTextColor(getResources().getColor(R.color.tndn_pink));
                } else {
                    menu_popup_hot_count.setTextColor(getResources().getColor(R.color.gray_9b));
                }


                menu_popup_hot_plus_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (store.getMenus().get(position).getMenu_price().equals(0)) {
                            return;
                        }
                        hotcount = store.getMenus().get(position).getCount();
                        if (hotcount > 29) {
                            return;
                        } else {
                            hotcount++;
                            menu_popup_hot_count.setText(hotcount + "");

                            store.getMenus().get(position).setCount(hotcount);
                            setTotalCount(true, Integer.parseInt(store.getMenus().get(position).getMenu_price()));

                            if (store.getMenus().get(position).getCount() != 0) {
                                menu_popup_hot_count.setTextColor(getResources().getColor(R.color.tndn_pink));
                            } else {
                                menu_popup_hot_count.setTextColor(getResources().getColor(R.color.gray_9b));
                            }
                        }
                    }
                });

                menu_popup_hot_minus_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (store.getMenus().get(position).getMenu_price().equals(0)) {
                            return;
                        }
                        hotcount = store.getMenus().get(position).getCount();
                        if (hotcount <= 0) {
                            return;
                        } else {
                            hotcount--;
                            menu_popup_hot_count.setText(hotcount + "");

                            store.getMenus().get(position).setCount(hotcount);
                            setTotalCount(false, Integer.parseInt(store.getMenus().get(position).getMenu_price()));

                            if (store.getMenus().get(position).getCount() != 0) {
                                menu_popup_hot_count.setTextColor(getResources().getColor(R.color.tndn_pink));
                            } else {
                                menu_popup_hot_count.setTextColor(getResources().getColor(R.color.gray_9b));
                            }
                        }
                    }
                });

            } else {
                //ice/hot 가격 두개다 있을때
                menu_popup_ice_layout.setVisibility(View.VISIBLE);
                menu_popup_hot_layout.setVisibility(View.VISIBLE);
                menu_popup_icehot_line.setVisibility(View.VISIBLE);

                menu_popup_ice_price_kor.setText(new MakePricePretty().makePricePretty(getApplicationContext(), store.getMenus().get(position).getMenu_price_for_ice(), true));
                menu_popup_ice_price_chn.setText(new MakePricePretty().makePricePretty(getApplicationContext(), store.getMenus().get(position).getMenu_price_for_ice(), false));

                menu_popup_hot_count.setText(store.getMenus().get(position).getCount() + "");
                menu_popup_ice_count.setText(store.getMenus().get(position).getCountForIce() + "");

                if (store.getMenus().get(position).getCount() != 0) {
                    menu_popup_hot_count.setTextColor(getResources().getColor(R.color.tndn_pink));
                } else {
                    menu_popup_hot_count.setTextColor(getResources().getColor(R.color.gray_9b));
                }

                if (store.getMenus().get(position).getCountForIce() != 0) {
                    menu_popup_ice_count.setTextColor(getResources().getColor(R.color.tndn_pink));
                } else {
                    menu_popup_ice_count.setTextColor(getResources().getColor(R.color.gray_9b));
                }

                menu_popup_ice_plus_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (store.getMenus().get(position).getMenu_price_for_ice().equals(0)) {
                            return;
                        }
                        icecount = store.getMenus().get(position).getCountForIce();
                        if (icecount > 29) {
                            return;
                        } else {
                            icecount++;
                            menu_popup_ice_count.setText(icecount + "");

                            store.getMenus().get(position).setCountForIce(icecount);
                            setTotalCount(true, Integer.parseInt(store.getMenus().get(position).getMenu_price_for_ice()));

                            if (store.getMenus().get(position).getCountForIce() != 0) {
                                menu_popup_ice_count.setTextColor(getResources().getColor(R.color.tndn_pink));
                            } else {
                                menu_popup_ice_count.setTextColor(getResources().getColor(R.color.gray_9b));
                            }
                        }
                    }
                });

                menu_popup_ice_minus_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (store.getMenus().get(position).getMenu_price_for_ice().equals(0)) {
                            return;
                        }
                        icecount = store.getMenus().get(position).getCountForIce();
                        if (icecount <= 0) {
                            return;
                        } else {
                            icecount--;
                            menu_popup_ice_count.setText(icecount + "");

                            store.getMenus().get(position).setCountForIce(icecount);
                            setTotalCount(false, Integer.parseInt(store.getMenus().get(position).getMenu_price_for_ice()));

                            if (store.getMenus().get(position).getCountForIce() != 0) {
                                menu_popup_ice_count.setTextColor(getResources().getColor(R.color.tndn_pink));
                            } else {
                                menu_popup_ice_count.setTextColor(getResources().getColor(R.color.gray_9b));
                            }
                        }
                    }
                });

                menu_popup_hot_price_kor.setText(new MakePricePretty().makePricePretty(getApplicationContext(), store.getMenus().get(position).getMenu_price(), true));
                menu_popup_hot_price_chn.setText(new MakePricePretty().makePricePretty(getApplicationContext(), store.getMenus().get(position).getMenu_price(), false));

                menu_popup_hot_plus_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (store.getMenus().get(position).getMenu_price().equals(0)) {
                            return;
                        }
                        hotcount = store.getMenus().get(position).getCount();
                        if (hotcount > 29) {
                            return;
                        } else {
                            hotcount++;
                            menu_popup_hot_count.setText(hotcount + "");

                            store.getMenus().get(position).setCount(hotcount);
                            setTotalCount(true, Integer.parseInt(store.getMenus().get(position).getMenu_price()));

                            if (store.getMenus().get(position).getCount() != 0) {
                                menu_popup_hot_count.setTextColor(getResources().getColor(R.color.tndn_pink));
                            } else {
                                menu_popup_hot_count.setTextColor(getResources().getColor(R.color.gray_9b));
                            }
                        }
                    }
                });

                menu_popup_hot_minus_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (store.getMenus().get(position).getMenu_price().equals(0)) {
                            return;
                        }
                        hotcount = store.getMenus().get(position).getCount();
                        if (hotcount <= 0) {
                            return;
                        } else {
                            hotcount--;
                            menu_popup_hot_count.setText(hotcount + "");

                            store.getMenus().get(position).setCount(hotcount);
                            setTotalCount(false, Integer.parseInt(store.getMenus().get(position).getMenu_price()));

                            if (store.getMenus().get(position).getCount() != 0) {
                                menu_popup_hot_count.setTextColor(getResources().getColor(R.color.tndn_pink));
                            } else {
                                menu_popup_hot_count.setTextColor(getResources().getColor(R.color.gray_9b));
                            }
                        }
                    }
                });

            }
        } else {
            menu_popup.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void menuScroll(int index, int last) {
/**
 0000
 0001
 0010
 0011
 0100
 0101
 0110
 0111
 1000
 1001
 1010
 1011
 1100
 1101
 1110
 1111
 */

        menu_main.setSelected(false);
        menu_sub.setSelected(false);
        menu_extra.setSelected(false);
        menu_drink.setSelected(false);

        if (fourposition.equals("0000")) {
            menu_main.setSelected(true);
        }
        if (fourposition.equals("0001")) {
            menu_drink.setSelected(true);
        }
        if (fourposition.equals("0010")) {
            menu_extra.setSelected(true);
        }
        if (fourposition.equals("0011")) {
//extra, drink
            if (index >= extra_position && index < drink_position) {
                menu_extra.setSelected(true);
            } else if (index >= drink_position) {
                menu_drink.setSelected(true);
            }

        }
        if (fourposition.equals("0100")) {
            menu_sub.setSelected(true);
        }
        if (fourposition.equals("0101")) {
            if (index >= sub_position && index < drink_position) {
                menu_sub.setSelected(true);
            } else if (index >= drink_position) {
                menu_drink.setSelected(true);

            }

        }
        if (fourposition.equals("0110")) {
            if (index >= sub_position && index < extra_position) {
                menu_sub.setSelected(true);
            } else if (index >= extra_position) {
                menu_extra.setSelected(true);

            }

        }
        if (fourposition.equals("0111")) {
            if (index >= sub_position && index < extra_position) {
                menu_sub.setSelected(true);
            } else if (index >= extra_position && index < drink_position) {
                menu_extra.setSelected(true);
            } else if (index >= drink_position) {
                menu_drink.setSelected(true);

            }

        }
        if (fourposition.equals("1000")) {
            menu_main.setSelected(true);
        }
        if (fourposition.equals("1001")) {
            if (index >= main_position && index < drink_position) {
                menu_main.setSelected(true);
            } else if (index >= drink_position) {
                menu_drink.setSelected(true);

            }

        }
        if (fourposition.equals("1010")) {
            if (index >= main_position && index < extra_position) {
                menu_main.setSelected(true);
            } else if (index >= extra_position) {
                menu_extra.setSelected(true);

            }

        }
        if (fourposition.equals("1011")) {
            if (index >= main_position && index < extra_position) {
                menu_main.setSelected(true);
            } else if (index >= extra_position && index < drink_position) {
                menu_extra.setSelected(true);
            } else if (index >= drink_position) {
                menu_drink.setSelected(true);

            }

        }
        if (fourposition.equals("1100")) {
            if (index >= main_position && index < sub_position) {
                menu_main.setSelected(true);
            } else if (index >= sub_position) {
                menu_sub.setSelected(true);

            }

        }
        if (fourposition.equals("1101")) {
            if (index >= main_position && index < sub_position) {
                menu_main.setSelected(true);
            } else if (index >= sub_position && index < drink_position) {
                menu_sub.setSelected(true);
            } else if (index >= drink_position) {
                menu_drink.setSelected(true);

            }

        }
        if (fourposition.equals("1110")) {
            if (index >= main_position && index < sub_position) {
                menu_main.setSelected(true);
            } else if (index >= sub_position && index < extra_position) {
                menu_sub.setSelected(true);
            } else if (index >= extra_position) {
                menu_extra.setSelected(true);

            }

        }
        if (fourposition.equals("1111")) {
            if (index >= main_position && index < sub_position) {
                menu_main.setSelected(true);
            } else if (index >= sub_position && index < extra_position) {
                menu_sub.setSelected(true);
            } else if (index >= extra_position && index < drink_position) {
                menu_extra.setSelected(true);
            } else if (index >= drink_position) {
                menu_drink.setSelected(true);

            }
        }

    }

}
