package tndn.app.nyam;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import tndn.app.nyam.adapter.ImagePagerAdapter;
import tndn.app.nyam.adapter.LocalSpinnerAdapter;
import tndn.app.nyam.adapter.VoiceAdapter;
import tndn.app.nyam.utils.BackPressCloseHandler;
import tndn.app.nyam.utils.LogHome;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.SaveExchangeRate;
import tndn.app.nyam.utils.SetListViewHeight;
import tndn.app.nyam.widget.HeightWrappingViewPager;

public class TDHomeActivity extends AppCompatActivity implements View.OnClickListener {

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
    private ImageView main_talk_imageview;
    private ImageView main_translate_imageview;

    private ImageView main_recommend01;
    private ImageView main_recommend02;
    private ImageView main_recommend03;
    private ImageView main_recommend04;


    //    private ImageView main_function01;
    private ImageView main_banner01;
    private ImageView main_banner02;
    private ImageView main_today;
    private ImageView banner_samjin;


    private LinearLayout main_exchagerate_ll;


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


    private BackPressCloseHandler backPressCloseHandler;
    Spinner actionbar_local_spinner;
    private boolean tmp_check;


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
        actionbar_local_spinner = (Spinner) findViewById(R.id.actionbar_local_spinner);

        actionbar_local_spinner.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
        actionbar_qr_button.setVisibility(View.VISIBLE);

