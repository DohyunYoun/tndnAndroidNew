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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tndn.app.nyam.R;
import tndn.app.nyam.utils.GpsInfo;
import tndn.app.nyam.utils.PreferenceManager;
import tpmap.android.map.Bounds;
import tpmap.android.map.Coord;
import tpmap.android.map.MapEventListener;
import tpmap.android.map.Pixel;
import tpmap.android.map.TPMap;
import tpmap.android.map.overlay.Balloon;
import tpmap.android.map.overlay.Marker;
import tpmap.android.map.overlay.MarkersLayer;
import tpmap.android.map.overlay.Overlay;
import tpmap.android.map.overlay.OverlayEventListener;
import tpmap.android.utils.Projection;


public class MapMainActivity extends AppCompatActivity implements View.OnClickListener, MapEventListener,
        OverlayEventListener {

    private TextView actionbar_text;
    private Button back;

    private TPMap mapView;

    private EditText map_edittext;
    private ImageView map_search;
    private ImageView map_zoom_in;
    private ImageView map_zoom_out;
    private ImageView map_myspot;

    private ImageView map_route;

    /**
     * 20160907
     * 버스정보 테스트
     */
    private ImageView map_airport_bus;


    private Drawable poi_blue;
    private Drawable ic_here;
    private Drawable blueCircle;
    private Drawable mapDirection;
    private Drawable redCircle;
    private Drawable myspot_selected;
    private Drawable myspot;

    Coord currentLocation;

    private int search_result_position;
    private ArrayList<MapSearchResultData> mapResults;
    private MarkerLayerWidget markerLayer;
//    private IconLayerSample iconLayer;
//    private LabelLayerSample labelLayer;
//    private CustomBalloon customBalloon;

    float currentX;
    float currentY;

    private ProgressDialog pDialog;
    private GpsInfo gps;
    private double latitude;
    private double longitude;

    private int isMySpot = -1;


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
                    Toast.makeText(MapMainActivity.this, toastMsg, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mapView.stopUserLocation();                            // 현재 위치 표시 기능을 OFF합니다.
        mapView.stopView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);


        initView();
        initialize();
        setBitmap();
        isMySpot = -1;

        PreferenceManager.getInstance(MapMainActivity.this).setMaprouteintent("");
        PreferenceManager.getInstance(MapMainActivity.this).setMapstartdata("");
        PreferenceManager.getInstance(MapMainActivity.this).setMapenddata("");

        if (PreferenceManager.getInstance(this).getMapintent().equals("OK")) {
            mapResults = new ArrayList<MapSearchResultData>();
            Intent intent = getIntent();
            mapResults = intent.getParcelableArrayListExtra("MAPRESULTS");
            search_result_position = intent.getIntExtra("POINT", 0);

            PreferenceManager.getInstance(MapMainActivity.this).setMapintent("");


            Coord coord = mapView.getProjection().transCoordination(Projection.WGS84,
                    Projection.UTMK, new Coord(mapResults.get(search_result_position).getX(), mapResults.get(search_result_position).getY()));            // 제주시청 주변 위,경도값
            mapView.setMapCenter(coord);                                // 입렫된 위치로 MAP의 중앙이 이동


            Coord getcoord = mapView.getMapCenter();
            Marker marker = new Marker(getcoord, getResources().getDrawable(R.mipmap.ic_mapmarker));
//            marker.setTitle("말풍선 입니다.");									// 팔뭉선 Message
            markerLayer.addItem(marker);

            /* 2번재 Marker 구현 예제
            Coord coord2 = mapView.getMapCenter();
			coord2.setCoord(coord.getX()+150,coord.getY()+150);
			Marker marker2 = new Marker(coord2,getResources().getDrawable(R.drawable.ic_here_28));
			marker2.setTitle("두번째 Marker입니다.");
			markerLayer.addItem(marker2);
			*/
            mapView.invalidate();                                            // 화면갱신


        } else {
            showpDialog();
            gps = new GpsInfo(MapMainActivity.this);
            if (gps.isGetLocation()) {

                Thread gpsThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        Coord transCoordination = mapView.getProjection().transCoordination(Projection.WGS84,
                                Projection.UTMK, new Coord((float) longitude, (float) latitude));            // 제주시청 주변 위,경도값
                        mapView.setMapCenter(transCoordination);
                        hidepDialog();
                    }
                });
                gpsThread.start();


                try {
                    gpsThread.join();  // 쓰레드 작업 끝날때까지 다른 작업들은 대기
                } catch (Exception e) {
                }


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
                                Coord transCoordination = mapView.getProjection().transCoordination(Projection.WGS84,
                                        Projection.UTMK, new Coord(126.5276157f, 33.5002358f));            // 제주시청 주변 위,경도값
                                mapView.setMapCenter(transCoordination);                                // 입력된 위치로 MAP의 중앙이 이동
                                hidepDialog();
                                return;
                            }
                        }).create().show();
            }
   /*
            if (!new CheckGpsService().chkGpsService(this)) {
                Coord transCoordination = mapView.getProjection().transCoordination(Projection.WGS84,
                        Projection.UTMK, new Coord(126.5276157f, 33.5002358f));            // 제주시청 주변 위,경도값
                mapView.setMapCenter(transCoordination);                                // 입력된 위치로 MAP의 중앙이 이동
                hidepDialog();
            }
            Coord transCoordination = mapView.getProjection().transCoordination(Projection.WGS84,
                    Projection.UTMK, new Coord(126.5276157f, 33.5002358f));            // 제주시청 주변 위,경도값
            mapView.setMapCenter(transCoordination);
            hidepDialog();
*/
//            mapView.showUserLocation(redCircle, mapDirection);

