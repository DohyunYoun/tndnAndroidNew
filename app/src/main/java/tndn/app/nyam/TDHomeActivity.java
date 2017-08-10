package tndn.app.nyam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.viewpagerindicator.CirclePageIndicator;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import tndn.app.nyam.adapter.BannerNetworkImagePagerAdapter;
import tndn.app.nyam.adapter.ImagePagerAdapter;
import tndn.app.nyam.adapter.LocalSpinnerAdapter;
import tndn.app.nyam.adapter.NetworkImagePagerAdapter;
import tndn.app.nyam.adapter.VoiceAdapter;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.BackPressCloseHandler;
import tndn.app.nyam.utils.IsOnline;
import tndn.app.nyam.utils.LogHome;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.SaveExchangeRate;
import tndn.app.nyam.utils.SetListViewHeight;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;
import tndn.app.nyam.widget.HeightWrappingViewPager;

public class TDHomeActivity extends AppCompatActivity implements View.OnClickListener, BeaconConsumer {

    /**
     * service
     */
    private ProgressDialog pDialog;

    /**
     * spinner
     */
    private Spinner localSpinner;


    private ImageView main_restaurant_imageview;
    private ImageView main_sight_imageview;
    private ImageView main_magazine_imageview;
    private ImageView main_map_imageview;
    private ImageView main_rate_imageview;
    private ImageView main_voice_imageview;
    private ImageView main_store_imageview;
    private ImageView main_assistant_imageview;

    private ImageView main_recommend01;
    private ImageView main_recommend02;
    private ImageView main_recommend03;
    private ImageView main_recommend04;


    //    private ImageView main_function01;
    private ImageView main_banner01;
    private ImageView main_banner02;

    private NetworkImageView banner_simya;
    private NetworkImageView banner_samjin;
    private NetworkImageView main_today;


    private LinearLayout main_exchagerate_ll;
    private LinearLayout main_quality_request;


    private ImageView main_category_cityhall;
    private ImageView main_category_baozen;
    private ImageView main_category_joongangro;
    private ImageView main_category_jejusi;
    private ImageView main_category_aewol;
    private ImageView main_category_jejuwest;
    private ImageView main_category_waljeong;
    private ImageView main_category_sungsan;
    private ImageView main_category_jejueast;
    private ImageView main_category_cheonjiyeon;
    private ImageView main_category_joongmoon;
    private ImageView main_category_seoguipo;
    private ImageView main_category_seoul;
    private ImageView main_category_suwon;


    private TextView main_rate_date;
    private TextView main_rate_time;
    private TextView main_rate_rate;


    private LinearLayout main_jejuweekly_01;
    private LinearLayout main_jejuweekly_02;

    String curr_date;
    private String intentURL;

    //    for voice
    private ArrayList<String> text;
    private ArrayList<Integer> sounds;
    ListView main_voice_listview;
    private VoiceAdapter mVoiceAdapter;

    public static int mSelectedItem = -1;


    //for image slider
    private HeightWrappingViewPager main_viewpager;
    private ImagePagerAdapter mImagePagerAdapter;
    private BannerNetworkImagePagerAdapter mNetworkImagePagerAdapter;
    private CirclePageIndicator indicator;
    ArrayList<Integer> idx_images;
    ArrayList<HashMap<String, String>> banners;


    private BackPressCloseHandler backPressCloseHandler;
    Spinner actionbar_local_spinner;
    private boolean tmp_check;


    ImageLoader mImageLoader;


    /**
     * for beacon
     * usin altbeacon library
     */
    private BeaconManager beaconManager;
    // 감지된 비콘들을 임시로 담을 리스트
    private ArrayList<Beacon> beaconList;

