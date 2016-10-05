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
import android.widget.GridView;
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
import tndn.app.nyam.utils.GpsInfo;
import tndn.app.nyam.utils.IsOnline;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;

public class MagazineSquareActivity extends AppCompatActivity {

    private GridView magazine_square_gridview;
    private MagazineSquareAdapter mSquareAdapter;

    //    for gps
    private GpsInfo gps;
    double latitude;
    double longitude;

    private ProgressDialog pDialog;

    //    for sites
    ArrayList<StoreListData> stores;
    private String url;


    //for popup
    private LinearLayout magazine_square_popup;
    private TextView magazine_square_popup_name;
    private TextView magazine_square_popup_distance;
    private ImageView magazine_square_popup_go;
    private NetworkImageView magazine_square_popup_imageview;


    private ImageLoader mImageLoader;

    int cPosition;

    //    for intent
    int idx = 0;

    /**
     * value
     */
    private int page;
    private String localizationId;
    /**
     * unlimited scroll
     */
    boolean lastitemVisibleFlag = false;        //화면에 리스트의 마지막 아이템이 보여지는지 체크
    int listview_position = 0;
    boolean lastitemFinishedFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine_square);

        /********************************************
         for actionbar
         ********************************************/
        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);

        actionbar_text.setText(getResources().getString(R.string.square_attraction));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /********************************************
         for actionbar
         ********************************************/
        initView();
        initialize();
        localizationId = PreferenceManager.getInstance(getApplicationContext()).getLocalization();

        Intent i = getIntent();
        if (Intent.ACTION_VIEW.equals(i.getAction())) {
            //카테고리에서 아이템 클릭이나 홈에서 아이템 클릭
            Uri uri = i.getData();
            if (uri.getQueryParameter("idx") == null) {
                idx = 0;
            } else {
                idx = Integer.parseInt(uri.getQueryParameter("idx"));
            }
        } else {
            //에러 혹은 큐알로 바로 찍어서 들어왔을때
//            idx = i.getIntExtra("IDX", 0);
            idx = 0;
        }

        getStoreList(localizationId, page, false);
        magazine_square_gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    getStoreList(localizationId, page, true);

                }
            }
        });
        magazine_square_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magazine_square_popup.setVisibility(View.GONE);
            }
        });
    }

    private void initView() {
        magazine_square_gridview = (GridView) findViewById(R.id.magazine_square_gridview);
        magazine_square_popup = (LinearLayout) findViewById(R.id.magazine_square_popup);
        magazine_square_popup_name = (TextView) findViewById(R.id.magazine_square_popup_name);
        magazine_square_popup_distance = (TextView) findViewById(R.id.magazine_square_popup_distance);
        magazine_square_popup_go = (ImageView) findViewById(R.id.magazine_square_popup_go);
        magazine_square_popup_imageview = (NetworkImageView) findViewById(R.id.magazine_square_popup_imageview);

    }

    private void initialize() {

        page = 0;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);

        stores = new ArrayList<StoreListData>();

        mImageLoader = AppController.getInstance().getImageLoader();
        gps = new GpsInfo(this);
    }


    /**
     * Internet Access
     */
    private void getStoreList(String localizationId, final int page, final boolean chkreloading) {

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

            url = new TDUrls().getStoreListURL + "?localizationId=" + localizationId + "&orderBy=random&page=" + page + "&mlat=" + latitude + "&mlon=" + longitude + new UserLog().getLog(getApplicationContext());
            JsonObjectRequest objreq = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject res) {

                    try {
                        if (res.getString("result").equals("failed")) {//else result failed

                            if (page == 0) {
                                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                lastitemFinishedFlag = true;
                            }


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
                                            if (value.equals("") || value.equals("null") || value.equals("NULL")) {
                                                value = "0";
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


                    mSquareAdapter = new MagazineSquareAdapter(MagazineSquareActivity.this, stores);
                    magazine_square_gridview.setAdapter(mSquareAdapter);

                    if (chkreloading) {
                        magazine_square_gridview.setSelection(listview_position);
                    }


                    magazine_square_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            cPosition = position;
                            magazine_square_popup.setVisibility(View.VISIBLE);
                            magazine_square_popup_name.setText(stores.get(cPosition).getName_chn());
                            magazine_square_popup_distance.setText(stores.get(cPosition).getDistance() + "km");
                            magazine_square_popup_go.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = "tndn://getStoreInfo?mainId=3&id=" + stores.get(cPosition).getIdx_store();
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(intent);
                                }
                            });
                            magazine_square_popup_imageview.setImageUrl(new TDUrls().getImageURL + "?size=800&id=" + stores.get(cPosition).getIdx_image_file_path(), mImageLoader);

                        }
                    });

                    //magazinehomeActivity에서 square를 눌러서 중간으로 들어온 경우
                    if (idx != 0) {
                        magazine_square_popup.setVisibility(View.VISIBLE);
                        cPosition = 0;
                        for (int i = 0; i < stores.size(); i++) {
                            if (idx == stores.get(i).getIdx_store()) {
                                cPosition = i;
                            }
                        }
                        magazine_square_gridview.setSelection(cPosition);
                        magazine_square_popup_name.setText(stores.get(cPosition).getName_chn());
                        magazine_square_popup_distance.setText(stores.get(cPosition).getDistance() + "km");
                        magazine_square_popup_go.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = "tndn://getStoreInfo?mainId=3&id=" + stores.get(cPosition).getIdx_store();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                            }
                        });
                        magazine_square_popup_imageview.setImageUrl(new TDUrls().getImageURL + "?size=800&id=" + stores.get(cPosition).getIdx_image_file_path(), mImageLoader);

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

    private void setPopupData(final ArrayList<StoreListData> mSites) {

    }

}
