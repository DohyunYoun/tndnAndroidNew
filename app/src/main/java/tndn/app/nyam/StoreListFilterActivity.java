package tndn.app.nyam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tndn.app.nyam.utils.PreferenceManager;

public class StoreListFilterActivity extends AppCompatActivity implements View.OnClickListener {


    /**
     * View
     */
    private LinearLayout store_list_filter_location_linearlayout;

    private ImageView store_list_filter_cityhall;
    private ImageView store_list_filter_baozen;
    private ImageView store_list_filter_joongangro;
    private ImageView store_list_filter_jejusi;
    private ImageView store_list_filter_aewol_hyeobjae_beach;
    private ImageView store_list_filter_jejuwest;
    private ImageView store_list_filter_waljeong;
    private ImageView store_list_filter_sungsan;
    private ImageView store_list_filter_jejueast;
    private ImageView store_list_filter_cheonjiyeon;
    private ImageView store_list_filter_joongmoon;
    private ImageView store_list_filter_seoguipo;


    private ImageView store_list_filter_korean;
    private ImageView store_list_filter_chinese;
    private ImageView store_list_filter_japanese;
    private ImageView store_list_filter_western;
    private ImageView store_list_filter_snack;
    private ImageView store_list_filter_cafe;
    private ImageView store_list_filter_seafood;
    private ImageView store_list_filter_chicken;
    private ImageView store_list_filter_pub;
    private ImageView store_list_filter_fastfood;
    private ImageView store_list_filter_pizza;
    private ImageView store_list_filter_distribution;
    private ImageView store_list_filter_bakery;
    private ImageView store_list_filter_dessert;
    private ImageView store_list_filter_asiafusion;
    private ImageView store_list_filter_etc;

    private ImageView store_list_filter_apply;
    private ImageView store_list_filter_view_map;
    private RelativeLayout category_viewmap;
    private ImageView category_view_map_jeju;
    private ImageView category_view_map_suwon;
    private ImageView category_view_map_seoul;
    private ImageView category_view_map_busan;

    private LinearLayout store_list_filter_seoul_location_linearlayout;

    private ImageView store_list_filter_gangnam;
    private ImageView store_list_filter_itaewon;
    private ImageView store_list_filter_jongno;
    private ImageView store_list_filter_wangsimni;


    private LinearLayout store_list_filter_suwon_location_linearlayout;

    private ImageView store_list_filter_suwon;


    private LinearLayout store_list_filter_busan_location_linearlayout;

    private ImageView store_list_filter_busan;

    /**
     * value
     */
    int[] locationIds;
    int[] foodIds;
    String locationId;
    String foodId;
    private String mainId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list_filter);
/********************************************
 for actionbar
 ********************************************/

        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);
        Button close = (Button) findViewById(R.id.actionbar_close_button);
        ImageView init = (ImageView) findViewById(R.id.actionbar_filter_initialization);

        actionbar_text.setText(getResources().getString(R.string.app_name));
        back.setVisibility(View.GONE);
        close.setVisibility(View.VISIBLE);
        init.setVisibility(View.VISIBLE);
        init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSelected();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
