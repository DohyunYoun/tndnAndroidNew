package tndn.app.nyam.map;

import android.app.AlertDialog;
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
import java.util.Random;

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


public class MapRoutMainActivity extends AppCompatActivity implements View.OnClickListener, MapEventListener,
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

    private ArrayList<MapRoutNodeData> routNodes;
    private ArrayList<MapRoutLinkData> routLinks;
    private MapSearchResultData s_mapresult;
    private MapSearchResultData e_mapresult;
    private String str_name;
    private String str_dist;
    private String str_time;
    private String str_cost;
    int[] number = {R.mipmap.ic_number_1, R.mipmap.ic_number_2, R.mipmap.ic_number_3, R.mipmap.ic_number_4, R.mipmap.ic_number_5, R.mipmap.ic_number_6, R.mipmap.ic_number_7, R.mipmap.ic_number_8, R.mipmap.ic_number_9, R.mipmap.ic_number_10, R.mipmap.ic_number_11, R.mipmap.ic_number_12, R.mipmap.ic_number_13, R.mipmap.ic_number_14, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15, R.mipmap.ic_number_15};

    private Random r = new Random();


    private MarkerLayerWidget markerLayer;
//    private IconLayerSample iconLayer;
//    private LabelLayerSample labelLayer;
//    private CustomBalloon customBalloon;

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
                    Toast.makeText(MapRoutMainActivity.this, toastMsg, Toast.LENGTH_LONG).show();
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

        isMySpot = -1;

        Intent i = getIntent();
        routNodes = i.getParcelableArrayListExtra("ROUTNODES");
        routLinks = i.getParcelableArrayListExtra("ROUTLINKS");
        s_mapresult = i.getParcelableExtra("S_MAPRESULT");
        e_mapresult = i.getParcelableExtra("E_MAPRESULT");
        str_name = s_mapresult.getName() + " (" + s_mapresult.getName_kor() + ")";
        str_dist = i.getStringExtra("CARDIST");
        str_time = i.getStringExtra("CARTIME");
        str_cost = i.getStringExtra("CARCOST");


        //        마커로 선처럼 보이게 해보자
        Coord transCoordination2;

//        갯수를 찾아볼까
        int count = 0;
        for (int temp = 0; temp < routLinks.size(); temp++) {
            for (int temp2 = 0; temp2 < routLinks.get(temp).getPoints().size(); temp2++) {
                count++;
            }
        }
        Coord[] lines = new Coord[count];
        int temp = 0;
        for (int k = 0; k < routLinks.size(); k++) {
//            ArrayList<MapPointsData> linePoints = new ArrayList<MapPointsData>();
            for (int l = 0; l < routLinks.get(k).getPoints().size(); l++) {
//                MapPointsData linePoint = new MapPointsData();
//                linePoint.setX(routLinks.get(k).getPoints().get(l).getX());
//                linePoint.setY(routLinks.get(k).getPoints().get(l).getY());
//                linePoints.add(linePoint);
                Coord coord = new Coord(routLinks.get(k).getPoints().get(l).getX(), routLinks.get(k).getPoints().get(l).getY());

                transCoordination2 = mapView.getProjection().transCoordination(Projection.WGS84,
                        Projection.UTMK, coord);
                lines[temp] = transCoordination2;
//                mapView.setMapCenter(transCoordination2);
//                Coord getcoord = mapView.getMapCenter();
//                Marker marker2 = new Marker(getcoord, getResources().getDrawable(R.mipmap.ic_end_mark));
//                markerLayer.addItem(marker2);
                temp++;
            }


        }
        Polyline polyline = new Polyline(lines, 20, getResources().getColor(R.color.tndn_pink));
        // polyline.dispatchOverlayEvent(this);
        polyline.setVisible(true);

        mapView.getOverlays().add(polyline);


//        Projection p = mapView.getProjection();
        Coord[] points = new Coord[routNodes.size()];
        Coord transCoordination;


        for (int j = 0; j < routNodes.size(); j++) {
            Coord coord = new Coord(routNodes.get(j).getX(), routNodes.get(j).getY());
////            points[j]=p.toCoord(new Pixel(routNodes.get(j).getX(), routNodes.get(j).getY()));

            transCoordination = mapView.getProjection().transCoordination(Projection.WGS84,
                    Projection.UTMK, coord);
            points[j] = transCoordination;

            mapView.setMapCenter(transCoordination);

            Coord getcoord = mapView.getMapCenter();
            Marker marker = null;
            if (j == 0) {
                //출발 마커
                marker = new Marker(getcoord, getResources().getDrawable(R.mipmap.ic_start_marker));
            } else if (j == routNodes.size() - 1) {
                //도착마커
                marker = new Marker(getcoord, getResources().getDrawable(R.mipmap.ic_end_marker));
            } else {
                //그냥 일반 마커들들
                marker = new Marker(getcoord, getResources().getDrawable(number[j]));
            }
            markerLayer.addItem(marker);
        }

        mapView.setMapCenter(points[0]);
        mapView.getOverlays().add(markerLayer);

        mapView.invalidate();

//        Polyline polyline = new Polyline(lines, 50, getResources().getColor(R.color.nyam_pink));
//        polyline.dispatchOverlayEvent(this);
//        marker.setTitle("말풍선 입니다.");									// 팔뭉선 Message

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

        routNodes = new ArrayList<MapRoutNodeData>();
        routLinks = new ArrayList<MapRoutLinkData>();
        s_mapresult = new MapSearchResultData();
        e_mapresult = new MapSearchResultData();

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
                gps = new GpsInfo(MapRoutMainActivity.this);
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
                    AlertDialog.Builder gsDialog = new AlertDialog.Builder(MapRoutMainActivity.this);
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

    private Coord[] randomPoints(int type) {
        Projection p = mapView.getProjection();
        if (type == 0) {
            float c_x, c_y, x, y;
            int point = r.nextInt(1000) + 3;
            Coord[] points = new Coord[point];
            c_x = r.nextFloat() * mapView.getWidth();
            c_y = r.nextFloat() * mapView.getHeight();

            for (int i = 0; i < point; i++) {
                x = (mapView.getWidth() / 8) - (r.nextFloat() * (mapView.getWidth() / 4));
                y = (mapView.getHeight() / 8) - (r.nextFloat() * (mapView.getHeight() / 4));
                points[i] = p.toCoord(new Pixel(c_x + x, c_y + y));
            }
            return points;
        } else {
            Coord[] points = new Coord[1];
            points[0] = p.toCoord(new Pixel(r.nextInt(mapView.getWidth()), r.nextInt(mapView
                    .getHeight())));
            return points;
        }

    }

}
