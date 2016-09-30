package tndn.app.nyam.map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tndn.app.nyam.R;

public class MapWalkRoutResultActivity extends AppCompatActivity {

    private TextView actionbar_text;
    private Button back;

    private ProgressDialog pDialog;


    private TextView map_walk_rout_result_start;
    private TextView map_walk_rout_result_end;
    private TextView map_walk_rout_result_dist;
    private TextView map_walk_rout_result_time;
    private TextView item_map_walk_rout_result_dist_time;
    private TextView item_map_walk_rout_result_start_name;
    private TextView item_map_walk_rout_result_end_name;


    private LinearLayout map_walk_rout_result;

    private MapSearchResultData s_mapresult;
    private MapSearchResultData e_mapresult;
    ArrayList<MapPointsData> walk_points;
    String walk_dist;
    String walk_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_walk_rout_result);


        initView();
        initialize();
        Intent i = getIntent();
        walk_points = i.getParcelableArrayListExtra("WALKPOINTS");
        walk_dist = i.getStringExtra("WALKDIST");
        walk_time = i.getStringExtra("WALKTIME");
        s_mapresult = i.getParcelableExtra("S_MAPRESULT");
        e_mapresult = i.getParcelableExtra("E_MAPRESULT");
        map_walk_rout_result_start.setText(s_mapresult.getName());
        map_walk_rout_result_end.setText(e_mapresult.getName());
        map_walk_rout_result_dist.setText(walk_dist);
        map_walk_rout_result_time.setText(walk_time);
        item_map_walk_rout_result_dist_time.setText("移动距离 " + walk_dist + " (" + walk_time + ")");
        item_map_walk_rout_result_start_name.setText(s_mapresult.getName());
        item_map_walk_rout_result_end_name.setText(e_mapresult.getName());


        map_walk_rout_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (walk_dist.equals("null") || walk_dist.equals("") || walk_dist.equals("NULL")) {
                    Intent intent = new Intent(getApplicationContext(), MapErrorActivity.class);
                    startActivity(intent);
                    finish();
                }
                Intent i = new Intent(getApplicationContext(), MapWalkRoutMainActivity.class);
                i.putParcelableArrayListExtra("WALKPOINTS", walk_points);
                i.putExtra("S_MAPRESULT", s_mapresult);
                i.putExtra("E_MAPRESULT", e_mapresult);
                startActivity(i);
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

        walk_points = new ArrayList<MapPointsData>();
        s_mapresult = new MapSearchResultData();
        e_mapresult = new MapSearchResultData();

    }

    private void initView() {
        actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        back = (Button) findViewById(R.id.actionbar_back_button);

        map_walk_rout_result_start = (TextView) findViewById(R.id.map_walk_rout_result_start);
        map_walk_rout_result_end = (TextView) findViewById(R.id.map_walk_rout_result_end);
        map_walk_rout_result_dist = (TextView) findViewById(R.id.map_walk_rout_result_dist);
        map_walk_rout_result_time = (TextView) findViewById(R.id.map_walk_rout_result_time);
        item_map_walk_rout_result_dist_time = (TextView) findViewById(R.id.item_map_walk_rout_result_dist_time);
        item_map_walk_rout_result_start_name = (TextView) findViewById(R.id.item_map_walk_rout_result_start_name);
        item_map_walk_rout_result_end_name = (TextView) findViewById(R.id.item_map_walk_rout_result_end_name);


        map_walk_rout_result = (LinearLayout) findViewById(R.id.map_walk_rout_result);
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