/********************************************
 for actionbar
 ********************************************/


        initialize();
        initView();


        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            mainId = uri.getQueryParameter("mainId");
            if (mainId == null || mainId.equals("") || mainId.equals("undefined") || mainId.equals("null"))
                mainId = "1";

        } else {
            mainId = "1";
        }

        if (mainId.equals("1")) {
            findViewById(R.id.sotre_list_filter_food_ll).setVisibility(View.VISIBLE);
            findViewById(R.id.store_list_filter_food).setVisibility(View.VISIBLE);
        }


        /**
         * 지역 선택에 따른 레이아웃 구분
         */
        if (PreferenceManager.getInstance(this).getLocalization().equals("1")) {
            store_list_filter_location_linearlayout.setVisibility(View.VISIBLE);

        } else if (PreferenceManager.getInstance(this).getLocalization().equals("2")) {
            store_list_filter_location_linearlayout.setVisibility(View.GONE);
            store_list_filter_suwon_location_linearlayout.setVisibility(View.VISIBLE);
            store_list_filter_seoul_location_linearlayout.setVisibility(View.GONE);
            store_list_filter_busan_location_linearlayout.setVisibility(View.GONE);
            store_list_filter_suwon.setSelected(true);
            locationIds[32] = 1;

        } else if (PreferenceManager.getInstance(this).getLocalization().equals("3")) {
            store_list_filter_location_linearlayout.setVisibility(View.GONE);
            store_list_filter_suwon_location_linearlayout.setVisibility(View.GONE);
            store_list_filter_seoul_location_linearlayout.setVisibility(View.VISIBLE);
            store_list_filter_busan_location_linearlayout.setVisibility(View.GONE);

        } else if (PreferenceManager.getInstance(this).getLocalization().equals("5")) {
            store_list_filter_location_linearlayout.setVisibility(View.GONE);
            store_list_filter_suwon_location_linearlayout.setVisibility(View.GONE);
            store_list_filter_seoul_location_linearlayout.setVisibility(View.GONE);
            store_list_filter_busan_location_linearlayout.setVisibility(View.VISIBLE);

            store_list_filter_busan.setSelected(true);
            locationIds[40] = 1;
        } else {
            store_list_filter_location_linearlayout.setVisibility(View.VISIBLE);

        }


        //지도보기
        store_list_filter_view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_viewmap.setVisibility(View.VISIBLE);
                if (PreferenceManager.getInstance(getApplicationContext()).getLocalization().equals("1")) {
                    //제주
                    category_view_map_jeju.setVisibility(View.VISIBLE);
                    category_view_map_suwon.setVisibility(View.GONE);
                    category_view_map_seoul.setVisibility(View.GONE);
                    category_view_map_busan.setVisibility(View.GONE);

                } else if (PreferenceManager.getInstance(getApplicationContext()).getLocalization().equals("2")) {
                    //수원
                    category_view_map_jeju.setVisibility(View.GONE);
                    category_view_map_suwon.setVisibility(View.VISIBLE);
                    category_view_map_seoul.setVisibility(View.GONE);
                    category_view_map_busan.setVisibility(View.GONE);

                } else if (PreferenceManager.getInstance(getApplicationContext()).getLocalization().equals("3")) {
                    //서울
                    category_view_map_jeju.setVisibility(View.GONE);
                    category_view_map_suwon.setVisibility(View.GONE);
                    category_view_map_seoul.setVisibility(View.VISIBLE);
                    category_view_map_busan.setVisibility(View.GONE);

                } else if (PreferenceManager.getInstance(getApplicationContext()).getLocalization().equals("5")) {
                    //부산
                    category_view_map_jeju.setVisibility(View.GONE);
                    category_view_map_suwon.setVisibility(View.GONE);
                    category_view_map_seoul.setVisibility(View.GONE);
                    category_view_map_busan.setVisibility(View.VISIBLE);

                } else {
                    //에러
                    category_view_map_jeju.setVisibility(View.VISIBLE);
                    category_view_map_suwon.setVisibility(View.GONE);
                    category_view_map_seoul.setVisibility(View.GONE);
                    category_view_map_busan.setVisibility(View.GONE);
                }

            }
        });

        category_viewmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_viewmap.setVisibility(View.INVISIBLE);
            }
        });


        /**
         * Preference에서 선택되었던 값들 가져와서 뿌려주기
         * 배열도 저장해놓기
         */


        //about location
        if (!PreferenceManager.getInstance(this).getLocationId().equals("")) {
            String[] str_locationId = PreferenceManager.getInstance(this).getLocationId().split(",");


            for (int i = 0; i < str_locationId.length; i++) {
                locationIds[Integer.parseInt(str_locationId[i])] = 1;

                switch (Integer.parseInt(str_locationId[i])) {
                    case 20:
                        store_list_filter_baozen.setSelected(true);
                        break;
                    case 21:
                        store_list_filter_cityhall.setSelected(true);
                        break;
                    case 22:
                        store_list_filter_joongangro.setSelected(true);
                        break;
                    case 26:
                        store_list_filter_jejusi.setSelected(true);
                        break;
                    case 23:
                        store_list_filter_aewol_hyeobjae_beach.setSelected(true);
                        break;
                    case 30:
                        store_list_filter_jejuwest.setSelected(true);
                        break;
                    case 24:
                        store_list_filter_waljeong.setSelected(true);
                        break;
                    case 25:
                        store_list_filter_sungsan.setSelected(true);
                        break;
                    case 29:
                        store_list_filter_jejueast.setSelected(true);
                        break;
                    case 34:
                        store_list_filter_cheonjiyeon.setSelected(true);
                        break;
                    case 27:
                        store_list_filter_joongmoon.setSelected(true);
                        break;
                    case 28:
                        store_list_filter_seoguipo.setSelected(true);
                        break;
                    case 32:
                        //수원
                        store_list_filter_suwon.setSelected(true);
                        break;
                    case 36:
                        store_list_filter_jongno.setSelected(true);
                        break;
                    case 37:
                        store_list_filter_gangnam.setSelected(true);
                        break;
                    case 38:
                        store_list_filter_wangsimni.setSelected(true);
                        break;
                    case 39:
                        store_list_filter_itaewon.setSelected(true);
                        break;
                    case 40:
                        //부산
                        store_list_filter_busan.setSelected(true);
                        break;
                }
            }
        }

        if (!PreferenceManager.getInstance(this).getFoodId().equals("")) {
            String[] str_foodId = PreferenceManager.getInstance(this).getFoodId().split(",");

            for (int i = 0; i < str_foodId.length; i++) {
                foodIds[Integer.parseInt(str_foodId[i])] = 1;
                switch (Integer.parseInt(str_foodId[i])) {
                    case 13:
                    case 31:
                        store_list_filter_korean.setSelected(true);
                        break;
                    case 9:
                    case 32:
                        store_list_filter_chinese.setSelected(true);
                        break;
                    case 8:
                    case 33:
                        store_list_filter_japanese.setSelected(true);
                        break;
                    case 6:
                    case 34:
                        store_list_filter_western.setSelected(true);
                        break;
                    case 3:
                    case 40:
                        store_list_filter_snack.setSelected(true);
                        break;
                    case 10:
                    case 44:
                        store_list_filter_cafe.setSelected(true);
                        break;
                    case 14:
                    case 39:
                        store_list_filter_seafood.setSelected(true);
                        break;
                    case 15:
                    case 37:
                        store_list_filter_chicken.setSelected(true);
                        break;
                    case 1:
                    case 35:
                        store_list_filter_pub.setSelected(true);
                        break;
                    case 12:
                    case 36:
                        store_list_filter_fastfood.setSelected(true);
                        break;
                    case 4:
                    case 38:
                        store_list_filter_pizza.setSelected(true);
                        break;
                    case 5:
                        store_list_filter_distribution.setSelected(true);
                        break;
                    case 7:
                    case 45:
                        store_list_filter_bakery.setSelected(true);
                        break;
                    case 11:
                    case 46:
                        store_list_filter_dessert.setSelected(true);
                        break;
                    case 2:
                    case 42:
                        store_list_filter_asiafusion.setSelected(true);
                        break;
                    case 16:
                    case 43:
                        store_list_filter_etc.setSelected(true);
                        break;
                }


            }
        }

        /**
         * click
         */
        store_list_filter_cityhall.setOnClickListener(this);
        store_list_filter_baozen.setOnClickListener(this);
        store_list_filter_joongangro.setOnClickListener(this);
        store_list_filter_jejusi.setOnClickListener(this);
        store_list_filter_aewol_hyeobjae_beach.setOnClickListener(this);
        store_list_filter_jejuwest.setOnClickListener(this);
        store_list_filter_waljeong.setOnClickListener(this);
        store_list_filter_sungsan.setOnClickListener(this);
        store_list_filter_jejueast.setOnClickListener(this);
        store_list_filter_cheonjiyeon.setOnClickListener(this);
        store_list_filter_joongmoon.setOnClickListener(this);
        store_list_filter_seoguipo.setOnClickListener(this);
        store_list_filter_korean.setOnClickListener(this);
        store_list_filter_chinese.setOnClickListener(this);
        store_list_filter_japanese.setOnClickListener(this);
        store_list_filter_western.setOnClickListener(this);
        store_list_filter_snack.setOnClickListener(this);
        store_list_filter_cafe.setOnClickListener(this);
        store_list_filter_seafood.setOnClickListener(this);
        store_list_filter_chicken.setOnClickListener(this);
        store_list_filter_pub.setOnClickListener(this);
        store_list_filter_fastfood.setOnClickListener(this);
        store_list_filter_pizza.setOnClickListener(this);
        store_list_filter_distribution.setOnClickListener(this);
        store_list_filter_bakery.setOnClickListener(this);
        store_list_filter_dessert.setOnClickListener(this);
        store_list_filter_asiafusion.setOnClickListener(this);
        store_list_filter_etc.setOnClickListener(this);
        store_list_filter_apply.setOnClickListener(this);

        store_list_filter_gangnam.setOnClickListener(this);
        store_list_filter_itaewon.setOnClickListener(this);
        store_list_filter_jongno.setOnClickListener(this);
        store_list_filter_wangsimni.setOnClickListener(this);

        store_list_filter_suwon.setOnClickListener(this);
        store_list_filter_busan.setOnClickListener(this);

    }

    private void initialize() {


        locationIds = new int[41];
        foodIds = new int[47];

        locationId = "";
        foodId = "";
    }

    private void initView() {

        store_list_filter_location_linearlayout = (LinearLayout) findViewById(R.id.store_list_filter_location_linearlayout);

        store_list_filter_cityhall = (ImageView) findViewById(R.id.store_list_filter_cityhall);
        store_list_filter_baozen = (ImageView) findViewById(R.id.store_list_filter_baozen);
        store_list_filter_joongangro = (ImageView) findViewById(R.id.store_list_filter_joongangro);
        store_list_filter_jejusi = (ImageView) findViewById(R.id.store_list_filter_jejusi);
        store_list_filter_aewol_hyeobjae_beach = (ImageView) findViewById(R.id.store_list_filter_aewol_hyeobjae_beach);
        store_list_filter_jejuwest = (ImageView) findViewById(R.id.store_list_filter_jejuwest);
        store_list_filter_waljeong = (ImageView) findViewById(R.id.store_list_filter_waljeong);
        store_list_filter_sungsan = (ImageView) findViewById(R.id.store_list_filter_sungsan);
        store_list_filter_jejueast = (ImageView) findViewById(R.id.store_list_filter_jejueast);
        store_list_filter_cheonjiyeon = (ImageView) findViewById(R.id.store_list_filter_cheonjiyeon);
        store_list_filter_joongmoon = (ImageView) findViewById(R.id.store_list_filter_joongmoon);
        store_list_filter_seoguipo = (ImageView) findViewById(R.id.store_list_filter_seoguipo);

        store_list_filter_korean = (ImageView) findViewById(R.id.store_list_filter_korean);
        store_list_filter_chinese = (ImageView) findViewById(R.id.store_list_filter_chinese);
        store_list_filter_japanese = (ImageView) findViewById(R.id.store_list_filter_japanese);
        store_list_filter_western = (ImageView) findViewById(R.id.store_list_filter_western);
        store_list_filter_snack = (ImageView) findViewById(R.id.store_list_filter_snack);
        store_list_filter_cafe = (ImageView) findViewById(R.id.store_list_filter_cafe);
        store_list_filter_seafood = (ImageView) findViewById(R.id.store_list_filter_seafood);
        store_list_filter_chicken = (ImageView) findViewById(R.id.store_list_filter_chicken);
        store_list_filter_pub = (ImageView) findViewById(R.id.store_list_filter_pub);
        store_list_filter_fastfood = (ImageView) findViewById(R.id.store_list_filter_fastfood);
        store_list_filter_pizza = (ImageView) findViewById(R.id.store_list_filter_pizza);
        store_list_filter_distribution = (ImageView) findViewById(R.id.store_list_filter_distribution);
        store_list_filter_bakery = (ImageView) findViewById(R.id.store_list_filter_bakery);
        store_list_filter_dessert = (ImageView) findViewById(R.id.store_list_filter_dessert);
        store_list_filter_asiafusion = (ImageView) findViewById(R.id.store_list_filter_asiafusion);
        store_list_filter_etc = (ImageView) findViewById(R.id.store_list_filter_etc);

        store_list_filter_apply = (ImageView) findViewById(R.id.store_list_filter_apply);

        store_list_filter_view_map = (ImageView) findViewById(R.id.store_list_filter_view_map);
        category_viewmap = (RelativeLayout) findViewById(R.id.store_list_view_map_ll);
        category_view_map_jeju = (ImageView) findViewById(R.id.store_list_view_map_jeju);
        category_view_map_suwon = (ImageView) findViewById(R.id.store_list_view_map_suwon);
        category_view_map_seoul = (ImageView) findViewById(R.id.store_list_view_map_seoul);
        category_view_map_busan = (ImageView) findViewById(R.id.store_list_view_map_busan);


        store_list_filter_seoul_location_linearlayout = (LinearLayout) findViewById(R.id.store_list_filter_seoul_location_linearlayout);

        store_list_filter_gangnam = (ImageView) findViewById(R.id.store_list_filter_gangnam);
        store_list_filter_itaewon = (ImageView) findViewById(R.id.store_list_filter_itaewon);
        store_list_filter_jongno = (ImageView) findViewById(R.id.store_list_filter_jongno);
        store_list_filter_wangsimni = (ImageView) findViewById(R.id.store_list_filter_wangsimni);

        store_list_filter_suwon_location_linearlayout = (LinearLayout) findViewById(R.id.store_list_filter_suwon_location_linearlayout);
        store_list_filter_suwon = (ImageView) findViewById(R.id.store_list_filter_suwon);

        store_list_filter_busan_location_linearlayout = (LinearLayout) findViewById(R.id.store_list_filter_busan_location_linearlayout);
        store_list_filter_busan = (ImageView) findViewById(R.id.store_list_filter_busan);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.store_list_filter_cityhall:
                if (store_list_filter_cityhall.isSelected()) {
                    store_list_filter_cityhall.setSelected(false);
                    locationIds[21] = 0;
                } else {
                    store_list_filter_cityhall.setSelected(true);
                    locationIds[21] = 1;
                }
                break;
            case R.id.store_list_filter_baozen:
                if (store_list_filter_baozen.isSelected()) {
                    store_list_filter_baozen.setSelected(false);
                    locationIds[20] = 0;
                } else {
                    store_list_filter_baozen.setSelected(true);
                    locationIds[20] = 1;
                }
                break;
            case R.id.store_list_filter_joongangro:
                if (store_list_filter_joongangro.isSelected()) {
                    store_list_filter_joongangro.setSelected(false);
                    locationIds[22] = 0;

                } else {
                    store_list_filter_joongangro.setSelected(true);
                    locationIds[22] = 1;
                }
                break;
            case R.id.store_list_filter_jejusi:
                if (store_list_filter_jejusi.isSelected()) {
                    store_list_filter_jejusi.setSelected(false);
                    locationIds[26] = 0;
                } else {
                    store_list_filter_jejusi.setSelected(true);
                    locationIds[26] = 1;
                }
                break;
            case R.id.store_list_filter_aewol_hyeobjae_beach:
                if (store_list_filter_aewol_hyeobjae_beach.isSelected()) {
                    store_list_filter_aewol_hyeobjae_beach.setSelected(false);
                    locationIds[23] = 0;
                } else {
                    store_list_filter_aewol_hyeobjae_beach.setSelected(true);
                    locationIds[23] = 1;
                }
                break;
            case R.id.store_list_filter_jejuwest:
                if (store_list_filter_jejuwest.isSelected()) {
                    store_list_filter_jejuwest.setSelected(false);
                    locationIds[30] = 0;
                } else {
                    store_list_filter_jejuwest.setSelected(true);
                    locationIds[30] = 1;
                }
                break;
            case R.id.store_list_filter_waljeong:
                if (store_list_filter_waljeong.isSelected()) {
                    store_list_filter_waljeong.setSelected(false);
                    locationIds[24] = 0;
                } else {
                    store_list_filter_waljeong.setSelected(true);
                    locationIds[24] = 1;
                }
                break;
            case R.id.store_list_filter_sungsan:
                if (store_list_filter_sungsan.isSelected()) {
                    store_list_filter_sungsan.setSelected(false);
                    locationIds[25] = 0;
                } else {
                    store_list_filter_sungsan.setSelected(true);
                    locationIds[25] = 1;
                }
                break;
            case R.id.store_list_filter_jejueast:
                if (store_list_filter_jejueast.isSelected()) {
                    store_list_filter_jejueast.setSelected(false);
                    locationIds[29] = 0;
                } else {
                    store_list_filter_jejueast.setSelected(true);
                    locationIds[29] = 1;
                }
                break;
            case R.id.store_list_filter_cheonjiyeon:
                if (store_list_filter_cheonjiyeon.isSelected()) {
                    store_list_filter_cheonjiyeon.setSelected(false);
                    locationIds[34] = 0;
                } else {
                    store_list_filter_cheonjiyeon.setSelected(true);
                    locationIds[34] = 1;
                }
                break;
            case R.id.store_list_filter_joongmoon:
                if (store_list_filter_joongmoon.isSelected()) {
                    store_list_filter_joongmoon.setSelected(false);
                    locationIds[27] = 0;
                } else {
                    store_list_filter_joongmoon.setSelected(true);
                    locationIds[27] = 1;
                }
                break;
            case R.id.store_list_filter_seoguipo:
                if (store_list_filter_seoguipo.isSelected()) {
                    store_list_filter_seoguipo.setSelected(false);
                    locationIds[28] = 0;
                } else {
                    store_list_filter_seoguipo.setSelected(true);
                    locationIds[28] = 1;
                }
                break;

            case R.id.store_list_filter_suwon:
                //수원
//                if (store_list_filter_suwon.isSelected()) {
//                    store_list_filter_suwon.setSelected(false);
//                    locationIds[32] = 0;
//                } else {
//                    store_list_filter_suwon.setSelected(true);
//                    locationIds[32] = 1;
//                }
                break;

            case R.id.store_list_filter_jongno:
                if (store_list_filter_jongno.isSelected()) {
                    store_list_filter_jongno.setSelected(false);
                    locationIds[36] = 0;
                } else {
                    store_list_filter_jongno.setSelected(true);
                    locationIds[36] = 1;
                }
                break;
            case R.id.store_list_filter_gangnam:
                if (store_list_filter_gangnam.isSelected()) {
                    store_list_filter_gangnam.setSelected(false);
                    locationIds[37] = 0;
                } else {
                    store_list_filter_gangnam.setSelected(true);
                    locationIds[37] = 1;
                }
                break;
            case R.id.store_list_filter_wangsimni:
                if (store_list_filter_wangsimni.isSelected()) {
                    store_list_filter_wangsimni.setSelected(false);
                    locationIds[38] = 0;
                } else {
                    store_list_filter_wangsimni.setSelected(true);
                    locationIds[38] = 1;
                }
                break;
            case R.id.store_list_filter_itaewon:
                if (store_list_filter_itaewon.isSelected()) {
                    store_list_filter_itaewon.setSelected(false);
                    locationIds[39] = 0;
                } else {
                    store_list_filter_itaewon.setSelected(true);
                    locationIds[39] = 1;
                }
                break;
            case R.id.store_list_filter_busan:
                //부산
//                if (store_list_filter_busan.isSelected()) {
//                    store_list_filter_busan.setSelected(false);
//                    locationIds[40] = 0;
//                } else {
//                    store_list_filter_busan.setSelected(true);
//                    locationIds[40] = 1;
//                }
                break;

            case R.id.store_list_filter_korean:
                if (store_list_filter_korean.isSelected()) {
                    store_list_filter_korean.setSelected(false);
                    foodIds[31] = 0;
                } else {
                    store_list_filter_korean.setSelected(true);
                    foodIds[31] = 1;
                }
                break;
            case R.id.store_list_filter_chinese:
                if (store_list_filter_chinese.isSelected()) {
                    store_list_filter_chinese.setSelected(false);
                    foodIds[32] = 0;
                } else {
                    store_list_filter_chinese.setSelected(true);
                    foodIds[32] = 1;
                }
                break;
            case R.id.store_list_filter_japanese:
                if (store_list_filter_japanese.isSelected()) {
                    store_list_filter_japanese.setSelected(false);
                    foodIds[33] = 0;
                } else {
                    store_list_filter_japanese.setSelected(true);
                    foodIds[33] = 1;
                }
                break;
            case R.id.store_list_filter_western:
                if (store_list_filter_western.isSelected()) {
                    store_list_filter_western.setSelected(false);
                    foodIds[34] = 0;
                } else {
                    store_list_filter_western.setSelected(true);
                    foodIds[34] = 1;
                }
                break;
            case R.id.store_list_filter_snack:
                if (store_list_filter_snack.isSelected()) {
                    store_list_filter_snack.setSelected(false);
                    foodIds[40] = 0;
                } else {
                    store_list_filter_snack.setSelected(true);
                    foodIds[40] = 1;
                }
                break;
            case R.id.store_list_filter_cafe:
                if (store_list_filter_cafe.isSelected()) {
                    store_list_filter_cafe.setSelected(false);
                    foodIds[44] = 0;
                } else {
                    store_list_filter_cafe.setSelected(true);
                    foodIds[44] = 1;
                }
                break;
            case R.id.store_list_filter_seafood:
                if (store_list_filter_seafood.isSelected()) {
                    store_list_filter_seafood.setSelected(false);
                    foodIds[39] = 0;
                } else {
                    store_list_filter_seafood.setSelected(true);
                    foodIds[39] = 1;
                }
                break;
            case R.id.store_list_filter_chicken:
                if (store_list_filter_chicken.isSelected()) {
                    store_list_filter_chicken.setSelected(false);
                    foodIds[37] = 0;
                } else {
                    store_list_filter_chicken.setSelected(true);
                    foodIds[37] = 1;
                }
                break;
            case R.id.store_list_filter_pub:
                if (store_list_filter_pub.isSelected()) {
                    store_list_filter_pub.setSelected(false);
                    foodIds[35] = 0;
                } else {
                    store_list_filter_pub.setSelected(true);
                    foodIds[35] = 1;
                }
                break;
            case R.id.store_list_filter_fastfood:
                if (store_list_filter_fastfood.isSelected()) {
                    store_list_filter_fastfood.setSelected(false);
                    foodIds[36] = 0;
                } else {
                    store_list_filter_fastfood.setSelected(true);
                    foodIds[36] = 1;
                }
                break;
            case R.id.store_list_filter_pizza:
                if (store_list_filter_pizza.isSelected()) {
                    store_list_filter_pizza.setSelected(false);
                    foodIds[38] = 0;
                } else {
                    store_list_filter_pizza.setSelected(true);
                    foodIds[38] = 1;
                }
                break;
            case R.id.store_list_filter_distribution:
                if (store_list_filter_distribution.isSelected()) {
                    store_list_filter_distribution.setSelected(false);
                    foodIds[5] = 0;
                } else {
                    store_list_filter_distribution.setSelected(true);
                    foodIds[5] = 1;
                }
                break;
            case R.id.store_list_filter_bakery:
                if (store_list_filter_bakery.isSelected()) {
                    store_list_filter_bakery.setSelected(false);
                    foodIds[45] = 0;
                } else {
                    store_list_filter_bakery.setSelected(true);
                    foodIds[45] = 1;
                }
                break;
            case R.id.store_list_filter_dessert:
                if (store_list_filter_dessert.isSelected()) {
                    store_list_filter_dessert.setSelected(false);
                    foodIds[46] = 0;
                } else {
                    store_list_filter_dessert.setSelected(true);
                    foodIds[46] = 1;
                }
                break;
            case R.id.store_list_filter_asiafusion:
                if (store_list_filter_asiafusion.isSelected()) {
                    store_list_filter_asiafusion.setSelected(false);
                    foodIds[42] = 0;
                } else {
                    store_list_filter_asiafusion.setSelected(true);
                    foodIds[42] = 1;
                }
                break;
            case R.id.store_list_filter_etc:
                if (store_list_filter_etc.isSelected()) {
                    store_list_filter_etc.setSelected(false);
                    foodIds[43] = 0;
                } else {
                    store_list_filter_etc.setSelected(true);
                    foodIds[43] = 1;
                }
                break;

            //apply
            case R.id.store_list_filter_apply:
                boolean location_tmp = true;
                for (int i = 0; i < locationIds.length; i++) {
                    //1는 선택된것
                    //0는 선택 안된것
                    if (locationIds[i] == 1) {
                        if (location_tmp) {
                            location_tmp = false;
                            //초기값 관리, 콤마때문
                            locationId = i + "";
                        } else {
                            locationId = locationId + "," + i;
                        }
                    }
                }

                boolean food_tmp = true;
                for (int i = 0; i < foodIds.length; i++) {
                    //1는 선택된것
                    //0는 선택 안된것
                    if (foodIds[i] == 1) {
                        if (food_tmp) {
                            food_tmp = false;
                            //초기값 관리, 콤마때문
                            foodId = i + "";
                        } else {
                            foodId = foodId + "," + i;
                        }
                    }
                }

                PreferenceManager.getInstance(getApplicationContext()).setLocationId(locationId);
                PreferenceManager.getInstance(getApplicationContext()).setFoodId(foodId);
                String url = "tndn://getStoreList?mainId=" + mainId;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                /**
                 * Activity 종료
                 */
                StoreListActivity storeListActivity = (StoreListActivity) StoreListActivity.storeListActivity;
                storeListActivity.finish();
                finish();

                break;
        }

    }

    private void initSelected() {

        locationIds = new int[41];
        foodIds = new int[47];

        store_list_filter_cityhall.setSelected(false);
        store_list_filter_baozen.setSelected(false);
        store_list_filter_joongangro.setSelected(false);
        store_list_filter_jejusi.setSelected(false);
        store_list_filter_aewol_hyeobjae_beach.setSelected(false);
        store_list_filter_jejuwest.setSelected(false);
        store_list_filter_waljeong.setSelected(false);
        store_list_filter_sungsan.setSelected(false);
        store_list_filter_jejueast.setSelected(false);
        store_list_filter_cheonjiyeon.setSelected(false);
        store_list_filter_joongmoon.setSelected(false);
        store_list_filter_seoguipo.setSelected(false);

        store_list_filter_jongno.setSelected(false);
        store_list_filter_gangnam.setSelected(false);
        store_list_filter_wangsimni.setSelected(false);
        store_list_filter_itaewon.setSelected(false);

        store_list_filter_korean.setSelected(false);
        store_list_filter_chinese.setSelected(false);
        store_list_filter_japanese.setSelected(false);
        store_list_filter_western.setSelected(false);
        store_list_filter_snack.setSelected(false);
        store_list_filter_cafe.setSelected(false);
        store_list_filter_seafood.setSelected(false);
        store_list_filter_chicken.setSelected(false);
        store_list_filter_pub.setSelected(false);
        store_list_filter_fastfood.setSelected(false);
        store_list_filter_pizza.setSelected(false);
        store_list_filter_distribution.setSelected(false);
        store_list_filter_bakery.setSelected(false);
        store_list_filter_dessert.setSelected(false);
        store_list_filter_asiafusion.setSelected(false);
        store_list_filter_etc.setSelected(false);
    }

}

