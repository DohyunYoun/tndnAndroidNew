package tndn.app.nyam.map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tndn.app.nyam.R;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;

public class MapPublicRoutResultDetailActivity extends AppCompatActivity {

    private TextView actionbar_text;
    private Button back;

    private ProgressDialog pDialog;


    private MapPublicPathData pathData;
    private MapPublicPathEtcData pathEtcData;
    private MapSearchResultData s_mapresult;
    private MapSearchResultData e_mapresult;

    MapPublicPathSubPathData subPathData;
    MapPublicPathSubPath01Data subPath01Data;
    MapPublicPathSubPath02Data subPath02Data;
    MapPublicPathSubPath02_laneData subPath02_laneData;
    MapPublicPathSubPath01Data subPath03Data;
    MapPublicPathInfoData infoData;

    private TextView map_public_rout_result_detail_start;
    private TextView map_public_rout_result_detail_busno;
    private TextView map_public_rout_result_detail_end;
    private TextView map_public_rout_result_detail_dist;
    private TextView map_public_rout_result_detail_time;
    private TextView map_public_rout_result_detail_transit;
    private TextView map_public_rout_result_detail_cost;

    private TextView item_map_public_rout_result_detail_dist_time_start;
    private TextView item_map_public_rout_result_detail_dist_time_first_stop;
    private TextView item_map_rout_result_detail_text_mid_stop;
    private TextView item_map_public_rout_result_detail_dist_time_mid_stop;
    private TextView item_map_public_rout_result_detail_dist_time_mid_text;
    private TextView item_map_rout_result_detail_text_last_stop;
    private TextView item_map_public_rout_result_detail_dist_time_last_stop;
    private TextView item_map_public_rout_result_detail_dist_time_end;

    private LinearLayout map_public_rout_result_detail;

    String pathType;

