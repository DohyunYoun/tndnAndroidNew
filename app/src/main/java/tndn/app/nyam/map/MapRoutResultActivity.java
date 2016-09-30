package tndn.app.nyam.map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tndn.app.nyam.R;

public class MapRoutResultActivity extends AppCompatActivity {

    private TextView actionbar_text;
    private Button back;

    private ProgressDialog pDialog;

    private ListView lv_rout;
    private TextView name;
    private TextView dist;
    private TextView time;
    private TextView cost;

    private MapRoutResultAdapter mAdapter;
    private ArrayList<MapRoutNodeData> routNodes;
    private ArrayList<MapRoutLinkData> routLinks;
    private MapSearchResultData s_mapresult;
    private MapSearchResultData e_mapresult;
    private String str_name;
    private String str_dist;
    private String str_time;
    private String str_cost;
    private LinearLayout map_rout_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_rout_result);


        initView();
        initialize();

        Intent i = getIntent();
        routNodes = i.getParcelableArrayListExtra("ROUTNODES");
        routLinks = i.getParcelableArrayListExtra("ROUTLINKS");
        s_mapresult = i.getParcelableExtra("S_MAPRESULT");
        e_mapresult = i.getParcelableExtra("E_MAPRESULT");
        str_name = s_mapresult.getName() + " (" + s_mapresult.getName_kor() + ")";
        str_dist = i.getStringExtra("CARDIST");
        str_time = i.getStringExtra("CARTIME");
        str_cost = i.getStringExtra("CARCOST");

        name.setText(str_name);
        dist.setText(str_dist);
        time.setText(str_time);
        cost.setText(str_cost);

        lv_rout = (ListView) findViewById(R.id.map_rout_result_listview);
        mAdapter = new MapRoutResultAdapter(this, routNodes, routLinks);
        lv_rout.setAdapter(mAdapter);

        actionbar_text.setText(getResources().getString(R.string.map));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        map_rout_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), MapRoutMainActivity.class);
                i.putParcelableArrayListExtra("ROUTNODES", routNodes);
                i.putParcelableArrayListExtra("ROUTLINKS", routLinks);
                i.putExtra("S_MAPRESULT", s_mapresult);
                i.putExtra("E_MAPRESULT", e_mapresult);
                i.putExtra("CARDIST", str_dist);
                i.putExtra("CARTIME", str_time);
                i.putExtra("CARCOST", str_cost);
                startActivity(i);

            }
        });

    }       //end oncreate

    private void initialize() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);

        routNodes = new ArrayList<MapRoutNodeData>();
        routLinks = new ArrayList<MapRoutLinkData>();
        s_mapresult = new MapSearchResultData();
        e_mapresult = new MapSearchResultData();

    }

    private void initView() {
        actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        back = (Button) findViewById(R.id.actionbar_back_button);

        name = (TextView) findViewById(R.id.map_rout_result_name);
        dist = (TextView) findViewById(R.id.map_rout_result_dist);
        time = (TextView) findViewById(R.id.map_rout_result_time);
        cost = (TextView) findViewById(R.id.map_rout_result_cost);
        map_rout_result = (LinearLayout) findViewById(R.id.map_rout_result);

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
