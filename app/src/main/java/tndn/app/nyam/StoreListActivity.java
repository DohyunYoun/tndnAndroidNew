package tndn.app.nyam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import tndn.app.nyam.adapter.StoreListAdapter;
import tndn.app.nyam.data.StoreListData;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.GpsInfo;
import tndn.app.nyam.utils.IsOnline;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;

/**
 * Created by YounDH on 2016-09-29.
 */
public class StoreListActivity extends AppCompatActivity {

    /**
     * service
     */
    private ProgressDialog pDialog;
    private String url;

    /**
     * view
     */
    private ImageView store_list_orderby_distance;
    private ImageView store_list_orderby_popular;
    private ImageView store_list_filter;
    private ListView store_list_listview;

    /**
     * listView
     */
    ArrayList<StoreListData> stores;
    private StoreListAdapter mAdapter;

    /**
     * gps
     */
    private GpsInfo gps;
    double latitude;
    double longitude;

    /**
     * value
     */
    private String mainId = "";
    private String locationId = "";
    private String foodId = "";
    private int localizationId = 1;
    private String orderBy;
    private int page;

    /**
     * unlimited scroll
     */
    boolean lastitemVisibleFlag = false;        //화면에 리스트의 마지막 아이템이 보여지는지 체크
    int listview_position = 0;
    boolean lastitemFinishedFlag = false;

    /**
     * activity 종료
     */
    public static StoreListActivity storeListActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
/********************************************
 for actionbar
 ********************************************/

        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);
        Button refresh = (Button) findViewById(R.id.actionbar_refresh_button);

        actionbar_text.setText(getResources().getString(R.string.category));
        refresh.setVisibility(View.VISIBLE);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getInstance(getApplicationContext()).setLocationcheck("");
//                stores = new ArrayList<StoreListData>();
//                page = 0;
//                getStoreList(mainId, locationId, foodId, orderBy, localizationId, page, false);
                startActivity(new Intent(getApplicationContext(), StoreListActivity.class));
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
/********************************************
 for actionbar
 ********************************************/

        storeListActivity = StoreListActivity.this;

        initialize();
        initView();

        /**
         * initialize
         */
        locationId = PreferenceManager.getInstance(getApplicationContext()).getLocationId();
        foodId = PreferenceManager.getInstance(getApplicationContext()).getFoodId();
        if (PreferenceManager.getInstance(getApplicationContext()).getLocalization().equals("")) {
            localizationId = 1;
        } else {
            localizationId = Integer.parseInt(PreferenceManager.getInstance(getApplicationContext()).getLocalization());
        }

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            mainId = uri.getQueryParameter("mainId");
            if (mainId == null || mainId.equals("") || mainId.equals("undefined") || mainId.equals("null"))
                mainId = "";
        }
        if (PreferenceManager.getInstance(getApplicationContext()).getOrderby().equals("")) {
            PreferenceManager.getInstance(this).setOrderby("distance");
        }
        orderBy = PreferenceManager.getInstance(getApplicationContext()).getOrderby();
        if (orderBy.equals("popular")) {
            store_list_orderby_popular.setSelected(true);
        } else {
            store_list_orderby_distance.setSelected(true);
        }

        /**
         * Call List
         */
        getStoreList(mainId, locationId, foodId, orderBy, localizationId, page, false);


        /**
         *unlimited scroll
         */

        store_list_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
                listview_position = firstVisibleItem + 1;
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
                //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag && !lastitemFinishedFlag) {
                    //TODO 화면이 바닦에 닿을때 처리
                    page = page + 20;
                    getStoreList(mainId, locationId, foodId, orderBy, localizationId, page, true);
                }
            }

        });

        //go to filter
        store_list_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "tndn://storeListFilter?mainId=" + mainId;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