    //nearby beacon
    boolean check = false;
    CircularProgressBar mCircularProgressBar;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td_home);

        /*********************************************
         for actionbar
         *********************************************/
        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);
        Button actionbar_qr_button = (Button) findViewById(R.id.actionbar_qr_button);
        ImageView actionbar_nearby = (ImageView) findViewById(R.id.actionbar_nearby);
        mCircularProgressBar = (CircularProgressBar) findViewById(R.id.actionbar_circular_progressbar);


        actionbar_local_spinner = (Spinner) findViewById(R.id.actionbar_local_spinner);

        actionbar_local_spinner.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
        actionbar_qr_button.setVisibility(View.VISIBLE);

        actionbar_nearby.setVisibility(View.VISIBLE);
        actionbar_text.setText("周边");
        actionbar_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCircularProgressBar.setVisibility(View.VISIBLE);
                if (check) {
                    //stop
                    ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).progressiveStop();
                    check = false;
                    beaconUnbind();
                    handler.removeMessages(0);
                } else {
                    //start
                    if (mBluetoothAdapter == null) {
                        // Device does not support Bluetooth
                        Log.i("DEBUG_TAG", "============블루투스지원안해요");
                        Toast.makeText(getApplicationContext(), "invalid bluetooth device", Toast.LENGTH_SHORT).show();
                    } else {//지원한다면..
                        if (!mBluetoothAdapter.isEnabled()) {//환경설정에서  enable여부, 켜져있는 상태이면 true
                            mBluetoothAdapter.enable();
//                            Log.i("DEBUG_TAG", "============블루투스활성화");
                        }
//                        else{
//                            mBluetoothAdapter.disable();
////                            Log.i("DEBUG_TAG", "============블루투스비활성화");
//                        }
                        ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).start();
                        check = true;
                        beaconBind();
                        handler.sendEmptyMessage(0);
                    }
                }
            }
        });
        actionbar_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCircularProgressBar.setVisibility(View.VISIBLE);
                if (check) {
                    //stop
                    ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).progressiveStop();
                    check = false;
                    beaconUnbind();
                    handler.removeMessages(0);
                } else {
                    //start
                    ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).start();
                    check = true;
                    beaconBind();
                    handler.sendEmptyMessage(0);
                }
            }
        });
        actionbar_qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.tndn.SCAN");
                intent.putExtra("SCAN_MODE", "ALL");
                startActivityForResult(intent, 0);
            }
        });


        /*********************************************
         for actionbar
         *********************************************/

        /*********************************************
         for tabbar
         *********************************************/
        final ImageView tabbar_home = (ImageView) findViewById(R.id.tabbar_home);
        final ImageView tabbar_assistant = (ImageView) findViewById(R.id.tabbar_assistant);
        final ImageView tabbar_mypage = (ImageView) findViewById(R.id.tabbar_mypage);
        tabbar_home.setSelected(true);

        tabbar_assistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogHome().send(getApplicationContext(), "tab-assistant");
                startActivity(new Intent(getApplicationContext(), TDAssistantActivity.class));
                finish();
            }
        });
        tabbar_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogHome().send(getApplicationContext(), "tab-mypage");
                startActivity(new Intent(getApplicationContext(), TDMypageActivity.class));
                finish();
            }
        });


        /*********************************************
         for tabbar
         *********************************************/

        new SaveExchangeRate().saveExchangeRate(this);
        curr_date = PreferenceManager.getInstance(this).getCurrDate();

        initView();
        initialize();
        addData();
        getBanner();

        /**
         * spinner
         */
        String[] test = {getResources().getString(R.string.jeju), getResources().getString(R.string.suwonsi), getResources().getString(R.string.seoulsi), getResources().getString(R.string.busansi)};
        final LocalSpinnerAdapter localSpinnerAdapter = new LocalSpinnerAdapter(this, android.R.layout.simple_spinner_item, test);
        localSpinner.setAdapter(localSpinnerAdapter);
        if (PreferenceManager.getInstance(getApplicationContext()).getLocalization().equals("1")) {
            localSpinner.setSelection(0);
            tmp_check = true;

        } else if (PreferenceManager.getInstance(getApplicationContext()).getLocalization().equals("2")) {
            //수원
            localSpinner.setSelection(1);
            tmp_check = true;

        } else if (PreferenceManager.getInstance(getApplicationContext()).getLocalization().equals("3")) {
            //서울
            localSpinner.setSelection(2);
            tmp_check = true;

        } else if (PreferenceManager.getInstance(getApplicationContext()).getLocalization().equals("5")) {
            //부산
            localSpinner.setSelection(3);
            tmp_check = true;

        } else {
            //제주
            localSpinner.setSelection(0);
            tmp_check = true;

        }

        localSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selItem = localSpinner.getSelectedItem().toString();
                if (tmp_check) {
                    tmp_check = false;
                } else {
                    if (position == 0) {
                        tmp_check = true;
                        PreferenceManager.getInstance(getApplicationContext()).setLocalization("1");
                        PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                        PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                    } else if (position == 1) {
                        tmp_check = true;
                        PreferenceManager.getInstance(getApplicationContext()).setLocalization("2");
                        PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                        PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                    } else if (position == 2) {
                        tmp_check = true;
                        PreferenceManager.getInstance(getApplicationContext()).setLocalization("3");
                        PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                        PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                    } else if (position == 3) {
                        tmp_check = true;
                        PreferenceManager.getInstance(getApplicationContext()).setLocalization("5");
                        PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                        PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                    } else {
                        tmp_check = true;
                        PreferenceManager.getInstance(getApplicationContext()).setLocalization("1");
                        PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                        PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                    }
                    Intent i = new Intent(getApplicationContext(), TDHomeActivity.class);
                    startActivity(i);
                    finish();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * spinner end
         */


        main_restaurant_imageview.setOnClickListener(this);
        main_sight_imageview.setOnClickListener(this);
        main_magazine_imageview.setOnClickListener(this);
        main_map_imageview.setOnClickListener(this);
        main_rate_imageview.setOnClickListener(this);
        main_voice_imageview.setOnClickListener(this);
        main_store_imageview.setOnClickListener(this);
        main_assistant_imageview.setOnClickListener(this);

        main_recommend01.setOnClickListener(this);
        main_recommend02.setOnClickListener(this);
        main_recommend03.setOnClickListener(this);
        main_recommend04.setOnClickListener(this);


        main_category_cityhall.setOnClickListener(this);
        main_category_baozen.setOnClickListener(this);
        main_category_joongangro.setOnClickListener(this);
        main_category_jejusi.setOnClickListener(this);
        main_category_aewol.setOnClickListener(this);
        main_category_jejuwest.setOnClickListener(this);
        main_category_waljeong.setOnClickListener(this);
        main_category_sungsan.setOnClickListener(this);
        main_category_jejueast.setOnClickListener(this);
        main_category_cheonjiyeon.setOnClickListener(this);
        main_category_joongmoon.setOnClickListener(this);
        main_category_seoguipo.setOnClickListener(this);
        main_category_seoul.setOnClickListener(this);
        main_category_suwon.setOnClickListener(this);

//        main_function01.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.weibo.cn/d/tndn?jumpfrom=weibocom"));
//                startActivity(intent);
//            }
//        });

        main_banner01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogHome().send(getApplicationContext(), "banner-magazine-theme01");
                //magazine theme1로 연결
                intentURL = "tndn://magazineTheme?id=1";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentURL));
                startActivity(intent);
            }
        });
        main_banner02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogHome().send(getApplicationContext(), "banner-magazine-theme02");
                //magazine theme2로 연결
                intentURL = "tndn://magazineTheme?id=2";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentURL));
                startActivity(intent);
            }
        });
        main_jejuweekly_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogHome().send(getApplicationContext(), "jejuweekly01");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jejuchina.net/news/articleView.html?idxno=1572"));
                startActivity(intent);
            }
        });
        main_jejuweekly_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogHome().send(getApplicationContext(), "jejuweekly02");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jejuchina.net/news/articleView.html?idxno=1571"));
                startActivity(intent);
            }
        });


        main_exchagerate_ll.setOnClickListener(this);
        main_quality_request.setOnClickListener(this);

        mVoiceAdapter = new VoiceAdapter(this, text, sounds, "home");
        main_voice_listview.setAdapter(mVoiceAdapter);

        main_voice_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedItem = position;
                mVoiceAdapter.notifyDataSetChanged();
            }
        });


        new SetListViewHeight().init(main_voice_listview);
