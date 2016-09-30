package tndn.app.nyam.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import tndn.app.nyam.R;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.SetListViewHeight;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;

/**
 * Created by YounDit on 2016-02-12.
 */
public class MapBusAirportActivity extends AppCompatActivity {


    private ListView map_bus_airport_listview01;
    private ListView map_bus_airport_listview02;
    private ListView map_bus_airport_listview03;
    private ListView map_bus_airport_listview04;
    private ListView map_bus_airport_listview05;

    private MapBusAirportAdapter mAdapter;
    private ArrayList<MapBusAirportData> mapBusAirportDatas01;
    private ArrayList<MapBusAirportData> mapBusAirportDatas02;
    private ArrayList<MapBusAirportData> mapBusAirportDatas03;
    private ArrayList<MapBusAirportData> mapBusAirportDatas04;
    private ArrayList<MapBusAirportData> mapBusAirportDatas05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_bus_airport);

/********************************************
 for actionbar
 ********************************************/
        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);
        Button actionbar_refresh_button = (Button) findViewById(R.id.actionbar_refresh_button);

        actionbar_text.setText(getResources().getString(R.string.map));
        actionbar_refresh_button.setVisibility(View.VISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        actionbar_refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapBusAirportActivity.class));
                finish();
            }
        });
/********************************************
 for actionbar
 ********************************************/
        initView();


        //TODO: 추가되는 버스정보 확인하기.
        getBusInfo();


    }


    private void initView() {
        map_bus_airport_listview01 = (ListView) findViewById(R.id.map_bus_airport_listview01);
        map_bus_airport_listview02 = (ListView) findViewById(R.id.map_bus_airport_listview02);
        map_bus_airport_listview03 = (ListView) findViewById(R.id.map_bus_airport_listview03);
        map_bus_airport_listview04 = (ListView) findViewById(R.id.map_bus_airport_listview04);
        map_bus_airport_listview05 = (ListView) findViewById(R.id.map_bus_airport_listview05);

    }


    private void getBusInfo() {

        /**
         * BUS 1
         * 제주국제공항입구[동]	월성 마을 방향	405000646
         */
        JsonObjectRequest objreq1 = new JsonObjectRequest(new TDUrls().getMapBusAirportURL + "?id=405000646" + new UserLog().getLog(this), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {

                try {

                    JSONObject response = res.getJSONObject("response");

                    if (!response.getJSONObject("header").getString("resultcode").equals("0")) {//else result failed
                        Toast.makeText(getApplicationContext(),
                                "Internet Access Failed", Toast.LENGTH_SHORT).show();
                    } else {
                        mapBusAirportDatas01 = new ArrayList<MapBusAirportData>();

                        if (!response.getJSONObject("body").getString("totalcount").equals("0")) {


                            JSONArray result = response.getJSONObject("body").getJSONObject("items").getJSONArray("item");

                            for (int i = 0; i < result.length(); i++) {

                                MapBusAirportData data = new MapBusAirportData();
                                JSONObject obj = result.getJSONObject(i);
                                Iterator<String> itr = obj.keys();

                                while (itr.hasNext()) {
                                    String key = itr.next();
                                    String value = obj.getString(key);

                                    switch (key) {
                                        case "leftstation":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                break;
                                            else {
                                                data.setLeftstation(value);
                                                break;
                                            }
                                        case "predicttravtm":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                break;
                                            else {
                                                data.setPredicttravtm(Integer.parseInt(value));
                                                break;
                                            }
                                        case "routeid":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                break;
                                            else {
                                                data.setRouteid(value);
                                                break;
                                            }
                                        case "stationord":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                break;
                                            else {
                                                data.setStationord(value);
                                                break;
                                            }
                                        case "updndir":
                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                break;
                                            else {
                                                data.setUpdndir(value);
                                                break;
                                            }

                                    }       //end switch
                                }   //end while
                                if (data.getPredicttravtm() != 0)
                                    mapBusAirportDatas01.add(data);
                            }   //end for
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } //end jsonexception catch

                //data setting
                if (mapBusAirportDatas01.size() == 0) {
                    findViewById(R.id.map_bus_airport_listview_empty01).setVisibility(View.VISIBLE);
                } else {
                    Collections.sort(mapBusAirportDatas01, new NoAscCompare());
                    mAdapter = new MapBusAirportAdapter(MapBusAirportActivity.this, mapBusAirportDatas01);
                    map_bus_airport_listview01.setAdapter(mAdapter);
                    new SetListViewHeight().init(map_bus_airport_listview01);
                }
                /**
                 *  BUS 2
                 *  제주국제공항[북]	월성 마을 방향	405000663
                 */
                JsonObjectRequest objreq2 = new JsonObjectRequest(new TDUrls().getMapBusAirportURL + "?id=405000663" + new UserLog().getLog(getApplicationContext()), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject res) {

                        try {

                            JSONObject response = res.getJSONObject("response");

                            if (!response.getJSONObject("header").getString("resultcode").equals("0")) {//else result failed
                                Toast.makeText(getApplicationContext(),
                                        "Internet Access Failed", Toast.LENGTH_SHORT).show();
                            } else {
                                mapBusAirportDatas02 = new ArrayList<MapBusAirportData>();

                                if (!response.getJSONObject("body").getString("totalcount").equals("0")) {


                                    JSONArray result = response.getJSONObject("body").getJSONObject("items").getJSONArray("item");

                                    for (int i = 0; i < result.length(); i++) {

                                        MapBusAirportData data = new MapBusAirportData();
                                        JSONObject obj = result.getJSONObject(i);
                                        Iterator<String> itr = obj.keys();

                                        while (itr.hasNext()) {
                                            String key = itr.next();
                                            String value = obj.getString(key);

                                            switch (key) {
                                                case "leftstation":
                                                    if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                        break;
                                                    else {
                                                        data.setLeftstation(value);
                                                        break;
                                                    }
                                                case "predicttravtm":
                                                    if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                        break;
                                                    else {
                                                        data.setPredicttravtm(Integer.parseInt(value));
                                                        break;
                                                    }
                                                case "routeid":
                                                    if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                        break;
                                                    else {
                                                        data.setRouteid(value);
                                                        break;
                                                    }
                                                case "stationord":
                                                    if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                        break;
                                                    else {
                                                        data.setStationord(value);
                                                        break;
                                                    }
                                                case "updndir":
                                                    if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                        break;
                                                    else {
                                                        data.setUpdndir(value);
                                                        break;
                                                    }

                                            }       //end switch
                                        }   //end while
                                        if (data.getPredicttravtm() != 0)
                                            mapBusAirportDatas02.add(data);
                                    }   //end for
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        } //end jsonexception catch

                        //data setting
                        if (mapBusAirportDatas02.size() == 0) {
                            findViewById(R.id.map_bus_airport_listview_empty02).setVisibility(View.VISIBLE);
                        } else {
                            Collections.sort(mapBusAirportDatas02, new NoAscCompare());
                            mAdapter = new MapBusAirportAdapter(MapBusAirportActivity.this, mapBusAirportDatas02);
                            map_bus_airport_listview02.setAdapter(mAdapter);
                            new SetListViewHeight().init(map_bus_airport_listview02);
                        }
                        /**
                         *  BUS 3
                         *  제주국제공항	다호 마을 방향	405001478
                         */
                        JsonObjectRequest objreq3 = new JsonObjectRequest(new TDUrls().getMapBusAirportURL + "?id=405001478" + new UserLog().getLog(getApplicationContext()), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject res) {

                                try {

                                    JSONObject response = res.getJSONObject("response");

                                    if (!response.getJSONObject("header").getString("resultcode").equals("0")) {//else result failed
                                        Toast.makeText(getApplicationContext(),
                                                "Internet Access Failed", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mapBusAirportDatas03 = new ArrayList<MapBusAirportData>();

                                        if (!response.getJSONObject("body").getString("totalcount").equals("0")) {


                                            JSONArray result = response.getJSONObject("body").getJSONObject("items").getJSONArray("item");

                                            for (int i = 0; i < result.length(); i++) {

                                                MapBusAirportData data = new MapBusAirportData();
                                                JSONObject obj = result.getJSONObject(i);
                                                Iterator<String> itr = obj.keys();

                                                while (itr.hasNext()) {
                                                    String key = itr.next();
                                                    String value = obj.getString(key);

                                                    switch (key) {
                                                        case "leftstation":
                                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                break;
                                                            else {
                                                                data.setLeftstation(value);
                                                                break;
                                                            }
                                                        case "predicttravtm":
                                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                break;
                                                            else {
                                                                data.setPredicttravtm(Integer.parseInt(value));
                                                                break;
                                                            }
                                                        case "routeid":
                                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                break;
                                                            else {
                                                                data.setRouteid(value);
                                                                break;
                                                            }
                                                        case "stationord":
                                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                break;
                                                            else {
                                                                data.setStationord(value);
                                                                break;
                                                            }
                                                        case "updndir":
                                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                break;
                                                            else {
                                                                data.setUpdndir(value);
                                                                break;
                                                            }

                                                    }       //end switch
                                                }   //end while
                                                if (data.getPredicttravtm() != 0)
                                                    mapBusAirportDatas03.add(data);
                                            }   //end for
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),
                                            "Error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                } //end jsonexception catch

                                //data setting
                                if (mapBusAirportDatas03.size() == 0) {
                                    findViewById(R.id.map_bus_airport_listview_empty03).setVisibility(View.VISIBLE);
                                } else {
                                    Collections.sort(mapBusAirportDatas03, new NoAscCompare());

                                    mAdapter = new MapBusAirportAdapter(MapBusAirportActivity.this, mapBusAirportDatas03);
                                    map_bus_airport_listview03.setAdapter(mAdapter);
                                    new SetListViewHeight().init(map_bus_airport_listview03);
                                }
                                /**
                                 *  BUS 4
                                 *  제주국제공항	제주 썬호텔[북] 방향	405001759
                                 */
                                JsonObjectRequest objreq4 = new JsonObjectRequest(new TDUrls().getMapBusAirportURL + "?id=405001759" + new UserLog().getLog(getApplicationContext()), new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject res) {

                                        try {

                                            JSONObject response = res.getJSONObject("response");

                                            if (!response.getJSONObject("header").getString("resultcode").equals("0")) {//else result failed
                                                Toast.makeText(getApplicationContext(),
                                                        "Internet Access Failed", Toast.LENGTH_SHORT).show();
                                            } else {
                                                mapBusAirportDatas04 = new ArrayList<MapBusAirportData>();

                                                if (!response.getJSONObject("body").getString("totalcount").equals("0")) {


                                                    JSONArray result = response.getJSONObject("body").getJSONObject("items").getJSONArray("item");

                                                    for (int i = 0; i < result.length(); i++) {

                                                        MapBusAirportData data = new MapBusAirportData();
                                                        JSONObject obj = result.getJSONObject(i);
                                                        Iterator<String> itr = obj.keys();

                                                        while (itr.hasNext()) {
                                                            String key = itr.next();
                                                            String value = obj.getString(key);

                                                            switch (key) {
                                                                case "leftstation":
                                                                    if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                        break;
                                                                    else {
                                                                        data.setLeftstation(value);
                                                                        break;
                                                                    }
                                                                case "predicttravtm":
                                                                    if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                        break;
                                                                    else {
                                                                        data.setPredicttravtm(Integer.parseInt(value));
                                                                        break;
                                                                    }
                                                                case "routeid":
                                                                    if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                        break;
                                                                    else {
                                                                        data.setRouteid(value);
                                                                        break;
                                                                    }
                                                                case "stationord":
                                                                    if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                        break;
                                                                    else {
                                                                        data.setStationord(value);
                                                                        break;
                                                                    }
                                                                case "updndir":
                                                                    if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                        break;
                                                                    else {
                                                                        data.setUpdndir(value);
                                                                        break;
                                                                    }

                                                            }       //end switch
                                                        }   //end while
                                                        if (data.getPredicttravtm() != 0)
                                                            mapBusAirportDatas04.add(data);
                                                    }   //end for
                                                }
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(),
                                                    "Error: " + e.getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        } //end jsonexception catch

                                        //data setting
                                        if (mapBusAirportDatas04.size() == 0) {
                                            findViewById(R.id.map_bus_airport_listview_empty04).setVisibility(View.VISIBLE);
                                        } else {
                                            Collections.sort(mapBusAirportDatas04, new NoAscCompare());
                                            mAdapter = new MapBusAirportAdapter(MapBusAirportActivity.this, mapBusAirportDatas04);
                                            map_bus_airport_listview04.setAdapter(mAdapter);
                                            new SetListViewHeight().init(map_bus_airport_listview04);
                                        }
                                        /**
                                         *  BUS 5
                                         *  제주국제공항	제주시외버스터미널(종점) 방향	405001897
                                         */
                                        JsonObjectRequest objreq5 = new JsonObjectRequest(new TDUrls().getMapBusAirportURL + "?id=405001897" + new UserLog().getLog(getApplicationContext()), new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject res) {

                                                try {

                                                    JSONObject response = res.getJSONObject("response");

                                                    if (!response.getJSONObject("header").getString("resultcode").equals("0")) {//else result failed
                                                        Toast.makeText(getApplicationContext(),
                                                                "Internet Access Failed", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        mapBusAirportDatas05 = new ArrayList<MapBusAirportData>();

                                                        if (!response.getJSONObject("body").getString("totalcount").equals("0")) {


                                                            JSONArray result = response.getJSONObject("body").getJSONObject("items").getJSONArray("item");

                                                            for (int i = 0; i < result.length(); i++) {

                                                                MapBusAirportData data = new MapBusAirportData();
                                                                JSONObject obj = result.getJSONObject(i);
                                                                Iterator<String> itr = obj.keys();

                                                                while (itr.hasNext()) {
                                                                    String key = itr.next();
                                                                    String value = obj.getString(key);

                                                                    switch (key) {
                                                                        case "leftstation":
                                                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                                break;
                                                                            else {
                                                                                data.setLeftstation(value);
                                                                                break;
                                                                            }
                                                                        case "predicttravtm":
                                                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                                break;
                                                                            else {
                                                                                data.setPredicttravtm(Integer.parseInt(value));
                                                                                break;
                                                                            }
                                                                        case "routeid":
                                                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                                break;
                                                                            else {
                                                                                data.setRouteid(value);
                                                                                break;
                                                                            }
                                                                        case "stationord":
                                                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                                break;
                                                                            else {
                                                                                data.setStationord(value);
                                                                                break;
                                                                            }
                                                                        case "updndir":
                                                                            if (value.equals("") || value.equals("null") || value.equals("NULL"))
                                                                                break;
                                                                            else {
                                                                                data.setUpdndir(value);
                                                                                break;
                                                                            }

                                                                    }       //end switch
                                                                }   //end while
                                                                if (data.getPredicttravtm() != 0)
                                                                    mapBusAirportDatas05.add(data);
                                                            }   //end for
                                                        }
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(getApplicationContext(),
                                                            "Error: " + e.getMessage(),
                                                            Toast.LENGTH_LONG).show();
                                                } //end jsonexception catch

                                                //data setting
                                                if (mapBusAirportDatas05.size() == 0) {
                                                    findViewById(R.id.map_bus_airport_listview_empty05).setVisibility(View.VISIBLE);
                                                } else {
                                                    Collections.sort(mapBusAirportDatas05, new NoAscCompare());
                                                    mAdapter = new MapBusAirportAdapter(MapBusAirportActivity.this, mapBusAirportDatas05);
                                                    map_bus_airport_listview05.setAdapter(mAdapter);
                                                    new SetListViewHeight().init(map_bus_airport_listview05);
                                                }

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

                                        AppController.getInstance().addToRequestQueue(objreq5);

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

                                AppController.getInstance().addToRequestQueue(objreq4);

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

                        AppController.getInstance().addToRequestQueue(objreq3);

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

        AppController.getInstance().addToRequestQueue(objreq1);
    }


    /**
     * No 오름차순
     *
     * @author falbb
     */
    static class NoAscCompare implements Comparator<MapBusAirportData> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(MapBusAirportData arg0, MapBusAirportData arg1) {
            // TODO Auto-generated method stub
            return arg0.getPredicttravtm() < arg1.getPredicttravtm() ? -1 : arg0.getPredicttravtm() > arg1.getPredicttravtm() ? 1 : 0;
        }

    }
}
