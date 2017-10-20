package tndn.app.nyam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import tndn.app.nyam.adapter.FoodOrderAdapter;
import tndn.app.nyam.alipay.PayDemoActivity;
import tndn.app.nyam.data.StoreInfoData;
import tndn.app.nyam.data.StoreMenuData;
import tndn.app.nyam.utils.MakePricePretty;
import tndn.app.nyam.utils.OutTradeNo;
import tndn.app.nyam.utils.PreferenceManager;

public class FoodOrderAcitivty extends AppCompatActivity {

    private TextView actionbar_text;
    private Button back;

    private ProgressDialog pDialog;
    private ArrayList<StoreMenuData> order;
    private FoodOrderAdapter mAdapter;

    private ListView lv_order;

    private TextView res_order_total_kor;
    private TextView res_order_total_chn;
    private Button res_order_pay_button;
    private int totalprice = 0;

    private String params_menucount = "";
    private String params_idx_restaurantMenu = "";
    private String params_menuKOR = "";
    private String params_menuPrice = "";
    private String params_data = "";

    double curr;
    double sale = 0;
    String priceKor;
    String priceChn;
    String priceKorSale;
    String priceChnSale;

    String name;

    private StoreInfoData shop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order);

        Intent i = getIntent();
        Gson gson = new Gson();
        String jsonText = PreferenceManager.getInstance(getApplicationContext()).getOrderdata();
        order = gson.fromJson(jsonText, new TypeToken<ArrayList<StoreMenuData>>() {
        }.getType());

        name = i.getStringExtra("NAME");

        initView();
        initialize();


        lv_order.setAdapter(mAdapter);


        for (int j = 0; j < order.size(); j++) {
            totalprice = totalprice + order.get(j).getCount() * Integer.parseInt(order.get(j).getMenu_price());
            params_menucount = params_menucount + '#' + order.get(j).getCount();
            params_idx_restaurantMenu = params_idx_restaurantMenu + '#' + order.get(j).getId();
            params_menuKOR = params_menuKOR + '#' + order.get(j).getMenu_name_kor();
            params_menuPrice = params_menuPrice + '#' + order.get(j).getMenu_price();
            if (j == 0) {
                //첫번째 데이터
                params_data = order.get(j).getMenu_name_kor() + "#" + order.get(j).getMenu_price() + "#" + order.get(j).getCount();

            } else {
                params_data = params_data + "@" + order.get(j).getMenu_name_kor() + "#" + order.get(j).getMenu_price() + "#" + order.get(j).getCount();
            }
        }
        res_order_total_kor.setText(new MakePricePretty().makePricePretty(this, totalprice + "", true));
        res_order_total_chn.setText(new MakePricePretty().makePricePretty(this, totalprice + "", false));


        actionbar_text.setText(getResources().getString(R.string.order));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        res_order_pay_button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        //1. alipay
//                                                        PayDemoActivity pay = new PayDemoActivity();
//                                                        pay.init(getApplicationContext(), FoodOrderAcitivty.this);
//                                                        String outTradeNo = new OutTradeNo().getOutTradeNo();
//                                                        pay.pay(6, "tndn", "tndn", "TNDN Inc./+827086709409", "15000", outTradeNo);
                                                        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(FoodOrderAcitivty.this);
                                                        alert_confirm.setMessage("这家店还没加如甜点，请使用其他方式支付。sorry~").setCancelable(false).setPositiveButton(getResources().getString(R.string.btn_ok),
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });
                                                        AlertDialog alert = alert_confirm.create();
                                                        alert.show();

                                                    }
                                                }
        );

    }       //end oncreate

    private void initialize() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);
        mAdapter = new FoodOrderAdapter(this, order);


    }

    private void initView() {
        actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        res_order_total_kor = (TextView) findViewById(R.id.res_order_total_kor);
        res_order_total_chn = (TextView) findViewById(R.id.res_order_total_chn);

        lv_order = (ListView) findViewById(R.id.res_order_listview);
        res_order_total_kor = (TextView) findViewById(R.id.res_order_total_kor);
        res_order_total_chn = (TextView) findViewById(R.id.res_order_total_chn);
        res_order_pay_button = (Button) findViewById(R.id.res_order_pay_button);

        back = (Button) findViewById(R.id.actionbar_back_button);
    }

}