//        setListViewHeightBasedOnChildren(main_voice_listview);

//        int_scrollViewPos = 현재 스크롤 포지션
//        int_TextView_lines = 스크롤뷰 크기

        //Detect Bottom ScrollView

    }

    private void initialize() {
        backPressCloseHandler = new BackPressCloseHandler(this);


        main_rate_date.setText(curr_date.split(" ")[0]);
        main_rate_time.setText(curr_date.split(" ")[1]);
        main_rate_rate.setText(PreferenceManager.getInstance(this).getCurrency());

        text = new ArrayList<String>();
        sounds = new ArrayList<Integer>();
        idx_images = new ArrayList<>();
        banners = new ArrayList<HashMap<String, String>>();


        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);

        mImageLoader = AppController.getInstance().getImageLoader();

        // 실제로 비콘을 탐지하기 위한 비콘매니저 객체를 초기화
        beaconList = new ArrayList<>();

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        // 비콘 탐지를 시작한다. 실제로는 서비스를 시작하는것.
        beaconManager.bind(this);

    }

    private void initView() {


        main_restaurant_imageview = (ImageView) findViewById(R.id.main_restaurant_imageview);
        main_sight_imageview = (ImageView) findViewById(R.id.main_sight_imageview);
        main_magazine_imageview = (ImageView) findViewById(R.id.main_magazine_imageview);
        main_map_imageview = (ImageView) findViewById(R.id.main_map_imageview);
        main_rate_imageview = (ImageView) findViewById(R.id.main_rate_imageview);
        main_voice_imageview = (ImageView) findViewById(R.id.main_voice_imageview);
        main_store_imageview = (ImageView) findViewById(R.id.main_store_imageview);
        main_assistant_imageview = (ImageView) findViewById(R.id.main_assistant_imageview);

        main_recommend01 = (ImageView) findViewById(R.id.main_recommend01);
        main_recommend02 = (ImageView) findViewById(R.id.main_recommend02);
        main_recommend03 = (ImageView) findViewById(R.id.main_recommend03);
        main_recommend04 = (ImageView) findViewById(R.id.main_recommend04);


        main_banner01 = (ImageView) findViewById(R.id.main_banner01);
        main_banner02 = (ImageView) findViewById(R.id.main_banner02);

        banner_simya = (NetworkImageView) findViewById(R.id.banner_simya);
        banner_samjin = (NetworkImageView) findViewById(R.id.banner_samjin);
        main_today = (NetworkImageView) findViewById(R.id.main_today);


        main_category_cityhall = (ImageView) findViewById(R.id.main_category_cityhall);
        main_category_baozen = (ImageView) findViewById(R.id.main_category_baozen);
        main_category_joongangro = (ImageView) findViewById(R.id.main_category_joongangro);
        main_category_jejusi = (ImageView) findViewById(R.id.main_category_jejusi);
        main_category_aewol = (ImageView) findViewById(R.id.main_category_aewol);
        main_category_jejuwest = (ImageView) findViewById(R.id.main_category_jejuwest);
        main_category_waljeong = (ImageView) findViewById(R.id.main_category_waljeong);
        main_category_sungsan = (ImageView) findViewById(R.id.main_category_sungsan);
        main_category_jejueast = (ImageView) findViewById(R.id.main_category_jejueast);
        main_category_cheonjiyeon = (ImageView) findViewById(R.id.main_category_cheonjiyeon);
        main_category_joongmoon = (ImageView) findViewById(R.id.main_category_joongmoon);
        main_category_seoguipo = (ImageView) findViewById(R.id.main_category_seoguipo);
        main_category_seoul = (ImageView) findViewById(R.id.main_category_seoul);
        main_category_suwon = (ImageView) findViewById(R.id.main_category_suwon);

        main_rate_date = (TextView) findViewById(R.id.main_rate_date);
        main_rate_time = (TextView) findViewById(R.id.main_rate_time);
        main_rate_rate = (TextView) findViewById(R.id.main_rate_rate);


        main_jejuweekly_01 = (LinearLayout) findViewById(R.id.main_jejuweekly_01);
        main_jejuweekly_02 = (LinearLayout) findViewById(R.id.main_jejuweekly_02);

        main_exchagerate_ll = (LinearLayout) findViewById(R.id.main_exchagerate_ll);
        main_quality_request = (LinearLayout) findViewById(R.id.main_quality_request);

        main_voice_listview = (ListView) findViewById(R.id.main_voice_listview);

        localSpinner = (Spinner) findViewById(R.id.actionbar_local_spinner);

        findViewById(R.id.main_voice_more).setOnClickListener(this);

        main_viewpager = (HeightWrappingViewPager) findViewById(R.id.main_viewpager);
        indicator = (CirclePageIndicator) findViewById(R.id.main_indicator);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {

            if (resultCode == Activity.RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                //위의 contents 값에 scan result가 들어온다.
                Log.e("TNDN_QRLOG", contents);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.main_restaurant_imageview:
                if (PreferenceManager.getInstance(this).getLocalization().equals("2")) {
                    //수원
                    intentURL = "tndn://getStoreList?mainId=1&id=32";
                    PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                    PreferenceManager.getInstance(getApplicationContext()).setFoodId("");


                } else if (PreferenceManager.getInstance(this).getLocalization().equals("3")) {
                    //서울
                    intentURL = "tndn://getStoreList?mainId=1&id=36";
                    PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                    PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                } else if (PreferenceManager.getInstance(this).getLocalization().equals("5")) {
                    //부산
                    intentURL = "tndn://getStoreList?mainId=1&id=40";
                    PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                    PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                } else {
                    //제주
                    intentURL = "tndn://getStoreList?mainId=1&id=20";
                    PreferenceManager.getInstance(this).setLocalization("1");
                    PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                    PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                }
                PreferenceManager.getInstance(this).setUserfrom("31");
                new LogHome().send(getApplicationContext(), "icon-food");
                break;
            case R.id.main_sight_imageview:
                PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                if (PreferenceManager.getInstance(this).getLocalization().equals("2")) {
                    //수원
                    intentURL = "tndn://getStoreList?mainId=3&id=32";


                } else if (PreferenceManager.getInstance(this).getLocalization().equals("3")) {
                    //서울
                    intentURL = "tndn://getStoreList?mainId=3&id=36";

                } else {
                    //제주
                    intentURL = "tndn://getStoreList?mainId=3&id=29";
                }
                PreferenceManager.getInstance(this).setUserfrom("31");
                new LogHome().send(getApplicationContext(), "icon-attraction");
                break;

            case R.id.main_magazine_imageview:
                intentURL = "tndn://magazine";
                new LogHome().send(getApplicationContext(), "icon-magazine");
                break;

            case R.id.main_map_imageview:
                intentURL = "tndn://map";
                new LogHome().send(getApplicationContext(), "icon-map");
                break;

            case R.id.main_rate_imageview:
                intentURL = "tndn://exchangerate";
                new LogHome().send(getApplicationContext(), "icon-exchangerate");
                break;
            case R.id.main_voice_imageview:
                intentURL = "tndn://voice";
                new LogHome().send(getApplicationContext(), "icon-voice");
                break;
            case R.id.main_store_imageview:
                PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                intentURL = "tndn://getStoreList?mainId=2";
                new LogHome().send(getApplicationContext(), "icon-store");
                break;
            case R.id.main_assistant_imageview:
                intentURL = "tndn://assistant?assistant=false";
                new LogHome().send(getApplicationContext(), "icon-assistant");
                break;
            case R.id.main_recommend01:
                intentURL = "tndn://getStoreInfo?mainId=1&id=6860&name=Ganse客厅";
                PreferenceManager.getInstance(this).setUserfrom("34");
                new LogHome().send(getApplicationContext(), "image-ganse");
                break;
            case R.id.main_recommend02:
                intentURL = "tndn://getStoreInfo?mainId=1&id=6605&name=Nilmori Dong Dong";
                PreferenceManager.getInstance(this).setUserfrom("34");
                new LogHome().send(getApplicationContext(), "image-nilmori");
                break;

            case R.id.main_recommend03:
                intentURL = "tndn://getStoreInfo?mainId=1&id=6666&name=Donpas";
                PreferenceManager.getInstance(this).setUserfrom("34");
                new LogHome().send(getApplicationContext(), "image-donpas");
                break;
            case R.id.main_recommend04:
                intentURL = "tndn://getStoreInfo?mainId=1&id=3513&name=Beoltae";
                PreferenceManager.getInstance(this).setUserfrom("34");
                new LogHome().send(getApplicationContext(), "image-beoltae");
                break;
            case R.id.main_category_cityhall:
                intentURL = "tndn://getStoreList?mainId=1&id=21";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("21");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-cityhall");
                break;
            case R.id.main_category_baozen:
                intentURL = "tndn://getStoreList?mainId=1&id=20";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("20");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-baozen");
                break;
            case R.id.main_category_joongangro:
                intentURL = "tndn://getStoreList?mainId=1&id=22";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("22");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                new LogHome().send(getApplicationContext(), "image-joongangro");
                break;
            case R.id.main_category_jejusi:
                intentURL = "tndn://getStoreList?mainId=1&id=26";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("26");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                new LogHome().send(getApplicationContext(), "image-jejusi");
                break;
            case R.id.main_category_aewol:
                intentURL = "tndn://getStoreList?mainId=1&id=23";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("23");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-aewol");

                break;
            case R.id.main_category_jejuwest:
                intentURL = "tndn://getStoreList?mainId=1&id=30";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("30");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                new LogHome().send(getApplicationContext(), "image-jejuwest");
                break;
            case R.id.main_category_waljeong:
                intentURL = "tndn://getStoreList?mainId=1&id=24";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("24");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-waljeong");

                break;
            case R.id.main_category_sungsan:
                intentURL = "tndn://getStoreList?mainId=1&id=25";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("25");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                new LogHome().send(getApplicationContext(), "image-sungsan");
                break;
            case R.id.main_category_jejueast:
                intentURL = "tndn://getStoreList?mainId=1&id=29";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("29");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-jejueast");

                break;
            case R.id.main_category_cheonjiyeon:
                intentURL = "tndn://getStoreList?mainId=1&id=34";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("34");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-cheonjiyeon");

                break;
            case R.id.main_category_joongmoon:
                intentURL = "tndn://getStoreList?mainId=1&id=27";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("27");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-joongmoon");

                break;
            case R.id.main_category_seoguipo:
                intentURL = "tndn://getStoreList?mainId=1&id=28";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("28");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-seoguipo");
                break;
            case R.id.main_category_suwon:
                intentURL = "tndn://getStoreList?mainId=1&id=32";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("2");
                PreferenceManager.getInstance(this).setLocationId("32");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                new LogHome().send(getApplicationContext(), "image-suwon");
                break;
            case R.id.main_category_seoul:
                intentURL = "tndn://getStoreList?mainId=1&id=36";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("3");
                PreferenceManager.getInstance(this).setLocationId("36");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-seoul");

                break;

            case R.id.main_exchagerate_ll:
                intentURL = "tndn://exchangerate";
                new LogHome().send(getApplicationContext(), "banner-exchagerate");
                break;

            case R.id.main_quality_request:
                intentURL = new TDUrls().qualityRequest;
                new LogHome().send(getApplicationContext(), "banner-qualityRequest");
break;
            case R.id.main_voice_more:
                intentURL = "tndn://voice";
                new LogHome().send(getApplicationContext(), "banner-voice");
                break;

        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentURL));
        startActivity(intent);
    }

    private void errorBanner() {
        //for image slider
        int[] mImages = {
                R.mipmap.img_app_function_01,
                R.mipmap.img_app_function_02,
                R.mipmap.banner_aidibao,
                R.mipmap.banner_rentcar
        };

        mImagePagerAdapter = new ImagePagerAdapter(getApplicationContext(), mImages);
        main_viewpager.setAdapter(mImagePagerAdapter);
        indicator.setViewPager(main_viewpager);

        banner_simya.setDefaultImageResId(R.mipmap.banner_simya);
        banner_samjin.setDefaultImageResId(R.mipmap.banner_samjin);
        main_today.setDefaultImageResId(R.mipmap.banner_ganse);

        banner_simya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tndn://banner?id=simya")));
                PreferenceManager.getInstance(getApplicationContext()).setUserfrom("40");
                new LogHome().send(getApplicationContext(), "banner-simya");
            }
        });
        banner_samjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tndn://getStoreInfo?mainId=1&id=7086&name=Samjin鱼丸")));
                PreferenceManager.getInstance(getApplicationContext()).setUserfrom("40");
                new LogHome().send(getApplicationContext(), "banner-samjin");
            }
        });
        main_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tndn://getStoreInfo?mainId=1&id=6860&name=Ganse客厅")));
                PreferenceManager.getInstance(getApplicationContext()).setUserfrom("34");
                new LogHome().send(getApplicationContext(), "banner-ganse");
            }
        });
    }

    private void getBanner() {

        if (!new IsOnline().onlineCheck(this)) {                  //internet check failed start
            errorBanner();
            Toast.makeText(this, "Internet Access Failed", Toast.LENGTH_SHORT).show();
        } else { //internet check success start
            showpDialog();
            String url = new TDUrls().getBannerURL + "?id=" + new UserLog().getLog(getApplicationContext());
            JsonObjectRequest objreq = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject res) {
                    try {
                        if (res.getString("result").equals("failed")) {//if result failed
                            errorBanner();
                            Toast.makeText(getApplicationContext(),
                                    "Internet Access Failed", Toast.LENGTH_SHORT).show();
                        } else {

                            idx_images = new ArrayList<>();
                            JSONArray images = res.getJSONArray("data");
                            for (int i = 0; i < images.length(); i++) {
                                HashMap<String, String> banner = new HashMap<String, String>();

                                JSONObject obj = images.getJSONObject(i);
                                Iterator<String> itr = obj.keys();
                                while (itr.hasNext()) {
                                    String key = itr.next();
                                    String value = obj.getString(key);

                                    switch (key) {
                                        case "id":
                                            if (value.equals("") || value.equals(null) || value.equals("null") || value.equals("NULL") || value == null)
                                                value = "0";
                                            banner.put("id", value);
                                            break;
                                        case "idx_image_file_path":
                                            if (value.equals("") || value.equals(null) || value.equals("null") || value.equals("NULL") || value == null)
                                                value = "0";
                                            banner.put("idx_image_file_path", value);
                                            break;
                                        case "position":
                                            if (value.equals("") || value.equals(null) || value.equals("null") || value.equals("NULL") || value == null)
                                                value = "0";
                                            banner.put("position", value);

                                            break;
                                        case "url_scheme_android":
                                            if (value.equals("") || value.equals(null) || value.equals("null") || value.equals("NULL") || value == null)
                                                value = "0";
                                            banner.put("url_scheme_android", value);

                                            break;
                                        case "sort_no":
                                            if (value.equals("") || value.equals(null) || value.equals("null") || value.equals("NULL") || value == null)
                                                value = "0";
                                            banner.put("sort_no", value);
                                            break;
                                    }//end switch
                                }//end while
                                banners.add(banner);
                            }//end for

                        }//end else (result)
                    } catch (JSONException e) {
                        errorBanner();
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        hidepDialog();
                    } //end jsonexception catch
                    hidepDialog();
//data setting

                    ArrayList<HashMap<String, String>> imagesForBanner = new ArrayList<HashMap<String, String>>();
                    int tmp = 0;
                    for (int j = 0; j < banners.size(); j++) {
                        final int i = j;
                        if (banners.get(j).get("position").equals("0")) {
                            //상단배너
                            idx_images.add(Integer.parseInt(banners.get(j).get("idx_image_file_path")));
                            imagesForBanner.add(banners.get(j));
                        } else if (banners.get(j).get("position").equals("1")) {
                            //개인배너
                            if (tmp == 0) {
                                banner_samjin.setImageUrl(new TDUrls().getImageURL + "?id=" + banners.get(j).get("idx_image_file_path"), mImageLoader);
                                banner_samjin.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(banners.get(i).get("url_scheme_android"))));
                                        PreferenceManager.getInstance(getApplicationContext()).setUserfrom("40");
                                        new LogHome().send(getApplicationContext(), "banner-samjin");
                                    }
                                });
                                tmp++;
                            } else if (tmp == 1) {
                                banner_simya.setImageUrl(new TDUrls().getImageURL + "?id=" + banners.get(j).get("idx_image_file_path"), mImageLoader);
                                banner_simya.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(banners.get(i).get("url_scheme_android"))));
                                        PreferenceManager.getInstance(getApplicationContext()).setUserfrom("40");
                                        new LogHome().send(getApplicationContext(), "banner-simya");
                                    }
                                });
                                tmp++;
                            } else if (tmp == 2) {
                                main_today.setImageUrl(new TDUrls().getImageURL + "?id=" + banners.get(j).get("idx_image_file_path"), mImageLoader);

                                main_today.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(banners.get(i).get("url_scheme_android"))));
                                        PreferenceManager.getInstance(getApplicationContext()).setUserfrom("34");
                                        new LogHome().send(getApplicationContext(), "banner-ganse");
                                    }
                                });
                                tmp++;
                            }

                        }

                    }


                    if (idx_images.size() == 0) {
                        errorBanner();
                    } else {
                        mNetworkImagePagerAdapter = new BannerNetworkImagePagerAdapter(getApplicationContext(), idx_images, imagesForBanner, "banner");
                        main_viewpager.setAdapter(mNetworkImagePagerAdapter);
                        indicator.setViewPager(main_viewpager);
                    }


                }//end response

            }, new Response.ErrorListener() {   //end request

                @Override
                public void onErrorResponse(VolleyError error) {
                    errorBanner();
                    Toast.makeText(getApplicationContext(),
                            "Internet Access Failed", Toast.LENGTH_SHORT).show();
                    //hide the progress dialog
                    hidepDialog();
                }
            });

            // Adding request to request queue

            AppController.getInstance().addToRequestQueue(objreq);
        }//end internet check success
    }


    //    for voice
    private void addData() {
        text.add("女)  谢谢");
        text.add("女)  可以点菜吗？");
        text.add("女)  请帮我拿点碟子");

        text.add("男)  请再拿点水");
        text.add("男)  请问洗手间在哪儿");
        text.add("男)  多少钱");


        sounds.add(R.raw.w_thanks);
        sounds.add(R.raw.w_order);
        sounds.add(R.raw.w_plate);

        sounds.add(R.raw.m_water);
        sounds.add(R.raw.m_toilet);
        sounds.add(R.raw.m_howmuch);

    }


    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    private void showpDialog() {
        if (!TDHomeActivity.this.isFinishing()) {
            if (!pDialog.isShowing())
                pDialog.show();
        }
    }

    private void hidepDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).progressiveStop();
        check = false;
        beaconUnbind();
        handler.removeMessages(0);
        beaconList.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconList.clear();
        mCircularProgressBar.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).progressiveStop();
        check = false;
        beaconUnbind();
        handler.removeMessages(0);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                    }

                    Collections.sort(beaconList, new Comparator<Beacon>() {
                        @Override
                        public int compare(Beacon beacon0, Beacon beacon1) {
                            NumberFormat f = NumberFormat.getInstance();
                            f.setGroupingUsed(false);
                            return Double.compare(Double.parseDouble(f.format(beacon0.getDistance())), Double.parseDouble(f.format(beacon1.getDistance())));
                        }

                    });


                }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //비콘 핸들러 실행시 핸들러를 다시 부를지 꺼버릴지 체크
            boolean check = true;


