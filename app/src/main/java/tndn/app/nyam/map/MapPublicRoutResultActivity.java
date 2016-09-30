package tndn.app.nyam.map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tndn.app.nyam.R;

public class MapPublicRoutResultActivity extends AppCompatActivity {

    private TextView actionbar_text;
    private Button back;

    private ProgressDialog pDialog;

    private ListView lv_public_rout;
    private TextView map_public_rout_result_totalcount;

    private MapPublicRoutResultAdapter mAdapter;
    private ArrayList<MapPublicPathData> pathDatas;
    private MapPublicPathEtcData pathEtcData;
    private MapSearchResultData s_mapresult;
    private MapSearchResultData e_mapresult;

    String pathType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_public_rout_result);


        initView();
        initialize();
        Intent i = getIntent();
        pathDatas = i.getParcelableArrayListExtra("PATHDATAS");
        pathEtcData = i.getParcelableExtra("ETCDATA");
        s_mapresult = i.getParcelableExtra("S_MAPRESULT");
        e_mapresult = i.getParcelableExtra("E_MAPRESULT");

//        pathType = pathDatas.get;
//
//        subPathData = new MapPublicPathSubPathData();
//        subPath01Data = new MapPublicPathSubPath01Data();
//        subPath02Data = new MapPublicPathSubPath02Data();
//        subPath02_laneData = new MapPublicPathSubPath02_laneData();
//        subPath03Data = new MapPublicPathSubPath01Data();
//
//        infoData = new MapPublicPathInfoData();
        int totalcount = pathEtcData.getBusCount() + pathEtcData.getSubwayCount() + pathEtcData.getSubwayBusCount();
        map_public_rout_result_totalcount.setText("搜索结果 : " + totalcount + "个");

        lv_public_rout = (ListView) findViewById(R.id.map_public_rout_result_listview);
        mAdapter = new MapPublicRoutResultAdapter(this, pathDatas);
        lv_public_rout.setAdapter(mAdapter);

        lv_public_rout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapPublicRoutResultDetailActivity.class);
                intent.putExtra("PATHDATA", pathDatas.get(position));
                intent.putExtra("ETCDATA", pathEtcData);
                intent.putExtra("S_MAPRESULT", s_mapresult);
                intent.putExtra("E_MAPRESULT", e_mapresult);
                startActivity(intent);
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

        pathDatas = new ArrayList<MapPublicPathData>();
        pathEtcData = new MapPublicPathEtcData();
        s_mapresult = new MapSearchResultData();
        e_mapresult = new MapSearchResultData();

        pathType = null;

    }

    private void initView() {
        actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        back = (Button) findViewById(R.id.actionbar_back_button);

        map_public_rout_result_totalcount = (TextView) findViewById(R.id.map_public_rout_result_totalcount);

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