    int walk_code;
    int walk_code2;
    ArrayList<MapPointsData> pointslist1;
    ArrayList<MapPointsData> pointslist2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_public_rout_result_detail);


        initView();
        initialize();
        Intent i = getIntent();
        pathData = i.getParcelableExtra("PATHDATA");
        pathEtcData = i.getParcelableExtra("ETCDATA");
        s_mapresult = i.getParcelableExtra("S_MAPRESULT");
        e_mapresult = i.getParcelableExtra("E_MAPRESULT");
        infoData = pathData.getInfo();
        subPathData = pathData.getSubpath();
        subPath01Data = subPathData.getSub01();
        subPath02Data = subPathData.getSub02();
        subPath02_laneData = subPath02Data.getLane();
        subPath03Data = subPathData.getSub03();


        map_public_rout_result_detail_start.setText(s_mapresult.getName() + "(" + s_mapresult.getName_kor() + ")");
        map_public_rout_result_detail_end.setText(e_mapresult.getName() + "(" + e_mapresult.getName_kor() + ")");
        map_public_rout_result_detail_busno.setText(subPath02_laneData.getBusNo() + "");
        map_public_rout_result_detail_dist.setText(getDistStr(String.valueOf(infoData.getTotalDistance())));
        map_public_rout_result_detail_time.setText(getTimeStr(String.valueOf(infoData.getTotalTime())));
        map_public_rout_result_detail_transit.setText("(转让 " + (infoData.getBusTransitCount() - 1) + "次)");
        map_public_rout_result_detail_cost.setText(infoData.getPayment() + getResources().getString(R.string.curr_kor));

        item_map_public_rout_result_detail_dist_time_start.setText(s_mapresult.getName() + "(" + s_mapresult.getName_kor() + ")");
        item_map_public_rout_result_detail_dist_time_first_stop.setText("移动距离 " + getDistStr(String.valueOf(subPath01Data.getDistance())) + " (" + getTimeStr(String.valueOf(subPath01Data.getSectionTime())) + ")");
        item_map_rout_result_detail_text_mid_stop.setText(subPath02Data.getStartName());
        item_map_public_rout_result_detail_dist_time_mid_stop.setText(subPath02_laneData.getBusNo() + "");
        item_map_public_rout_result_detail_dist_time_mid_text.setText("移动距离 " + getDistStr(String.valueOf(subPath02Data.getDistance())) + " (" + getTimeStr(String.valueOf(subPath02Data.getSectionTime())) + ")");
        item_map_rout_result_detail_text_last_stop.setText(subPath02Data.getEndName());
        item_map_public_rout_result_detail_dist_time_last_stop.setText("移动距离 " + getDistStr(String.valueOf(subPath03Data.getDistance())) + " (" + getTimeStr(String.valueOf(subPath03Data.getSectionTime())) + ")");
        item_map_public_rout_result_detail_dist_time_end.setText(e_mapresult.getName() + "(" + e_mapresult.getName_kor() + ")");

        map_public_rout_result_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //setting walk rout 01
                JsonObjectRequest objreq = new JsonObjectRequest(new TDUrls().getMapRouteURL + "?sx=" + s_mapresult.getX() + "&sy=" + s_mapresult.getY() + "&ex=" + subPath02Data.getStartX() + "&ey=" + subPath02Data.getStartY() + "&what=" + 3 + new UserLog().getLog(getApplicationContext()), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject res) {

                        try {

                            walk_code = res.getInt("code");
                            if (walk_code == 100) {
                                //성공
                                //code 100 : 도보 길찾기 성공 / 200 : 반경초과(기본 10,000m) / 300 : 도보 길찾기 검색 불가(도보 데이터 미존재 지역)
                                String[] points = res.getString("path").split("[|]");
                                for (int i = 0; i < points.length; i += 2) {
                                    MapPointsData walk_point = new MapPointsData();
                                    walk_point.setX(Float.parseFloat(points[i]));
                                    walk_point.setY(Float.parseFloat(points[i + 1]));
                                    pointslist1.add(walk_point);
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
                        } //end jsonexception catch

                        //data setting


                        //setting walk rout 02
                        JsonObjectRequest objreq2 = new JsonObjectRequest(new TDUrls().getMapRouteURL + "?sx=" + subPath02Data.getEndX() + "&sy=" + subPath02Data.getEndY() + "&ex=" + e_mapresult.getX() + "&ey=" + e_mapresult.getY() + "&what=" + 3 + new UserLog().getLog(getApplicationContext()), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject res) {

                                try {

                                    walk_code2 = res.getInt("code");
                                    if (walk_code2 == 100) {
                                        //성공
                                        //code 100 : 도보 길찾기 성공 / 200 : 반경초과(기본 10,000m) / 300 : 도보 길찾기 검색 불가(도보 데이터 미존재 지역)
                                        String[] points = res.getString("path").split("[|]");
                                        int count_point = 0;
                                        for (int i = 0; i < points.length; i += 2) {
                                            MapPointsData walk_point = new MapPointsData();
                                            walk_point.setX(Float.parseFloat(points[i]));
                                            walk_point.setY(Float.parseFloat(points[i + 1]));
                                            pointslist2.add(walk_point);
                                        }
                                    } else if (walk_code2 == 200) {
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
                                } //end jsonexception catch

                                //data setting


                                Intent i = new Intent(getApplicationContext(), MapPublicRoutMainActivity.class);
                                i.putParcelableArrayListExtra("POINTSLIST1", pointslist1);
                                i.putParcelableArrayListExtra("POINTSLIST2", pointslist2);
                                i.putExtra("PATHDATA", pathData);
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
                            }
                        });

                        // Adding request to request queue

                        AppController.getInstance().addToRequestQueue(objreq2);

                    }   //end response

                }, new Response.ErrorListener() {   //end request

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                "Internet Access Failed", Toast.LENGTH_SHORT).show();
                        //hide the progress dialog
                    }
                });

                // Adding request to request queue

                AppController.getInstance().addToRequestQueue(objreq);


            }
        });

        actionbar_text.setText(getResources().getString(R.string.map));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }       //end oncreate

    private void initialize() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);

        pathData = new MapPublicPathData();
        pathEtcData = new MapPublicPathEtcData();
        s_mapresult = new MapSearchResultData();
        e_mapresult = new MapSearchResultData();

        infoData = new MapPublicPathInfoData();
        subPathData = new MapPublicPathSubPathData();
        subPath01Data = new MapPublicPathSubPath01Data();
        subPath02Data = new MapPublicPathSubPath02Data();
        subPath02_laneData = new MapPublicPathSubPath02_laneData();
        subPath03Data = new MapPublicPathSubPath01Data();

        pathType = null;

        pointslist1 = new ArrayList<MapPointsData>();
        pointslist2 = new ArrayList<MapPointsData>();

    }

    private void initView() {
        actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        back = (Button) findViewById(R.id.actionbar_back_button);

        map_public_rout_result_detail_start = (TextView) findViewById(R.id.map_public_rout_result_detail_start);
        map_public_rout_result_detail_busno = (TextView) findViewById(R.id.map_public_rout_result_detail_busno);
        map_public_rout_result_detail_end = (TextView) findViewById(R.id.map_public_rout_result_detail_end);
        map_public_rout_result_detail_dist = (TextView) findViewById(R.id.map_public_rout_result_detail_dist);
        map_public_rout_result_detail_time = (TextView) findViewById(R.id.map_public_rout_result_detail_time);
        map_public_rout_result_detail_transit = (TextView) findViewById(R.id.map_public_rout_result_detail_transit);
        map_public_rout_result_detail_cost = (TextView) findViewById(R.id.map_public_rout_result_detail_cost);

        item_map_public_rout_result_detail_dist_time_start = (TextView) findViewById(R.id.item_map_public_rout_result_detail_dist_time_start);
        item_map_public_rout_result_detail_dist_time_first_stop = (TextView) findViewById(R.id.item_map_public_rout_result_detail_dist_time_first_stop);
        item_map_rout_result_detail_text_mid_stop = (TextView) findViewById(R.id.item_map_rout_result_detail_text_mid_stop);
        item_map_public_rout_result_detail_dist_time_mid_stop = (TextView) findViewById(R.id.item_map_public_rout_result_detail_dist_time_mid_stop);
        item_map_public_rout_result_detail_dist_time_mid_text = (TextView) findViewById(R.id.item_map_public_rout_result_detail_dist_time_mid_text);
        item_map_rout_result_detail_text_last_stop = (TextView) findViewById(R.id.item_map_rout_result_detail_text_last_stop);
        item_map_public_rout_result_detail_dist_time_last_stop = (TextView) findViewById(R.id.item_map_public_rout_result_detail_dist_time_last_stop);
        item_map_public_rout_result_detail_dist_time_end = (TextView) findViewById(R.id.item_map_public_rout_result_detail_dist_time_end);

        map_public_rout_result_detail = (LinearLayout) findViewById(R.id.map_public_rout_result_detail);
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

    public String getDistStr(String dist) {
        if (Integer.parseInt(dist) > 999) {
            return String.valueOf(Math.round(Integer.parseInt(dist) / 100) / 10) + "Km";
        } else {
            return dist + "m";
        }
    }

    public String getTimeStr(String s_sec) {
        int sec = Integer.parseInt(s_sec) * 60;
        double h = Math.floor(sec / 3600);
        double m = (h > 0) ? Math.floor((sec - (h * 3600)) / 60) : Math.floor(sec / 60);
        double s = sec - ((h * 3600) + (m * 60));
        String result = "";
        result = (h > 0) ? result + h + "点" : result;
        result = (m > 0) ? result + " " + m + "分钟" : result;
        return result.replace(".0", "");
    }
}