//                startActivity(new Intent(getApplicationContext(), StoreListFilterActivity.class));

            }
        });

        //order by distance
        store_list_orderby_distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!orderBy.equals("distance")) {
                    locationId = PreferenceManager.getInstance(getApplicationContext()).getLocationId();
                    foodId = PreferenceManager.getInstance(getApplicationContext()).getFoodId();
                    PreferenceManager.getInstance(getApplicationContext()).setOrderby("distance");
                    orderBy = "distance";
                    stores = new ArrayList<StoreListData>();
                    page = 0;

                    getStoreList(mainId, locationId, foodId, orderBy, localizationId, page, false);
                    store_list_orderby_distance.setSelected(true);
                    store_list_orderby_popular.setSelected(false);
                }
            }
        });


        //order by popular
        store_list_orderby_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!orderBy.equals("popular")) {
                    locationId = PreferenceManager.getInstance(getApplicationContext()).getLocationId();
                    foodId = PreferenceManager.getInstance(getApplicationContext()).getFoodId();
                    PreferenceManager.getInstance(getApplicationContext()).setOrderby("popular");
                    orderBy = "popular";
                    stores = new ArrayList<StoreListData>();
                    page = 0;

                    getStoreList(mainId, locationId, foodId, orderBy, localizationId, page, false);
                    store_list_orderby_distance.setSelected(false);
                    store_list_orderby_popular.setSelected(true);
                }
            }
        });
    }

    private void initialize() {

        locationId = "";
        foodId = "";
        localizationId = 1;
        orderBy = "";
        page = 0;

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);

        gps = new GpsInfo(this);

        stores = new ArrayList<StoreListData>();


    }

    private void initView() {
        store_list_orderby_distance = (ImageView) findViewById(R.id.store_list_orderby_distance);
        store_list_orderby_popular = (ImageView) findViewById(R.id.store_list_orderby_popular);
        store_list_filter = (ImageView) findViewById(R.id.store_list_filter);

        store_list_listview = (ListView) findViewById(R.id.store_list_listview);

    }

    /**
     * Internet Access
     */
    private void getStoreList(final String mainId, String locationId, String classifyId, String orderBy, int localizationId, final int page, final boolean chkreloading) {

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

            url = new TDUrls().getStoreListURL + "?mainId=" + mainId + "&locationId=" + locationId + "&classifyId=" + classifyId + "&orderBy=" + orderBy + "&localizationId=" + localizationId + "&page=" + page + "&mlat=" + latitude + "&mlon=" + longitude + new UserLog().getLog(getApplicationContext());
            JsonObjectRequest objreq = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject res) {

                    try {
                        if (res.getString("result").equals("failed")) {//else result failed
                            if (page == 0) {
                                findViewById(R.id.store_list_error).setVisibility(View.VISIBLE);
                            } else {
                                lastitemFinishedFlag = true;
                            }
                        } else {
                            findViewById(R.id.store_list_error).setVisibility(View.INVISIBLE);

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
                                            if (value.equals("") || value.equals("null") || value.equals("NULL")) {
                                                if (mainId.equals("1")) {
                                                    value = "0";
                                                } else if (mainId.equals("2")) {
                                                    value = "0";
                                                } else if (mainId.equals("3")) {
                                                    value = "7630";
                                                } else {
                                                    value = "0";
                                                }
                                            }
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
                    mAdapter = new StoreListAdapter(StoreListActivity.this, stores);
                    store_list_listview = (ListView) findViewById(R.id.store_list_listview);
                    store_list_listview.setAdapter(mAdapter);
                    if (chkreloading) {
                        store_list_listview.setSelection(listview_position);
                    }
                    store_list_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                            String url = new TDUrls().getIntentURL("getShop", stores.get(position).getIdx_store()) + "&input_type=" + stores.get(position).getMenu_input_type() + "&resName=" + stores.get(position).getName_chn();
                            String url = "tndn://getStoreInfo?mainId=" + mainId + "&id=" + stores.get(position).getIdx_store() + "&inputType=" + stores.get(position).getMenu_input_type() + "&nameChn=" + stores.get(position).getName_chn();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);

                        }
                    });
                }   //end response

            }

                    , new Response.ErrorListener()

            {   //end request

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),
                            "Internet Access Failed", Toast.LENGTH_SHORT).show();
                    //hide the progress dialog
                    hidepDialog();
                }
            }

            );

            // Adding request to request queue

            AppController.getInstance().

                    addToRequestQueue(objreq);
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
}
