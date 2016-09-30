package tndn.app.nyam.map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import tndn.app.nyam.R;
import tndn.app.nyam.data.StoreInfoData;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.GpsInfo;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;


public class MapRoutActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView actionbar_text;
    private Button back;

    private ProgressDialog pDialog;


    private EditText map_route_start_edittext;
    private ImageView map_route_search_start;
    private EditText map_route_end_edittext;
    private ImageView map_route_search_end;
    private LinearLayout car;
    private LinearLayout bus;
    private LinearLayout walk;
    private View map_route_line_car;
    private View map_route_line_bus;
    private View map_route_line_walk;
    private ImageView map_route_reverse;
    private ImageView map_route_findroad;

    private MapSearchResultData s_mapresult;
    private MapSearchResultData e_mapresult;

    private Gson gson;

    private int what;

    //activity 삭제
    public static MapRoutActivity maprouteActivity;

    private ArrayList<MapRoutNodeData> routNodes;
    private ArrayList<MapRoutLinkData> routLinks;
    private ArrayList<MapPublicPathData> pathDatas;
    private MapPublicPathEtcData pathEtcData;
    private ArrayList<MapPointsData> walk_points;


    String cost;
    String time;
    String dist;

    String walk_dist;
    String walk_time;
    int walk_code;

    float currentX = 0;
    float currentY = 0;
    private GpsInfo gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_rout);

        initView();
        initialize();

        maprouteActivity = MapRoutActivity.this;
        if (PreferenceManager.getInstance(this).getFrominfo().equals("OK")) {
            //shopinfo에서 넘어온 데이터. 도착지로 세팅해주기.
            Intent infoIntent = getIntent();
            StoreInfoData shop = infoIntent.getParcelableExtra("STORE");

            if (gps.isGetLocation()) {

                Thread gpsThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        currentX = (float) gps.getLongitude();
                        currentY = (float) gps.getLatitude();
                    }
                });
                gpsThread.start();


                try {
                    gpsThread.join();  // 쓰레드 작업 끝날때까지 다른 작업들은 대기
                } catch (Exception e) {
                }
                s_mapresult.setX(currentX);
                s_mapresult.setY(currentY);
                s_mapresult.setBemd_kor(" ");
                s_mapresult.setHemd_kor(" ");
                s_mapresult.setSgg_kor(" ");
                s_mapresult.setCateNm(" ");
                s_mapresult.setName(getResources().getString(R.string.current_location));
                //我的位置
                s_mapresult.setName_kor("현위치");
                s_mapresult.setCateNmKor(" ");
                s_mapresult.setSido_kor(" ");
                s_mapresult.setSido_chn(" ");
                s_mapresult.setSgg_chn(" ");
                s_mapresult.setHemd_chn(" ");
                map_route_start_edittext.setText(s_mapresult.getName());

                String json = gson.toJson(s_mapresult);
                PreferenceManager.getInstance(MapRoutActivity.this).setMapstartdata(json);

            } else {
                // GPS 를 사용할수 없으므로
                AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
                gsDialog.setTitle("GPS");
                gsDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_gps));
                gsDialog.setPositiveButton(getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        // GPS설정 화면으로 이동
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivity(intent);
                        finish();
                    }
                })
                        .setNegativeButton(getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                return;
                            }
                        }).create().show();
            }


            e_mapresult.setX(Float.parseFloat(shop.getLongitude()));
            e_mapresult.setY(Float.parseFloat(shop.getLatitude()));
            e_mapresult.setBemd_kor(" ");
            e_mapresult.setHemd_kor(" ");
            e_mapresult.setSgg_kor(" ");
            e_mapresult.setCateNm(" ");
            e_mapresult.setName(shop.getName_chn());
            e_mapresult.setName_kor(shop.getName_kor());
            e_mapresult.setCateNmKor(shop.getClassify_chn());
            e_mapresult.setSido_kor(shop.getAddress_kor());
            e_mapresult.setSido_chn(" ");
            e_mapresult.setSgg_chn(" ");
            e_mapresult.setHemd_chn(" ");

            map_route_end_edittext.setText(e_mapresult.getName());

            String json = gson.toJson(e_mapresult);
            PreferenceManager.getInstance(MapRoutActivity.this).setMapenddata(json);
            PreferenceManager.getInstance(this).setFrominfo("");
        } else {
            Intent intent2 = getIntent();

            currentX = intent2.getFloatExtra("CURRENTX", 0);
            currentY = intent2.getFloatExtra("CURRENTY", 0);

            if (currentX == 0 || currentX == 0.0 || currentY == 0 || currentY == 0.0) {

            } else {
                //값이 무언가 들어있으니 출발을 현위치를 잡아줘

                s_mapresult.setX(currentX);
                s_mapresult.setY(currentY);
                s_mapresult.setBemd_kor(" ");
                s_mapresult.setHemd_kor(" ");
                s_mapresult.setSgg_kor(" ");
                s_mapresult.setCateNm(" ");
                s_mapresult.setName(getResources().getString(R.string.current_location));
                s_mapresult.setName_kor("현위치");
                s_mapresult.setCateNmKor(" ");
                s_mapresult.setSido_kor(" ");
                s_mapresult.setSido_chn(" ");
                s_mapresult.setSgg_chn(" ");
                s_mapresult.setHemd_chn(" ");
                map_route_start_edittext.setText(s_mapresult.getName());

                String json = gson.toJson(s_mapresult);
                PreferenceManager.getInstance(MapRoutActivity.this).setMapstartdata(json);
            }

            if (!PreferenceManager.getInstance(MapRoutActivity.this).getMaprouteintent().equals("")) {
                //start 나 end 가 왔어.
                if (!PreferenceManager.getInstance(MapRoutActivity.this).getMapstartdata().equals("")) {
                    //start data가 들어있어

                    if (PreferenceManager.getInstance(MapRoutActivity.this).getMaprouteintent().equals("START")) {
                        //start data가 들어있는데 start req가 또 왔어
                        Intent i = getIntent();
                        s_mapresult = i.getParcelableExtra("MAPRESULT");
                        map_route_start_edittext.setText(s_mapresult.getName());
                        String json = gson.toJson(s_mapresult);
                        PreferenceManager.getInstance(MapRoutActivity.this).setMapstartdata(json);
                        if (!PreferenceManager.getInstance(MapRoutActivity.this).getMapenddata().equals("")) {
                            //start data가 들어있고, start req가 왔는데 이미 end req가 있어
                            e_mapresult = gson.fromJson(PreferenceManager.getInstance(MapRoutActivity.this).getMapenddata(), MapSearchResultData.class);
                            map_route_end_edittext.setText(e_mapresult.getName());
                        }

                    } else if (PreferenceManager.getInstance(MapRoutActivity.this).getMaprouteintent().equals("END")) {
                        //start data가 들어있는데 end req가 왔어
                        Intent i = getIntent();
                        e_mapresult = i.getParcelableExtra("MAPRESULT");
                        s_mapresult = gson.fromJson(PreferenceManager.getInstance(MapRoutActivity.this).getMapstartdata(), MapSearchResultData.class);
                        map_route_start_edittext.setText(s_mapresult.getName());
                        map_route_end_edittext.setText(e_mapresult.getName());

                        String json = gson.toJson(e_mapresult);
                        PreferenceManager.getInstance(MapRoutActivity.this).setMapenddata(json);

                    } else {
                        Log.e("TNDN_LOG", "------------------error-----------------");
                    }
                } else if (!PreferenceManager.getInstance(MapRoutActivity.this).getMapenddata().equals("")) {
                    //end data가 들어있어

                    if (PreferenceManager.getInstance(MapRoutActivity.this).getMaprouteintent().equals("START")) {
                        //end data가 들어있는데 start req가 왔어
                        Intent i = getIntent();
                        s_mapresult = i.getParcelableExtra("MAPRESULT");
                        e_mapresult = gson.fromJson(PreferenceManager.getInstance(MapRoutActivity.this).getMapenddata(), MapSearchResultData.class);
                        map_route_start_edittext.setText(s_mapresult.getName());
                        map_route_end_edittext.setText(e_mapresult.getName());

                        String json = gson.toJson(s_mapresult);
                        PreferenceManager.getInstance(MapRoutActivity.this).setMapstartdata(json);
                    } else if (PreferenceManager.getInstance(MapRoutActivity.this).getMaprouteintent().equals("END")) {
                        //end data가 들어있는데 end req가 또 왔어
                        Intent i = getIntent();
                        e_mapresult = i.getParcelableExtra("MAPRESULT");
                        map_route_end_edittext.setText(e_mapresult.getName());

                        String json = gson.toJson(e_mapresult);
                        PreferenceManager.getInstance(MapRoutActivity.this).setMapenddata(json);

                        if (!PreferenceManager.getInstance(MapRoutActivity.this).getMapstartdata().equals("")) {
                            //start data가 들어있고, start req가 왔는데 이미 end req가 있어
                            s_mapresult = gson.fromJson(PreferenceManager.getInstance(MapRoutActivity.this).getMapstartdata(), MapSearchResultData.class);
                            map_route_start_edittext.setText(s_mapresult.getName());

                        }

                    } else {
                        Log.e("TNDN_LOG", "------------------error-----------------");
                    }

                } else {
                    //기존 data가 전혀없어
                    if (PreferenceManager.getInstance(MapRoutActivity.this).getMaprouteintent().equals("START")) {
                        //처음으로 start req가 왔어
                        Intent i = getIntent();
                        s_mapresult = i.getParcelableExtra("MAPRESULT");
                        map_route_start_edittext.setText(s_mapresult.getName());

                        String json = gson.toJson(s_mapresult);
                        PreferenceManager.getInstance(MapRoutActivity.this).setMapstartdata(json);

                    } else if (PreferenceManager.getInstance(MapRoutActivity.this).getMaprouteintent().equals("END")) {
                        //처음으로 end req가 왔어
                        Intent i = getIntent();
                        e_mapresult = i.getParcelableExtra("MAPRESULT");
                        map_route_end_edittext.setText(e_mapresult.getName());

                        String json = gson.toJson(e_mapresult);
                        PreferenceManager.getInstance(MapRoutActivity.this).setMapenddata(json);

                    } else {
                        Log.e("TNDN_LOG", "------------------error-----------------");
                    }
                }
            }


        }

        map_route_start_edittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //엔터키
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Intent i = new Intent(getApplicationContext(), MapSearchResultActivity.class);
                    i.putExtra("QUERY", map_route_start_edittext.getText().toString().replace(" ", ""));
                    startActivity(i);
                    PreferenceManager.getInstance(getApplicationContext()).setMaprouteintent("START");
                    return true;
                }
                return false;
            }
        });

        map_route_end_edittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //엔터키
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Intent i = new Intent(getApplicationContext(), MapSearchResultActivity.class);
                    i.putExtra("QUERY", map_route_end_edittext.getText().toString().replace(" ", ""));
                    startActivity(i);
                    PreferenceManager.getInstance(getApplicationContext()).setMaprouteintent("END");
                    return true;
                }
                return false;
            }
        });

    }

    private void initialize() {

        actionbar_text.setText(getResources().getString(R.string.map));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getInstance(MapRoutActivity.this).setMaprouteintent("");
                PreferenceManager.getInstance(MapRoutActivity.this).setMapstartdata("");
                PreferenceManager.getInstance(MapRoutActivity.this).setMapenddata("");
                finish();
            }
        });

        map_route_search_start.setOnClickListener(this);
        map_route_search_end.setOnClickListener(this);
        map_route_reverse.setOnClickListener(this);
        map_route_findroad.setOnClickListener(this);
        car.setOnClickListener(this);
        bus.setOnClickListener(this);
        walk.setOnClickListener(this);

        what = 2;
        s_mapresult = new MapSearchResultData();
        e_mapresult = new MapSearchResultData();

        gson = new Gson();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);

        routNodes = new ArrayList<MapRoutNodeData>();
        routLinks = new ArrayList<MapRoutLinkData>();
        pathDatas = new ArrayList<MapPublicPathData>();
        pathEtcData = new MapPublicPathEtcData();

        walk_points = new ArrayList<MapPointsData>();


        currentX = 0;
        currentY = 0;
        gps = new GpsInfo(MapRoutActivity.this);


    }

    private void initView() {

        actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        back = (Button) findViewById(R.id.actionbar_back_button);


        map_route_start_edittext = (EditText) findViewById(R.id.map_route_start_edittext);
        map_route_end_edittext = (EditText) findViewById(R.id.map_route_end_edittext);

        map_route_search_start = (ImageView) findViewById(R.id.map_route_search_start);
        map_route_search_end = (ImageView) findViewById(R.id.map_route_search_end);

        car = (LinearLayout) findViewById(R.id.map_route_car);
        bus = (LinearLayout) findViewById(R.id.map_route_bus);
        walk = (LinearLayout) findViewById(R.id.map_route_walk);
        map_route_line_car = (View) findViewById(R.id.map_route_line_car);
        map_route_line_bus = (View) findViewById(R.id.map_route_line_bus);
        map_route_line_walk = (View) findViewById(R.id.map_route_line_walk);
        map_route_reverse = (ImageView) findViewById(R.id.map_route_reverse);
        map_route_findroad = (ImageView) findViewById(R.id.map_route_findroad);


    }


    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        switch (id) {

            case R.id.map_route_search_start:
                String query_start = map_route_start_edittext.getText().toString().replace(" ", "");
                if (query_start.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.map_nostart), Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(this, MapSearchResultActivity.class);
                    i.putExtra("QUERY", map_route_start_edittext.getText().toString().replace(" ", ""));
                    startActivity(i);
                    PreferenceManager.getInstance(getApplicationContext()).setMaprouteintent("START");
                }
                break;
            case R.id.map_route_search_end:
                String query_end = map_route_end_edittext.getText().toString().replace(" ", "");
                if (query_end.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.map_noend), Toast.LENGTH_SHORT).show();
                } else {

                    Intent i2 = new Intent(this, MapSearchResultActivity.class);
                    i2.putExtra("QUERY", map_route_end_edittext.getText().toString().replace(" ", ""));
                    startActivity(i2);
                    PreferenceManager.getInstance(getApplicationContext()).setMaprouteintent("END");
                }
                break;

            case R.id.map_route_reverse:
                String temp = null;
                temp = map_route_start_edittext.getText().toString();
                map_route_start_edittext.setText(map_route_end_edittext.getText().toString());
                map_route_end_edittext.setText(temp);

                MapSearchResultData tempdata = null;
                tempdata = s_mapresult;
                s_mapresult = e_mapresult;
                e_mapresult = tempdata;

                Gson gson = new Gson();
                String start = gson.toJson(s_mapresult);
                String end = gson.toJson(e_mapresult);
                PreferenceManager.getInstance(MapRoutActivity.this).setMapstartdata(start);
                PreferenceManager.getInstance(MapRoutActivity.this).setMapenddata(end);
                break;
            case R.id.map_route_car:
                map_route_line_car.setVisibility(View.VISIBLE);
                map_route_line_bus.setVisibility(View.INVISIBLE);
                map_route_line_walk.setVisibility(View.INVISIBLE);
                what = 2;
                break;
            case R.id.map_route_bus:
                map_route_line_car.setVisibility(View.INVISIBLE);
                map_route_line_bus.setVisibility(View.VISIBLE);
                map_route_line_walk.setVisibility(View.INVISIBLE);
                what = 1;
                break;
            case R.id.map_route_walk:
                map_route_line_car.setVisibility(View.INVISIBLE);
                map_route_line_bus.setVisibility(View.INVISIBLE);
                map_route_line_walk.setVisibility(View.VISIBLE);
                what = 3;
                break;

            case R.id.map_route_findroad:

                if (s_mapresult.getName().equals("") || s_mapresult.getName() == null) {
                    Log.e("TNDN_LOG___", s_mapresult.toString());
                    //출발과 도착이 둘다 설정되어 있지 않음
                    Toast.makeText(MapRoutActivity.this, getResources().getString(R.string.map_nostart), Toast.LENGTH_SHORT).show();
                    break;
                } else if (e_mapresult.getName().equals("") || e_mapresult.getName() == null) {
                    Toast.makeText(MapRoutActivity.this, getResources().getString(R.string.map_noend), Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    //길찾기 실행
                    if (what == 2) {
                        //자동차

                        JsonObjectRequest objreq = new JsonObjectRequest(new TDUrls().getMapRouteURL + "?sx=" + s_mapresult.getX() + "&sy=" + s_mapresult.getY() + "&ex=" + e_mapresult.getX() + "&ey=" + e_mapresult.getY() + "&what=" + 2 + new UserLog().getLog(getApplicationContext()), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject res) {

                                try {
                                    if (res.getString("success").equals("true")) {
                                        //길찾기 성공

                                        cost = res.getString("cost") + getResources().getString(R.string.curr_kor);
                                        time = getTimeStr(res.getString("time"));
                                        dist = getDistStr(res.getString("length"));

                                        JSONArray routeSection = res.getJSONArray("routSection");
                                        JSONObject obj = routeSection.getJSONObject(0);

                                        JSONArray routLinkArray = obj.getJSONArray("routLink");
                                        JSONArray routNodeArray = obj.getJSONArray("routNode");

                                        for (int i = 0; i < routNodeArray.length(); i++) {
                                            MapRoutNodeData routNode = new MapRoutNodeData();

                                            JSONObject obj_node = routNodeArray.getJSONObject(i);
                                            Iterator<String> itr_node = obj_node.keys();

                                            while (itr_node.hasNext()) {
                                                String key = itr_node.next();
                                                String value_node = obj_node.getString(key);

                                                switch (key) {
                                                    case "x":
                                                        routNode.setX(Float.parseFloat(value_node));
                                                        break;
                                                    case "y":
                                                        routNode.setY(Float.parseFloat(value_node));
                                                        break;
                                                    case "rotation":
                                                        routNode.setRotation(Integer.parseInt(value_node));
                                                        break;

                                                }       //end switch
                                            }   //end while
                                            routNodes.add(routNode);
                                        }//end routNode for 문

                                        for (int i = 0; i < routLinkArray.length(); i++) {
                                            MapRoutLinkData routLink = new MapRoutLinkData();
                                            ArrayList<MapPointsData> points = new ArrayList<MapPointsData>();


                                            JSONObject obj_link = routLinkArray.getJSONObject(i);
                                            Iterator<String> itr_link = obj_link.keys();

                                            while (itr_link.hasNext()) {
                                                String key = itr_link.next();
                                                String value_link = obj_link.getString(key);

                                                switch (key) {
                                                    case "length":
                                                        routLink.setLength(Integer.parseInt(value_link));
                                                        break;
                                                    case "routPoints":
                                                        String strPoints = value_link;
                                                        String arrPoints[] = strPoints.split(" ");
                                                        for (int j = 0; j < arrPoints.length; j++) {
                                                            MapPointsData point = new MapPointsData();
                                                            String arrPoint[] = arrPoints[j].split(",");
                                                            point.setX(Float.parseFloat(arrPoint[0]));
                                                            point.setY(Float.parseFloat(arrPoint[1]));

                                                            points.add(point);
                                                        }
                                                        routLink.setPoints(points);
                                                        break;

                                                }       //end switch
                                            }   //end while
                                            routLinks.add(routLink);
                                        }//end routlink for 문


                                    } else {
                                        //길찾기 실패
                                        Intent i = new Intent(getApplicationContext(), MapErrorActivity.class);
                                        startActivity(i);

                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
//                                Toast.makeText(getApplicationContext(),
//                                        "Error: " + e.getMessage(),
//                                        Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getApplicationContext(), MapErrorActivity.class);
                                    startActivity(i);
                                    hidepDialog();
                                } //end jsonexception catch

                                //data setting
                                hidepDialog();
                                if (routNodes.size() == 0 || routLinks.size() == 0) {
                                    Intent i = new Intent(getApplicationContext(), MapErrorActivity.class);
                                    startActivity(i);
                                }

                                Intent i = new Intent(getApplicationContext(), MapRoutResultActivity.class);
                                i.putParcelableArrayListExtra("ROUTNODES", routNodes);
                                i.putParcelableArrayListExtra("ROUTLINKS", routLinks);
                                i.putExtra("S_MAPRESULT", s_mapresult);
                                i.putExtra("E_MAPRESULT", e_mapresult);
                                i.putExtra("CARDIST", dist);
                                i.putExtra("CARTIME", time);
                                i.putExtra("CARCOST", cost);
                                startActivity(i);

                            }   //end response

                        }, new Response.ErrorListener() {   //end request

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Intent i = new Intent(getApplicationContext(), MapErrorActivity.class);
                                startActivity(i);
//                            Toast.makeText(getApplicationContext(),
//                                    "Internet Access Failed", Toast.LENGTH_SHORT).show();
                                //hide the progress dialog
                                hidepDialog();
                            }
                        });

                        // Adding request to request queue

                        AppController.getInstance().addToRequestQueue(objreq);


                    } //자동차 끝
                    else if (what == 1) {
                        //버스

                        JsonObjectRequest objreq = new JsonObjectRequest(new TDUrls().getMapRouteURL + "?sx=" + s_mapresult.getX() + "&sy=" + s_mapresult.getY() + "&ex=" + e_mapresult.getX() + "&ey=" + e_mapresult.getY() + "&what=" + 1 + new UserLog().getLog(getApplicationContext()), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject res) {

                                try {

                                    JSONObject result = res.getJSONObject("result");
                                    JSONArray paths = result.getJSONArray("path");


                                    int length = paths.length();
                                    for (int i = 0; i < length; i++) {

                                        if (i == length - 1) {
                                            //가장 마지막 길이
                                            /** MapPublicPathEtcData**/
                                            JSONObject obj_etc = paths.getJSONObject(i);
                                            pathEtcData.setSearchType(obj_etc.getInt("searchType"));
                                            pathEtcData.setStartRadius(obj_etc.getInt("startRadius"));
                                            pathEtcData.setEndRadius(obj_etc.getInt("endRadius"));
                                            pathEtcData.setSubwayCount(obj_etc.getInt("subwayCount"));
                                            pathEtcData.setBusCount(obj_etc.getInt("busCount"));
                                            pathEtcData.setSubwayBusCount(obj_etc.getInt("subwayBusCount"));
                                            pathEtcData.setPointDistance(obj_etc.getInt("pointDistance"));
                                            pathEtcData.setOutTrafficCheck(obj_etc.getInt("outTrafficCheck"));

                                            //이 데이터를 한번에 넣어놓을 곳이 없다...

                                        } else {
                                            //pathType, subPath, info 세개가 있는 JsonOBject 객체'들'
                                            MapPublicPathData pathData = new MapPublicPathData();

                                            MapPublicPathSubPathData subPathData = new MapPublicPathSubPathData();
                                            MapPublicPathSubPath01Data subPath01Data = new MapPublicPathSubPath01Data();
                                            MapPublicPathSubPath02Data subPath02Data = new MapPublicPathSubPath02Data();
                                            MapPublicPathSubPath02_laneData subPath02_laneData = new MapPublicPathSubPath02_laneData();
                                            MapPublicPathSubPath01Data subPath03Data = new MapPublicPathSubPath01Data();

                                            MapPublicPathInfoData infoData = new MapPublicPathInfoData();


                                            JSONObject path = paths.getJSONObject(i);

                                            JSONArray path_subPath = path.getJSONArray("subPath");
                                            JSONObject path_info = path.getJSONObject("info");

                                            /** make path subPath data**/
                                            //path_subPath01: 시작점 -> 정류장
                                            JSONObject path_subPath01 = path_subPath.getJSONObject(0);
                                            subPath01Data.setTrafficType(path_subPath01.getInt("trafficType"));
                                            subPath01Data.setDistance(path_subPath01.getInt("distance"));
                                            subPath01Data.setSectionTime(path_subPath01.getInt("sectionTime"));
                                            /**subPath01Data 완성**/

                                            //path_subPath02: 정류장에 관한 자세한 정보
                                            JSONObject path_subPath02 = path_subPath.getJSONObject(1);
                                            subPath02Data.setTrafficType(path_subPath02.getInt("trafficType"));
                                            subPath02Data.setStationCount(path_subPath02.getInt("stationCount"));
                                            subPath02Data.setDistance(path_subPath02.getInt("distance"));
                                            subPath02Data.setSectionTime(path_subPath02.getInt("sectionTime"));
                                            subPath02Data.setStartX(Float.parseFloat(path_subPath02.getString("startX")));
                                            subPath02Data.setStartY(Float.parseFloat(path_subPath02.getString("startY")));
                                            subPath02Data.setStartID(path_subPath02.getInt("startID"));
                                            subPath02Data.setStartName(path_subPath02.getString("startName"));
                                            subPath02Data.setEndX(Float.parseFloat(path_subPath02.getString("endX")));
                                            subPath02Data.setEndY(Float.parseFloat(path_subPath02.getString("endY")));
                                            subPath02Data.setEndID(path_subPath02.getInt("endID"));
                                            subPath02Data.setEndName(path_subPath02.getString("endName"));
                                            //***********path_subPath02의 lane배열
                                            JSONArray subPath_lanes = path_subPath02.getJSONArray("lane");
                                            JSONObject subPath_lane = subPath_lanes.getJSONObject(0);
                                            subPath02_laneData.setBusNo(subPath_lane.getString("busNo"));
                                            subPath02_laneData.setType(subPath_lane.getInt("type"));
                                            subPath02_laneData.setBusID(subPath_lane.getInt("busID"));

                                            //****lane을 path_subPath02에 추가
                                            subPath02Data.setLane(subPath02_laneData);
                                            /**subPath02Data 완성**/

                                            //path_subPath03: 정류장 -> 도착점
                                            JSONObject path_subPath03 = path_subPath.getJSONObject(2);
                                            subPath03Data.setTrafficType(path_subPath03.getInt("trafficType"));
                                            subPath03Data.setDistance(path_subPath03.getInt("distance"));
                                            subPath03Data.setSectionTime(path_subPath03.getInt("sectionTime"));
                                            /**subPath03Data 완성**/

                                            //i번째 pathData의 subPathData완성
                                            subPathData.setSub01(subPath01Data);
                                            subPathData.setSub02(subPath02Data);
                                            subPathData.setSub03(subPath03Data);

                                            /**make path info data**/
                                            infoData.setMapObj(path_info.getString("mapObj"));
                                            infoData.setPayment(path_info.getInt("payment"));
                                            infoData.setBusTransitCount(path_info.getInt("busTransitCount"));
                                            infoData.setSubwayTransitCount(path_info.getInt("subwayTransitCount"));
                                            infoData.setBusStationCount(path_info.getInt("busStationCount"));
                                            infoData.setSubwayStationCount(path_info.getInt("subwayStationCount"));
                                            infoData.setTotalStationCount(path_info.getInt("totalStationCount"));
                                            infoData.setTotalTime(path_info.getInt("totalTime"));
                                            infoData.setTotalWalk(path_info.getInt("totalWalk"));
                                            infoData.setTrafficDistance(path_info.getInt("trafficDistance"));
                                            infoData.setTotalDistance(path_info.getInt("totalDistance"));
                                            infoData.setFirstStartStation(path_info.getString("firstStartStation"));
                                            infoData.setLastEndStation(path_info.getString("lastEndStation"));
                                            infoData.setTotalWalkTime(path_info.getString("totalWalkTime"));

                                            /**final setting**/
                                            pathData.setPathType(path.getInt("pathType"));
                                            pathData.setSubpath(subPathData);
                                            pathData.setInfo(infoData);

                                            /**array list add**/
                                            pathDatas.add(pathData);
                                        }
                                    }//end for문 가장 큰 path.length기준

                                } catch (JSONException e) {
                                    Intent i = new Intent(getApplicationContext(), MapErrorActivity.class);
                                    startActivity(i);

                                    e.printStackTrace();
//                                Toast.makeText(getApplicationContext(),
//                                        "Error: " + e.getMessage(),
//                                        Toast.LENGTH_LONG).show();
                                    hidepDialog();
                                } //end jsonexception catch

                                //data setting
                                hidepDialog();

                                if (pathDatas.size() == 0) {
                                    Intent i = new Intent(getApplicationContext(), MapErrorActivity.class);
                                    startActivity(i);
                                }

                                Intent i = new Intent(getApplicationContext(), MapPublicRoutResultActivity.class);
                                i.putParcelableArrayListExtra("PATHDATAS", pathDatas);
                                i.putExtra("ETCDATA", pathEtcData);
                                i.putExtra("S_MAPRESULT", s_mapresult);
                                i.putExtra("E_MAPRESULT", e_mapresult);
                                startActivity(i);

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

                    } //버스 끝
                    else if (what == 3) {
                        //도보
                        //"http://52.69.30.53:505/public?sx=126.9842753&sy=37.5609692&ex=126.989779&ey=37.565574&what=3"
                        //new TDUrls().getPublicURL(s_mapresult.getX(), s_mapresult.getY(), e_mapresult.getX(), e_mapresult.getY(), 3)
                        JsonObjectRequest objreq = new JsonObjectRequest(new TDUrls().getMapRouteURL + "?sx=" + s_mapresult.getX() + "&sy=" + s_mapresult.getY() + "&ex=" + e_mapresult.getX() + "&ey=" + e_mapresult.getY() + "&what=3" + new UserLog().getLog(getApplicationContext()), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject res) {

                                try {

                                    walk_code = res.getInt("code");
                                    if (walk_code == 100) {
                                        //성공
                                        walk_dist = getDistStr(res.getString("dist"));
                                        walk_time = getTimeStr(res.getString("time"));
                                        //code 100 : 도보 길찾기 성공 / 200 : 반경초과(기본 10,000m) / 300 : 도보 길찾기 검색 불가(도보 데이터 미존재 지역)
                                        String[] points = res.getString("path").split("[|]");
                                        Log.e("TNDN_LOG", points.toString());
                                        int count_point = 0;
                                        for (int i = 0; i < points.length; i += 2) {
                                            MapPointsData walk_point = new MapPointsData();
                                            walk_point.setX(Float.parseFloat(points[i]));
                                            walk_point.setY(Float.parseFloat(points[i + 1]));
                                            walk_points.add(walk_point);
                                        }
                                    } else if (walk_code == 200) {
                                        //반경초과
                                        Intent i = new Intent(getApplicationContext(), MapErrorActivity.class);
                                        startActivity(i);
                                    } else {
                                        //300 포함/ 검색불가. 데이터 미존재
                                        Intent i = new Intent(getApplicationContext(), MapErrorActivity.class);
                                        startActivity(i);
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

                                if (walk_code == 100) {
                                    Intent i = new Intent(getApplicationContext(), MapWalkRoutResultActivity.class);
                                    i.putParcelableArrayListExtra("WALKPOINTS", walk_points);
                                    i.putExtra("WALKDIST", walk_dist);
                                    i.putExtra("WALKTIME", walk_time);
                                    i.putExtra("S_MAPRESULT", s_mapresult);
                                    i.putExtra("E_MAPRESULT", e_mapresult);
                                    startActivity(i);
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

                    } //도보 끝
                    else {
                        Log.e("TNDN_LOG", "길찾기 에러");
                        Intent intent2 = new Intent(getApplicationContext(), MapErrorActivity.class);
                        startActivity(intent2);
                    }
                }//end 출발/도착중 하나라도 명칭검색 안했을때의 else
                break;

        }
    }


    public String getDistStr(String dist) {
        if (Integer.parseInt(dist) > 900) {
            return String.valueOf(Math.round(Integer.parseInt(dist) / 100) / 10) + "Km";
        } else {
            return dist + "m";
        }
    }

    public String getTimeStr(String s_sec) {
        int sec = Integer.parseInt(s_sec);
        double h = Math.floor(sec / 3600);
        double m = (h > 0) ? Math.floor((sec - (h * 3600)) / 60) : Math.floor(sec / 60);
        double s = sec - ((h * 3600) + (m * 60));
        String result = "";
        result = (h > 0) ? result + h + "点" : result;
        result = (m > 0) ? result + " " + m + "分钟" : result;
        return result.replace(".0", "");
    }

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