//            for (Beacon beacon : beaconList) {
//                Log.e("beacon search", beaconList.size() + "     " +
//                        "getBluetoothAddress" + beacon.getBluetoothAddress()
//                        + "        getDistance        " + beacon.getDistance());
//            }
            if (beaconList.size() > 0) {
                switch (beaconList.get(0).getId3().toString()) {
                    case "10722":
                        intentURL = "tndn://getStoreInfo?mainId=1&id=6605&name=Nilmori Dong Dong";
                        new LogHome().send(getApplicationContext(), "beacon-nilmori");
                        break;
                    case "10560":
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tndn://getStoreInfo?mainId=1&id=7086&name=Samjin鱼丸")));
                        intentURL = "tndn://getStoreInfo?mainId=1&id=7086&name=Samjin鱼丸";
                        new LogHome().send(getApplicationContext(), "beacon-samjin");
                        break;
                    case "10727":
                        intentURL = "tndn://getStoreInfo?mainId=1&id=6860&name=Ganse客厅";
                        new LogHome().send(getApplicationContext(), "beacon-ganse");
                        break;
                    case "12643":
                        intentURL = "tndn://getStoreInfo?mainId=1&id=6666&name=Donpas";
                        new LogHome().send(getApplicationContext(), "beacon-donpas");
                        break;
                    case "12544":
                        intentURL = "tndn://getStoreInfo?mainId=1&id=3513&name=Beoltae";
                        new LogHome().send(getApplicationContext(), "beacon-beoltae");
                        break;
                    case "12556":
                        intentURL = "tndn://getStoreInfo?mainId=1&id=6&name=tndn";
                        new LogHome().send(getApplicationContext(), "beacon-tndn");
                        break;

                }
                PreferenceManager.getInstance(getApplicationContext()).setUserfrom("ff");
                beaconUnbind();
                handler.removeMessages(0);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentURL)));
                check = false;

            }
            if (check)
                // 자기 자신을 1초마다 호출
                handler.sendEmptyMessageDelayed(0, 2000);
        }
    };

    private void beaconUnbind() {
        beaconManager.unbind(this);
    }

    private void beaconBind() {
        beaconManager.bind(this);
    }
}