//            Coord transCoordination = mapView.getProjection().transCoordination(Projection.WGS84,
//                    Projection.UTMK, new Coord(126.5276157f, 33.5002358f));            // 제주시청 주변 위,경도값
//            mapView.setMapCenter(transCoordination);                                // 입력된 위치로 MAP의 중앙이 이동
        }

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

        map_edittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //엔터키
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Intent i = new Intent(getApplicationContext(), MapSearchResultActivity.class);
                    i.putExtra("QUERY", map_edittext.getText().toString().replace(" ", ""));
                    startActivity(i);
                    return true;
                }
                return false;
            }
        });

        map_myspot.setOnClickListener(this);
        map_zoom_in.setOnClickListener(this);
        map_zoom_out.setOnClickListener(this);
        map_search.setOnClickListener(this);
        map_route.setOnClickListener(this);

        markerLayer = new MarkerLayerWidget();
//        markerLayer.setEnableBalloon(true);            // Marker Balloon 기능 ON
//        markerLayer.dispatchOverlayEvent(this);        // Marker Layer Listener ON
        mapView.getOverlays().add(markerLayer);

        currentX = 0;
        currentY = 0;

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("find my location...");
        pDialog.setCancelable(false);


    }

    private void initView() {

        actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        back = (Button) findViewById(R.id.actionbar_back_button);

        mapView = (TPMap) findViewById(R.id.map_view);


        mapView.dispatchMapEvent(this);

        map_edittext = (EditText) findViewById(R.id.map_edittext);
        map_search = (ImageView) findViewById(R.id.map_search);
        map_zoom_in = (ImageView) findViewById(R.id.map_zoom_in);
        map_zoom_out = (ImageView) findViewById(R.id.map_zoom_out);
        map_myspot = (ImageView) findViewById(R.id.map_myspot);
        map_route = (ImageView) findViewById(R.id.map_route);

        /**
         * 20160907
         * 버스정보 테스트
         */
        map_airport_bus = (ImageView) findViewById(R.id.map_airport_bus);
        map_airport_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapBusAirportActivity.class));
                finish();
            }
        });

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
            case R.id.map_myspot:                    // Toggle = OFF면 On시키고 ON이면 OFF합니다.

                gps = new GpsInfo(MapMainActivity.this);
                if (gps.isGetLocation()) {

                    if (isMySpot == -1) {
                        mapView.showUserLocation(redCircle, mapDirection);    // 현재 위치 표시 기능을 ON합니다.
                        isMySpot = 0;
                        map_myspot.setImageResource(R.mipmap.btn_myspot_selected);
                    } else if (isMySpot == 0) {
                        mapView.setUserLocation(TPMap.UserLocationType.TYPE_COMPASS_TRACE);
                        isMySpot = 1;
                        map_myspot.setImageResource(R.mipmap.btn_myspot_compass);
                    } else {
                        mapView.stopUserLocation();
                        isMySpot = -1;
                        map_myspot.setImageResource(R.mipmap.btn_myspot);
                    }

                } else {
                    // GPS 를 사용할수 없으므로
                    AlertDialog.Builder gsDialog = new AlertDialog.Builder(MapMainActivity.this);
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

             /*      if (mapView.isUserLocationEnabled()) {                    // 현재 위치 표시 기능이 동작하는지 확인합니다.
                    mapView.stopUserLocation();                            // 현재 위치 표시 기능을 OFF합니다.
                } else {
                    mapView.showUserLocation(redCircle, mapDirection);    // 현재 위치 표시 기능을 ON합니다.
                }
             if (mapView.getUserLocationType() != TPMap.UserLocationType.TYPE_COMPASS_TRACE) {
                    // 나침반이 가르키는 방향과 지도를 일치시키고 움직이면 따라서 지도도 움직입니다.
                    mapView.setUserLocation(TPMap.UserLocationType.TYPE_COMPASS_TRACE);
                } else if (mapView.getUserLocationType() == TPMap.UserLocationType.TYPE_COMPASS_TRACE) {
                    // 지도는 북쪽을 가르키고  움직였을때 지도가 움직이지 않습니다.
                    mapView.setUserLocation(TPMap.UserLocationType.TYPE_BASE);
                }*/
                break;
            case R.id.map_zoom_in:
                mapView.zoomIn();
                break;
            case R.id.map_zoom_out:
                mapView.zoomOut();
                break;

            case R.id.map_search:
                String query = map_edittext.getText().toString().replace(" ", "");
                if (query.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.map_noresult), Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(this, MapSearchResultActivity.class);
                    i.putExtra("QUERY", map_edittext.getText().toString().replace(" ", ""));
                    startActivity(i);
                }
                break;
            case R.id.map_route:

                gps = new GpsInfo(MapMainActivity.this);
                if (gps.isGetLocation()) {

                    Thread gpsThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            currentY = (float) gps.getLatitude();
                            currentX = (float) gps.getLongitude();

                        }
                    });
                    gpsThread.start();


                    try {
                        gpsThread.join();  // 쓰레드 작업 끝날때까지 다른 작업들은 대기
                    } catch (Exception e) {
                    }
                    Intent intent = new Intent(MapMainActivity.this, MapRoutActivity.class);
                    intent.putExtra("CURRENTX", currentX);
                    intent.putExtra("CURRENTY", currentY);
                    startActivity(intent);

                    hidepDialog();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.map_route_noregion), Toast.LENGTH_SHORT).show();

                } else {
                    // GPS 를 사용할수 없으므로
                    Intent intent = new Intent(this, MapRoutActivity.class);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.map_route_noregion), Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }


    // For MapEventListener
    ArrayList<Coord> addPositionViews = new ArrayList<Coord>();
    private Balloon balloon;

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
        hidepDialog();
        return false;
    }


    // For Overlay Event
    @Override
    public boolean onTouch(Overlay overlay, float x, float y) {
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
