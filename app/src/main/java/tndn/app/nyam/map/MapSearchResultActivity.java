package tndn.app.nyam.map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import tndn.app.nyam.R;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.IsOnline;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;

public class MapSearchResultActivity extends AppCompatActivity {

    private TextView actionbar_text;
    private Button back;

    private ProgressDialog pDialog;
    private ArrayList<MapSearchResultData> mapResults;
    private MapSearchResultAdapter mAdapter;

    private ListView lv_map_result;

    private String query;

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search_result);

        Intent i = getIntent();
        query = i.getStringExtra("QUERY");

        initView();
        initialize();

        if (new IsOnline().onlineCheck(this)) {                 //internet check true start
            showpDialog();
            mapResults = new ArrayList<MapSearchResultData>();

            JsonObjectRequest objreq = new JsonObjectRequest(new TDUrls().getMapNameURL + "?name=" + query + new UserLog().getLog(this), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject res) {

                    try {
                        int count = res.getInt("count");
                        if (count == 0) {
                            //검색결과가 없을때
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.map_noresult), Toast.LENGTH_SHORT).show();
                            Intent in2 = new Intent(getApplicationContext(), MapRoutActivity.class);
                            startActivity(in2);
                            finish();
                            PreferenceManager.getInstance(MapSearchResultActivity.this).setMapintent("");
                            PreferenceManager.getInstance(MapSearchResultActivity.this).setMaprouteintent("");
                            PreferenceManager.getInstance(MapSearchResultActivity.this).setMapenddata("");
                            PreferenceManager.getInstance(MapSearchResultActivity.this).setMapstartdata("");
                            //hide the progress dialog
                            hidepDialog();
                        }
                        JSONArray result = res.getJSONArray("list");

                        for (int i = 0; i < result.length(); i++) {

                            MapSearchResultData mapResult = new MapSearchResultData();
                            JSONObject obj = result.getJSONObject(i);
                            Iterator<String> itr = obj.keys();

                            while (itr.hasNext()) {
                                String key = itr.next();
                                String value = obj.getString(key);

                                switch (key) {
                                    case "name":
                                        mapResult.setName(value);
                                        break;
                                    case "kor":
                                        mapResult.setName_kor(value);
                                        break;
                                    case "cateNm":
                                        mapResult.setCateNm(value);
                                        break;
                                    case "cateNmKor":
                                        mapResult.setCateNmKor(value);
                                        break;
                                    case "sido":
                                        mapResult.setSido_chn(value);
                                        break;
                                    case "sgg":
                                        mapResult.setSgg_chn(value);
                                        break;
                                    case "hemd":
                                        mapResult.setHemd_chn(value);
                                        break;
                                    case "x":
                                        mapResult.setX(Float.parseFloat(value));
                                        break;
                                    case "y":
                                        mapResult.setY(Float.parseFloat(value));
                                        break;
                                    case "sidoKor":
                                        mapResult.setSido_kor(value);
                                        break;
                                    case "sggKor":
                                        mapResult.setSgg_kor(value);
                                        break;
                                    case "hemdKor":
                                        mapResult.setHemd_kor(value);
                                        break;
                                    case "bemdKor":
                                        mapResult.setBemd_kor(value);
                                        break;

                                }       //end switch
                            }   //end while
                            mapResults.add(mapResult);
                        }   //end for

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        hidepDialog();
                    } //end jsonexception catch

                    //data setting
                    hidepDialog();

                    mAdapter = new MapSearchResultAdapter(MapSearchResultActivity.this, mapResults);
                    lv_map_result.setAdapter(mAdapter);


                    lv_map_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // Log.e("TNDN_LOG", mapResults.get(position).toString());

                            if (!PreferenceManager.getInstance(MapSearchResultActivity.this).getMaprouteintent().equals("")) {
                                //start 나 end 가 왔어.
                                if (!PreferenceManager.getInstance(MapSearchResultActivity.this).getMapstartdata().equals("")) {
                                    //start data가 들어있어

                                    if (PreferenceManager.getInstance(MapSearchResultActivity.this).getMaprouteintent().equals("START")) {
                                        //start data가 들어있는데 start req가 또 왔어
//                                        Gson gson = new Gson();
//                                        String json = gson.toJson(mapResults.get(position));
//                                        PreferenceManager.getInstance(MapSearchResultActivity.this).setMapstartdata(json);

                                        //이전 map route activity 삭제
                                        MapRoutActivity maprouteActivity = (MapRoutActivity) MapRoutActivity.maprouteActivity;
                                        maprouteActivity.finish();

                                        Intent i = new Intent(MapSearchResultActivity.this, MapRoutActivity.class);
                                        if (!mapResults.get(position).getSido_kor().equals("제주특별자치도")) {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.map_route_noregion), Toast.LENGTH_SHORT).show();
                                            PreferenceManager.getInstance(MapSearchResultActivity.this).setMaprouteintent("");
                                        } else {
                                            i.putExtra("MAPRESULT", mapResults.get(position));
                                        }
                                        startActivity(i);
                                        finish();
                                    } else if (PreferenceManager.getInstance(MapSearchResultActivity.this).getMaprouteintent().equals("END")) {
                                        //start data가 들어있는데 end req가 왔어
//                                        Gson gson = new Gson();
//                                        String json = gson.toJson(mapResults.get(position));
//                                        PreferenceManager.getInstance(MapSearchResultActivity.this).setMapenddata(json);
//이전 map route activity 삭제
                                        MapRoutActivity maprouteActivity = (MapRoutActivity) MapRoutActivity.maprouteActivity;
                                        maprouteActivity.finish();
                                        Intent i = new Intent(MapSearchResultActivity.this, MapRoutActivity.class);
                                        if (!mapResults.get(position).getSido_kor().equals("제주특별자치도")) {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.map_route_noregion), Toast.LENGTH_SHORT).show();
                                            PreferenceManager.getInstance(MapSearchResultActivity.this).setMaprouteintent("");

                                        } else {
                                            i.putExtra("MAPRESULT", mapResults.get(position));
                                        }
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Log.e("TNDN_LOG", "------------------error-----------------");
                                        Intent errorintent = new Intent(getApplicationContext(), MapErrorActivity.class);
                                        startActivity(errorintent);
                                        finish();
                                    }
                                } else if (!PreferenceManager.getInstance(MapSearchResultActivity.this).getMapenddata().equals("")) {
                                    //end data가 들어있어

                                    if (PreferenceManager.getInstance(MapSearchResultActivity.this).getMaprouteintent().equals("START")) {
                                        //end data가 들어있는데 start req가 왔어
//                                        Gson gson = new Gson();
//                                        String json = gson.toJson(mapResults.get(position));
//                                        PreferenceManager.getInstance(MapSearchResultActivity.this).setMapstartdata(json);
//이전 map route activity 삭제
                                        MapRoutActivity maprouteActivity = (MapRoutActivity) MapRoutActivity.maprouteActivity;
                                        maprouteActivity.finish();
                                        Intent i = new Intent(MapSearchResultActivity.this, MapRoutActivity.class);
                                        if (!mapResults.get(position).getSido_kor().equals("제주특별자치도")) {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.map_route_noregion), Toast.LENGTH_SHORT).show();
                                            PreferenceManager.getInstance(MapSearchResultActivity.this).setMaprouteintent("");

                                        } else {
                                            i.putExtra("MAPRESULT", mapResults.get(position));
                                        }
                                        startActivity(i);
                                        finish();

                                    } else if (PreferenceManager.getInstance(MapSearchResultActivity.this).getMaprouteintent().equals("END")) {
                                        //end data가 들어있는데 end req가 또 왔어
//                                        Gson gson = new Gson();
//                                        String json = gson.toJson(mapResults.get(position));
//                                        PreferenceManager.getInstance(MapSearchResultActivity.this).setMapenddata(json);
//이전 map route activity 삭제
                                        MapRoutActivity maprouteActivity = (MapRoutActivity) MapRoutActivity.maprouteActivity;
                                        maprouteActivity.finish();
                                        Intent i = new Intent(MapSearchResultActivity.this, MapRoutActivity.class);
                                        if (!mapResults.get(position).getSido_kor().equals("제주특별자치도")) {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.map_route_noregion), Toast.LENGTH_SHORT).show();
                                            PreferenceManager.getInstance(MapSearchResultActivity.this).setMaprouteintent("");

                                        } else {
                                            i.putExtra("MAPRESULT", mapResults.get(position));
                                        }
                                        startActivity(i);
                                        finish();

                                    } else {
                                        Log.e("TNDN_LOG", "------------------error-----------------");
                                    }

                                } else {
                                    //기존 data가 전혀없어
                                    if (PreferenceManager.getInstance(MapSearchResultActivity.this).getMaprouteintent().equals("START")) {
                                        //처음으로 start req가 왔어

//이전 map route activity 삭제
                                        MapRoutActivity maprouteActivity = (MapRoutActivity) MapRoutActivity.maprouteActivity;
                                        maprouteActivity.finish();
                                        Intent i = new Intent(MapSearchResultActivity.this, MapRoutActivity.class);
                                        if (!mapResults.get(position).getSido_kor().equals("제주특별자치도")) {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.map_route_noregion), Toast.LENGTH_SHORT).show();
                                            PreferenceManager.getInstance(MapSearchResultActivity.this).setMaprouteintent("");

                                        } else {
                                            i.putExtra("MAPRESULT", mapResults.get(position));
                                        }
                                        startActivity(i);
                                        finish();

                                    } else if (PreferenceManager.getInstance(MapSearchResultActivity.this).getMaprouteintent().equals("END")) {
                                        //처음으로 end req가 왔어
//                                        Gson gson = new Gson();
//                                        String json = gson.toJson(mapResults.get(position));
////                                        PreferenceManager.getInstance(MapSearchResultActivity.this).setMapenddata(json);
//이전 map route activity 삭제
                                        MapRoutActivity maprouteActivity = (MapRoutActivity) MapRoutActivity.maprouteActivity;
                                        maprouteActivity.finish();
                                        Intent i = new Intent(MapSearchResultActivity.this, MapRoutActivity.class);
                                        if (!mapResults.get(position).getSido_kor().equals("제주특별자치도")) {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.map_route_noregion), Toast.LENGTH_SHORT).show();
                                            PreferenceManager.getInstance(MapSearchResultActivity.this).setMaprouteintent("");

                                        } else {
                                            i.putExtra("MAPRESULT", mapResults.get(position));
                                        }
                                        startActivity(i);
                                        finish();

                                    } else {
                                        Log.e("TNDN_LOG", "------------------error-----------------");
                                    }
                                }
                            } else {
                                Intent intent = new Intent(MapSearchResultActivity.this, MapMainActivity.class);
                                intent.putParcelableArrayListExtra("MAPRESULTS", mapResults);
                                intent.putExtra("POINT", position);
                                startActivity(intent);
                                finish();
                                PreferenceManager.getInstance(MapSearchResultActivity.this).setMapintent("OK");
                            }
                        }
                    });
                }   //end response

            }, new Response.ErrorListener() {   //end request

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),
                            "连接时间过长。请重新开始", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), MapRoutActivity.class);
                    startActivity(in);
                    finish();
                    PreferenceManager.getInstance(MapSearchResultActivity.this).setMapintent("");
                    PreferenceManager.getInstance(MapSearchResultActivity.this).setMaprouteintent("");
                    PreferenceManager.getInstance(MapSearchResultActivity.this).setMapenddata("");
                    PreferenceManager.getInstance(MapSearchResultActivity.this).setMapstartdata("");
                    //hide the progress dialog
                    hidepDialog();
                }
            });

            // Adding request to request queue

//            objreq.setRetryPolicy(new DefaultRetryPolicy(
//                    60000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(objreq);

        } else

        {                   //internet check failed start
            Toast.makeText(this, "Internet Access Failed", Toast.LENGTH_SHORT).show();
        }   //end internet check failed


        actionbar_text.setText(

                getResources().getString(R.string.map)

        );
        back.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                }

        );


    }       //end oncreate

    private void initialize() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);


    }

    private void initView() {
        actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        lv_map_result = (ListView) findViewById(R.id.map_search_result_listview);

        back = (Button) findViewById(R.id.actionbar_back_button);
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