        actionbar_text.setText(getResources().getString(R.string.app_name));
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
        findViewById(R.id.banner_simya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogHome().send(getApplicationContext(), "banner-simya");
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tndn://banner?id=simya")));
            }
        });

        main_restaurant_imageview.setOnClickListener(this);
        main_sight_imageview.setOnClickListener(this);
        main_magazine_imageview.setOnClickListener(this);
        main_map_imageview.setOnClickListener(this);
        main_rate_imageview.setOnClickListener(this);
        main_voice_imageview.setOnClickListener(this);
        main_talk_imageview.setOnClickListener(this);
        main_translate_imageview.setOnClickListener(this);

        main_recommend01.setOnClickListener(this);
        main_recommend02.setOnClickListener(this);
        main_recommend03.setOnClickListener(this);
        main_recommend04.setOnClickListener(this);
        main_today.setOnClickListener(this);
        banner_samjin.setOnClickListener(this);


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

        //for image slider
        int[] mImages = {
                R.mipmap.banner_coupon,
                R.mipmap.img_app_function_01,
                R.mipmap.banner_aidibao,
                R.mipmap.banner_rentcar
        };

        mImagePagerAdapter = new ImagePagerAdapter(this, mImages);
        main_viewpager = (HeightWrappingViewPager) findViewById(R.id.main_viewpager);
        main_viewpager.setAdapter(mImagePagerAdapter);

        CirclePageIndicator indecator = (CirclePageIndicator) findViewById(R.id.main_indicator);
        indecator.setViewPager(main_viewpager);

    }

    private void initialize() {
        backPressCloseHandler = new BackPressCloseHandler(this);


        main_rate_date.setText(curr_date.split(" ")[0]);
        main_rate_time.setText(curr_date.split(" ")[1]);
        main_rate_rate.setText(PreferenceManager.getInstance(this).getCurrency());

        text = new ArrayList<String>();
        sounds = new ArrayList<Integer>();


    }

    private void initView() {


        main_restaurant_imageview = (ImageView) findViewById(R.id.main_restaurant_imageview);
        main_sight_imageview = (ImageView) findViewById(R.id.main_sight_imageview);
        main_magazine_imageview = (ImageView) findViewById(R.id.main_magazine_imageview);
        main_map_imageview = (ImageView) findViewById(R.id.main_map_imageview);
        main_rate_imageview = (ImageView) findViewById(R.id.main_rate_imageview);
        main_voice_imageview = (ImageView) findViewById(R.id.main_voice_imageview);
        main_talk_imageview = (ImageView) findViewById(R.id.main_talk_imageview);
        main_translate_imageview = (ImageView) findViewById(R.id.main_translate_imageview);

        main_recommend01 = (ImageView) findViewById(R.id.main_recommend01);
        main_recommend02 = (ImageView) findViewById(R.id.main_recommend02);
        main_recommend03 = (ImageView) findViewById(R.id.main_recommend03);
        main_recommend04 = (ImageView) findViewById(R.id.main_recommend04);


        main_banner01 = (ImageView) findViewById(R.id.main_banner01);
        main_banner02 = (ImageView) findViewById(R.id.main_banner02);
        main_today = (ImageView) findViewById(R.id.main_today);
        banner_samjin = (ImageView) findViewById(R.id.banner_samjin);


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

        main_voice_listview = (ListView) findViewById(R.id.main_voice_listview);

        localSpinner = (Spinner) findViewById(R.id.actionbar_local_spinner);

        findViewById(R.id.main_voice_more).setOnClickListener(this);


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
                    intentURL = "tndn://getStoreList?id=32";
                    PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                    PreferenceManager.getInstance(getApplicationContext()).setFoodId("");


                } else if (PreferenceManager.getInstance(this).getLocalization().equals("3")) {
                    //서울
                    intentURL = "tndn://getStoreList?id=36";
                    PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                    PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                } else if (PreferenceManager.getInstance(this).getLocalization().equals("5")) {
                    //부산
                    intentURL = "tndn://getStoreList?id=40";
                    PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                    PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                } else {
                    //제주
                    intentURL = "tndn://getStoreList?id=20";
                    PreferenceManager.getInstance(this).setLocalization("1");
                    PreferenceManager.getInstance(getApplicationContext()).setLocationId("");
                    PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                }
                PreferenceManager.getInstance(this).setUserfrom("31");
                new LogHome().send(getApplicationContext(), "icon-getStoreList");
                break;
            case R.id.main_sight_imageview:
                if (PreferenceManager.getInstance(this).getLocalization().equals("2")) {
                    //수원
                    intentURL = "tndn://attractionList?id=32";


                } else if (PreferenceManager.getInstance(this).getLocalization().equals("3")) {
                    //서울
                    intentURL = "tndn://attractionList?id=36";

                } else {
                    //제주
                    intentURL = "tndn://attractionList?id=29";
                }
                PreferenceManager.getInstance(this).setUserfrom("31");
                new LogHome().send(getApplicationContext(), "icon-getSites");
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
            case R.id.main_talk_imageview:
                intentURL = "tndn://talk";
                new LogHome().send(getApplicationContext(), "icon-talk");
                break;
            case R.id.main_translate_imageview:
                intentURL = "tndn://cs";
                new LogHome().send(getApplicationContext(), "icon-cs");
                break;
            case R.id.main_recommend01:
                intentURL = "tndn://getFood?id=6860&name=Ganse客厅";
                PreferenceManager.getInstance(this).setUserfrom("34");
                new LogHome().send(getApplicationContext(), "image-ganse");
                break;
            case R.id.main_recommend02:
                intentURL = "tndn://getFood?id=6605&name=Nilmori Dong Dong";
                PreferenceManager.getInstance(this).setUserfrom("34");
                new LogHome().send(getApplicationContext(), "image-nilmori");
                break;

            case R.id.main_recommend03:
                intentURL = "tndn://getFood?id=6666&name=Donpas";
                PreferenceManager.getInstance(this).setUserfrom("34");
                new LogHome().send(getApplicationContext(), "image-donpas");
                break;
            case R.id.main_recommend04:
                intentURL = "tndn://getFood?id=3513&name=Beoltae";
                PreferenceManager.getInstance(this).setUserfrom("34");
                new LogHome().send(getApplicationContext(), "image-beoltae");
                break;
            case R.id.main_today:
                intentURL = "tndn://getFood?id=6860&name=Ganse客厅";
                PreferenceManager.getInstance(this).setUserfrom("34");
                new LogHome().send(getApplicationContext(), "banner-ganse");
                break;
            case R.id.banner_samjin:
                intentURL = "tndn://getFood?id=7086&name=Samjin鱼丸";
                PreferenceManager.getInstance(this).setUserfrom("40");
                new LogHome().send(getApplicationContext(), "banner-samjin");
                break;
            case R.id.main_category_cityhall:
                intentURL = "tndn://getStoreList?id=21";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("21");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-cityhall");
                break;
            case R.id.main_category_baozen:
                intentURL = "tndn://getStoreList?id=20";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("20");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-baozen");
                break;
            case R.id.main_category_joongangro:
                intentURL = "tndn://getStoreList?id=22";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("22");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                new LogHome().send(getApplicationContext(), "image-joongangro");
                break;
            case R.id.main_category_jejusi:
                intentURL = "tndn://getStoreList?id=26";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("26");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                new LogHome().send(getApplicationContext(), "image-jejusi");
                break;
            case R.id.main_category_aewol:
                intentURL = "tndn://getStoreList?id=23";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("23");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-aewol");

                break;
            case R.id.main_category_jejuwest:
                intentURL = "tndn://getStoreList?id=30";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("30");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                new LogHome().send(getApplicationContext(), "image-jejuwest");
                break;
            case R.id.main_category_waljeong:
                intentURL = "tndn://getStoreList?id=24";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("24");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-waljeong");

                break;
            case R.id.main_category_sungsan:
                intentURL = "tndn://getStoreList?id=25";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("25");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                new LogHome().send(getApplicationContext(), "image-sungsan");
                break;
            case R.id.main_category_jejueast:
                intentURL = "tndn://getStoreList?id=29";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("29");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-jejueast");

                break;
            case R.id.main_category_cheonjiyeon:
                intentURL = "tndn://getStoreList?id=34";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("34");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-cheonjiyeon");

                break;
            case R.id.main_category_joongmoon:
                intentURL = "tndn://getStoreList?id=27";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("27");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-joongmoon");

                break;
            case R.id.main_category_seoguipo:
                intentURL = "tndn://getStoreList?id=28";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("1");
                PreferenceManager.getInstance(this).setLocationId("28");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");
                new LogHome().send(getApplicationContext(), "image-seoguipo");
                break;
            case R.id.main_category_suwon:
                intentURL = "tndn://getStoreList?id=32";
                PreferenceManager.getInstance(this).setUserfrom("33");
                PreferenceManager.getInstance(this).setLocalization("2");
                PreferenceManager.getInstance(this).setLocationId("32");
                PreferenceManager.getInstance(getApplicationContext()).setFoodId("");

                new LogHome().send(getApplicationContext(), "image-suwon");
                break;
            case R.id.main_category_seoul:
                intentURL = "tndn://getStoreList?id=36";
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

            case R.id.main_voice_more:
                intentURL = "tndn://voice";
                new LogHome().send(getApplicationContext(), "banner-voice");
                break;

        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentURL));
        startActivity(intent);
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
}
