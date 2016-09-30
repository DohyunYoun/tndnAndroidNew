package tndn.app.nyam.map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tndn.app.nyam.R;
import tndn.app.nyam.utils.GpsInfo;
import tpmap.android.map.Bounds;
import tpmap.android.map.Coord;
import tpmap.android.map.MapEventListener;
import tpmap.android.map.Pixel;
import tpmap.android.map.TPMap;
import tpmap.android.map.overlay.Marker;
import tpmap.android.map.overlay.MarkersLayer;
import tpmap.android.map.overlay.Overlay;
import tpmap.android.map.overlay.OverlayEventListener;
import tpmap.android.map.overlay.Polyline;
import tpmap.android.utils.Projection;


public class MapPublicRoutMainActivity extends AppCompatActivity implements View.OnClickListener, MapEventListener,
        OverlayEventListener {

    private TextView actionbar_text;
    private Button back;

    private TPMap mapView;

    private ImageView map_rout_zoom_in;
    private ImageView map_rout_zoom_out;
    private ImageView map_rout_myspot;


    private Drawable poi_blue;
    private Drawable ic_here;
    private Drawable blueCircle;
    private Drawable mapDirection;
    private Drawable redCircle;
    private Drawable myspot_selected;
    private Drawable myspot;

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

    int[] number = {R.mipmap.ic_number_1, R.mipmap.ic_number_2, R.mipmap.ic_number_3, R.mipmap.ic_number_4, R.mipmap.ic_number_5, R.mipmap.ic_number_6, R.mipmap.ic_number_7, R.mipmap.ic_number_8, R.mipmap.ic_number_9, R.mipmap.ic_number_10, R.mipmap.ic_number_11, R.mipmap.ic_number_12, R.mipmap.ic_number_13, R.mipmap.ic_number_14, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15};

    int walk_code;
    int walk_code2;
    ArrayList<MapPointsData> pointslist1;
    ArrayList<MapPointsData> pointslist2;

    private MarkerLayerWidget markerLayer;
//    private IconLayerSample iconLayer;
//    private LabelLayerSample labelLayer;
//    private CustomBalloon customBalloon;

    private ProgressDialog pDialog;

    private int isMySpot = -1;
    private GpsInfo gps;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:                                //정상적인 Key 인증완료
                    mapView.invalidate();
                    break;
                case 1:
                    // mapView.refreshSize();
                    break;
                case 2:                                // Key 인증 실패처리
                    String toastMsg = (String) msg.obj;
                    Toast.makeText(MapPublicRoutMainActivity.this, toastMsg, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        mapView.stopView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_rout_main);


        initView();
        initialize();
        setBitmap();


        Intent i = getIntent();
        pathData = i.getParcelableExtra("PATHDATA");
        s_mapresult = i.getParcelableExtra("S_MAPRESULT");
        e_mapresult = i.getParcelableExtra("E_MAPRESULT");
        pointslist1 = i.getParcelableArrayListExtra("POINTSLIST1");
        pointslist2 = i.getParcelableArrayListExtra("POINTSLIST2");

        infoData = pathData.getInfo();
        subPathData = pathData.getSubpath();
        subPath01Data = subPathData.getSub01();
        subPath02Data = subPathData.getSub02();
        subPath02_laneData = subPath02Data.getLane();
        subPath03Data = subPathData.getSub03();

        Coord startpoint = new Coord(s_mapresult.getX(), s_mapresult.getY());
        Coord endpoint = new Coord(e_mapresult.getX(), e_mapresult.getY());
        Coord startbus = new Coord(subPath02Data.getStartX(), subPath02Data.getStartY());
        Coord startwalk = new Coord(subPath02Data.getEndX(), subPath02Data.getEndY());

        Coord transEnd = mapView.getProjection().transCoordination(Projection.WGS84,
                Projection.UTMK, endpoint);
        mapView.setMapCenter(transEnd);                                // 입력된 위치로 MAP의 중앙이 이동
        Coord endgetCoord = mapView.getMapCenter();
        final Marker endMarker = new Marker(endgetCoord, getResources().getDrawable(R.mipmap.ic_end_marker));

        final Coord transBus = mapView.getProjection().transCoordination(Projection.WGS84,
                Projection.UTMK, startbus);
        mapView.setMapCenter(transBus);                                // 입력된 위치로 MAP의 중앙이 이동
        Coord busgetCoord = mapView.getMapCenter();
        final Marker busMarker = new Marker(busgetCoord, getResources().getDrawable(R.mipmap.ic_rout_bus));

        final Coord transWalk = mapView.getProjection().transCoordination(Projection.WGS84,
                Projection.UTMK, startwalk);
        mapView.setMapCenter(transWalk);                                // 입력된 위치로 MAP의 중앙이 이동
        Coord walkgetCoord = mapView.getMapCenter();
        final Marker walkMarker = new Marker(walkgetCoord, getResources().getDrawable(R.mipmap.ic_rout_walk));

        Coord transStart = mapView.getProjection().transCoordination(Projection.WGS84,
                Projection.UTMK, startpoint);
        mapView.setMapCenter(transStart);                                // 입력된 위치로 MAP의 중앙이 이동
        Coord startgetCoord = mapView.getMapCenter();
        final Marker startMarker = new Marker(startgetCoord, getResources().getDrawable(R.mipmap.ic_start_marker));

        Coord[] routs = new Coord[pointslist1.size() + pointslist2.size() + 2];

        for (int j = 0; j < pointslist1.size(); j++) {
            Coord transCoord = mapView.getProjection().transCoordination(Projection.WGS84,
                    Projection.UTMK, new Coord(pointslist1.get(j).getX(), pointslist1.get(j).getY()));
            routs[j] = transCoord;
        }

        routs[pointslist1.size()] = transBus;
        routs[pointslist1.size() + 1] = transWalk;
        for (int k = 0; k < pointslist2.size(); k++) {
            Coord transCoord = mapView.getProjection().transCoordination(Projection.WGS84,
                    Projection.UTMK, new Coord(pointslist2.get(k).getX(), pointslist2.get(k).getY()));
            routs[k + pointslist1.size() + 2] = transCoord;
        }


        Polyline polyline = new Polyline(routs, 20, getResources().getColor(R.color.blue_bus));
        polyline.setVisible(true);

        mapView.getOverlays().add(polyline);

        mapView.getOverlays().add(markerLayer);

        markerLayer.addItem(startMarker);
        markerLayer.addItem(walkMarker);
        markerLayer.addItem(busMarker);
        markerLayer.addItem(endMarker);

        mapView.invalidate();

    }

    private void initialize() {

        actionbar_text.setText(getResources().getString(R.string.map));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mapView.setMultiLang(TPMap.LangType.TYPE_CHIKOR);
        mapView.setMapResolution(TPMap.NORMAL_BIC_FONT);            // resolution BIG Setting


        map_rout_myspot.setOnClickListener(this);
        map_rout_zoom_in.setOnClickListener(this);
        map_rout_zoom_out.setOnClickListener(this);

        markerLayer = new MarkerLayerWidget();
//        markerLayer.setEnableBalloon(true);            // Marker Balloon 기능 ON
//        markerLayer.dispatchOverlayEvent(this);        // Marker Layer Listener ON

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

        pointslist1 = new ArrayList<MapPointsData>();
        pointslist2 = new ArrayList<MapPointsData>();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);

    }

    private void initView() {

        actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        back = (Button) findViewById(R.id.actionbar_back_button);

        mapView = (TPMap) findViewById(R.id.map_rout_view);


        mapView.dispatchMapEvent(this);

        map_rout_zoom_in = (ImageView) findViewById(R.id.map_rout_zoom_in);
        map_rout_zoom_out = (ImageView) findViewById(R.id.map_rout_zoom_out);
        map_rout_myspot = (ImageView) findViewById(R.id.map_rout_myspot);
    }


    private void setBitmap() {
//        poi_blue = getBaseContext().getResources().getDrawable(R.drawable.poi_blue);
//        ic_here = getBaseContext().getResources().getDrawable(R.drawable.ic_here_28);
//        blueCircle = getBaseContext().getResources().getDrawable(R.drawable.blue_circle);
        mapDirection = getBaseContext().getResources().getDrawable(R.mipmap.map_circinus);
        redCircle = getBaseContext().getResources().getDrawable(R.mipmap.blue_circle);

        float totalsize = Debug.getNativeHeapAllocatedSize()
                + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        Log.i("LTG",
                "current size 1:" + (totalsize / 1024.0 / 1024.0) + "/ "
                        + Environment.getRootDirectory());

    }


    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        switch (id) {
            case R.id.map_rout_myspot:                    // Toggle = OFF면 On시키고 ON이면 OFF합니다.

                gps = new GpsInfo(MapPublicRoutMainActivity.this);
                if (gps.isGetLocation()) {

                    if (isMySpot == -1) {
                        mapView.showUserLocation(redCircle, mapDirection);    // 현재 위치 표시 기능을 ON합니다.
                        isMySpot = 0;
                        map_rout_myspot.setImageResource(R.mipmap.btn_myspot_selected);
                    } else if (isMySpot == 0) {
                        mapView.setUserLocation(TPMap.UserLocationType.TYPE_COMPASS_TRACE);
                        isMySpot = 1;
                        map_rout_myspot.setImageResource(R.mipmap.btn_myspot_compass);
                    } else {
                        mapView.stopUserLocation();
                        isMySpot = -1;
                        map_rout_myspot.setImageResource(R.mipmap.btn_myspot);
                    }

                } else {
                    // GPS 를 사용할수 없으므로
                    AlertDialog.Builder gsDialog = new AlertDialog.Builder(MapPublicRoutMainActivity.this);
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
                break;
            case R.id.map_rout_zoom_in:
                mapView.zoomIn();
                break;
            case R.id.map_rout_zoom_out:
                mapView.zoomOut();
                break;

        }
    }


    // For MapEventListener

    @Override
    public boolean onTouch(Pixel position) {
        // TODO Auto-generated method stub
        Log.d("TNDN_LOG", "onTouch : " + position.toString());


        return false;
    }

    // Map 화면에서 더블 Touch시 Call되는 함수입니다.
    // 현재는 SDK에서 자체  Zoom In 처리를 하고 Call하지 안습니다.
    @Override
    public boolean onDoubleTouch(Pixel position) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onMultiTouch(Pixel[] positionArray) {
        // TODO Auto-generated method stub
        return false;
    }

    // Map 화면에서 Long Touch시 Call됩니다.
    @Override
    public boolean onLongTouch(Pixel position) {
        // TODO Auto-generated method stub
        /*
        ImageView image = new ImageView(this);

		image.setImageResource(R.drawable.blue_circle);
		Coord coord = mapView.getProjection().toCoord(position);

		TPMap.LayoutParams lp = new TPMap.LayoutParams(LayoutParams.WRAP_CONTENT, // 차지할 폭
				LayoutParams.WRAP_CONTENT, // 차지할 높이
				coord, // 이미지를 나타낼 위치
				TPMap.LayoutParams.CENTER, 0, 0); // 중앙에 정렬

		// 맵뷰에 이미지 출력하기
		Animation slide_in = AnimationUtils.loadAnimation(mapView.getContext(), R.anim.bounce);

		image.setAnimation(slide_in);

		mapView.addView(image, lp);
		mapView.invalidate();
		addPositionViews.add(coord);
		*/
        return false;
    }

    // Map Level이 바뀌면 알려줍니다.
    @Override
    public boolean onChangeZoomLevel(boolean isZoomIn, int zoomlevel) {
        // TODO Auto-generated method stub

        //Log.d("yun","onChangeZoomLevel() isZoomIn:"+isZoomIn+" zoomlevel:"+zoomlevel);

		/*
        Toast.makeText(MainActivity.this, "onChangeZoomLevel() isZoomIn:"+isZoomIn+" zoomlevel:"+zoomlevel,
				Toast.LENGTH_LONG).show();
		*/
        return false;
    }

    // Map Initial 후 결과를 알려쥡니다.
    // Network이 이상이 생기는 경우 false Map Level이 바뀌면 알려줍니다.
    @Override
    public boolean onMapInitializing(boolean isSuccess) {
        // TODO Auto-generated method stub

        return false;
    }

    // 화면의 범위가 바뀌면 알려쥡니다.
    @Override
    public boolean onBoundsChange(Bounds mapBounds) {
        // TODO Auto-generated method stub
        // Log.d("hts", "mapBounds : " + mapBounds.toString());
        return false;
    }


    // For Overlay Event
    @Override
    public boolean onTouch(Overlay overlay, float x, float y) {
        switch (overlay.getOverlayType()) {
            case Overlay.OVERLAY_TYPE_POLYLINE:
                Log.i("overlay", "polyline");
                break;
        }
        // TODO Auto-generated method stub
      /*  int[] focusArray = markerLayer.getFocusIndexArray();
        if (focusArray != null) {
            for (int i = 0; i < focusArray.length; i++) {
                Log.i("overlay", "focusArray : " + focusArray[i]);
            }
        }
        switch (overlay.getOverlayType()) {
            case Overlay.OVERLAY_TYPE_BALLOON:
                if (balloon != null) {
                    balloon.onTouchEvent(mapView, x, y);
                    mapView.getOverlays().remove(balloon);
                }
                break;
            case Overlay.OVERLAY_TYPE_CLUSTER:
                Log.d("hts", "~~~~~~~~~~~~~~~~~~");
                break;
            case Overlay.OVERLAY_TYPE_CIRCLE:
                Log.i("overlay", "circle");
                break;
            case Overlay.OVERLAY_TYPE_IMAGE:
                Log.i("overlay", "image");
                break;
            case Overlay.OVERLAY_TYPE_IMAGE_LAYER:
                Log.i("overlay", "image layer");
                break;
            case Overlay.OVERLAY_TYPE_LABEL:
                Log.i("overlay", "label");
                break;
            case Overlay.OVERLAY_TYPE_LABEL_LAYER:
                Log.i("overlay", "label layer");
                break;
            case Overlay.OVERLAY_TYPE_MARKER:
                // Log.i("overlay", "marker");
                Marker marker = (Marker) overlay;
                int height = marker.getIconSize().getY().intValue();
                if (balloon != null) {
                    mapView.getOverlays().remove(balloon);
                }
                balloon = new Balloon(marker.getTitle(), marker.getPoint());
                balloon.setHeightOff(height);
                mapView.getOverlays().add(balloon);

                mapView.invalidate();
                break;
            case Overlay.OVERLAY_TYPE_MARKER_LAYER:
                Log.i("overlay", "marker layer");
                break;
            case Overlay.OVERLAY_TYPE_POLYGON:
                Log.i("overlay", "polygon");
                break;
            case Overlay.OVERLAY_TYPE_POLYLINE:
                Log.i("overlay", "polyline");
                break;
        }
*/
        return false;
    }

    @Override
    public boolean onDoubleTouch(Overlay overlay, float x, float y) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onLongTouch(Overlay overlay, float x, float y) {
        // TODO Auto-generated method stub
        return false;
    }

    class MarkerLayerWidget extends MarkersLayer {


        ArrayList<Marker> list = new ArrayList<Marker>();

        @Override
        protected Marker getMarker(int index) {
            // TODO Auto-generated method stub
            if (index >= list.size())
                return null;
            return list.get(index);
        }

        @Override
        protected int size() {
            // TODO Auto-generated method stub
            return list.size();
        }

        public void addItem(Marker mark) {
            list.add(mark);
        }

        public void removeAll() {
            list.clear();
            list = new ArrayList<Marker>();
        }

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
