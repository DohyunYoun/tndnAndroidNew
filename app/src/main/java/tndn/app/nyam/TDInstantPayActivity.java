package tndn.app.nyam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tndn.app.nyam.alipay.PayDemoActivity;
import tndn.app.nyam.data.StoreInfoData;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.CustomRequest;
import tndn.app.nyam.utils.GpsInfo;
import tndn.app.nyam.utils.IsOnline;
import tndn.app.nyam.utils.MakePaymentPrice;
import tndn.app.nyam.utils.MakePricePretty;
import tndn.app.nyam.utils.OutTradeNo;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.SaveExchangeRate;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;
import tndn.app.nyam.widget.ClearEditText;


public class TDInstantPayActivity extends AppCompatActivity {


    /**
     * View
     */
    private ClearEditText straight_pay_kor;
    private TextView straight_pay_chn;
    private TextView straight_pay_chn_web;
    private ImageView straight_pay_button;
    private ImageView img_logo;


    private StoreInfoData store;

    /**
     * value
     */
    double curr = 0;
    double sale = 0;
    String priceKor;
    String priceChn;
    String priceKorSale;
    String priceChnSale;
    String data = "";
    String outTradeNo = "";

    int price_kor;
    int price_sale_kor;
    private int id;
    String url;

    double latitude;
    double longitude;

    /**
     * utils
     */
    private GpsInfo gps;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td_instant_pay);
        initView();
        initialize();


        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri.getQueryParameter("id") == null || uri.getQueryParameter("id").equals("") || uri.getQueryParameter("id").equals("undefined") || uri.getQueryParameter("id").equals("null")) {
                showErrorAndFinish();
            } else {
                id = Integer.parseInt(uri.getQueryParameter("id"));
            }
        } else {
            showErrorAndFinish();
        }
        new SaveExchangeRate().saveExchangeRate(getApplicationContext());
        curr = Double.parseDouble(PreferenceManager.getInstance(this).getCurrency());


        getStoreInfo(id);


        straight_pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (straight_pay_kor.getText().toString().equals("") || straight_pay_kor.getText().toString().equals("0")) {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(TDInstantPayActivity.this);
                    alert_confirm.setMessage("금액을 입력해주세요").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();
                } else {
                    Map<String, String> params = new HashMap<>();
                    /**
                     *
                     *
                     * idxStore(*)
                     * nameStoreKor(*)
                     * nameStoreChn(*)
                     * data(*)
                     * currency(*)
                     * priceKor(*)
                     * priceChn(*)
                     * ouitTradeNo(*)
                     * payType(*)
                     *
                     * idxAppUser
                     * priceSaleKor
                     * priceSaleChn
                     * sale
                     *
                     * userLog
                     * os(*)
                     */

                    outTradeNo = new OutTradeNo().getOutTradeNo();

                    params.put("idxStore", id + "");
                    params.put("idxAppUser", PreferenceManager.getInstance(getApplicationContext()).getIdxAppUser());
                    params.put("nameStoreKor", store.getName_kor());
                    params.put("nameStoreChn", store.getName_chn());

                    data = "즉시결제#" + straight_pay_kor.getText().toString() + "#1";
                    params.put("data", data);
                    params.put("currency", curr + "");

                    priceKor = straight_pay_kor.getText().toString().replace(".0", "").replace(",", "");
                    priceChn = straight_pay_chn_web.getText().toString().replace("¥ ", "").replace(",", "");
                    // priceChnSale = straight_pay_chn.getText().toString().replace("¥ ", "").replace(",", "");

                    params.put("priceKor", priceKor);
                    params.put("priceChn", priceChn);
                    params.put("priceSaleKor", priceKorSale.replace(",", ""));
                    params.put("priceSaleChn", priceChnSale.replace(",", ""));
                    params.put("outTradeNo", outTradeNo);
                    params.put("payType", "alipay");

                    params.put("sale", (sale * 100 + "").replace(".0",""));
                    params.put("userIs", PreferenceManager.getInstance(getApplicationContext()).getUseris());
                    params.put("userFrom", PreferenceManager.getInstance(getApplicationContext()).getUserfrom());
                    params.put("os", "android");
                    params.put("userCode", PreferenceManager.getInstance(getApplicationContext()).getUsercode());

                    CustomRequest objreq = new CustomRequest(Request.Method.POST, new TDUrls().setStoreInstantOrder, params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                if (response.getString("result").equals("success")) {//if result failed

                                    //1. alipay
                                    PayDemoActivity pay = new PayDemoActivity();
                                    pay.init(getApplicationContext(), TDInstantPayActivity.this);
                                    pay.pay(id, store.getName_kor(), store.getName_chn(), "TNDN Inc./+827086709409", priceChnSale.replace(",", ""), outTradeNo);
                                    PreferenceManager.getInstance(TDInstantPayActivity.this).clear("USERFROM");
                                } else {

                                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(TDInstantPayActivity.this);
                                    alert_confirm.setMessage("本美食店跟甜点公司还没携手").setCancelable(false).setPositiveButton(getResources().getString(R.string.btn_ok),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    finish();
                                                }
                                            }).setNegativeButton(getResources().getString(R.string.btn_cancel),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // 'No'
                                                    return;
                                                }
                                            });
                                    AlertDialog alert = alert_confirm.create();
                                    alert.show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            hidepDialog();
                        }
                    });
                    AppController.getInstance().addToRequestQueue(objreq);

                }
            }
        });


        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void initView() {
        straight_pay_button = (ImageView) findViewById(R.id.straight_pay_button);
        straight_pay_kor = (ClearEditText) findViewById(R.id.straight_pay_kor);
        straight_pay_chn = (TextView) findViewById(R.id.straight_pay_chn);
        straight_pay_chn_web = (TextView) findViewById(R.id.straight_pay_chn_web);
        img_logo = (ImageView) findViewById(R.id.img_logo);
    }

    private void initialize() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);
        gps = new GpsInfo(this);

        sale = 0.05;
        price_kor = 0;
        price_sale_kor = 0;
        priceChnSale = "";
    }


    private void getStoreInfo(final int id) {

        if (!new IsOnline().onlineCheck(this)) {                  //internet check failed start
            Toast.makeText(this, "Internet Access Failed", Toast.LENGTH_SHORT).show();
            showErrorAndFinish();
        } else { //internet check success start
            showpDialog();
// GPS 사용유무 가져오기
            url = new TDUrls().getStoreInfoURL + "?id=" + id + new UserLog().getLog(getApplicationContext());
            JsonObjectRequest objreq = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject res) {
                    try {
                        if (res.getString("result").equals("failed")) {//if result failed

                            showErrorAndFinish();
                        } else {

                            store = new StoreInfoData();

                            /**
                             * FOR INFO
                             */
                            JSONObject info = res.getJSONArray("info").getJSONObject(0);
                            String tmpValue = "";

                            tmpValue = info.getString("id");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setId(Integer.parseInt(tmpValue));

                            tmpValue = info.getString("classify_kor");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setClassify_kor(tmpValue);

                            tmpValue = info.getString("classify_chn");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setClassify_chn(tmpValue);

                            tmpValue = info.getString("category_name_kor");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setCategory_name_kor(tmpValue);

                            tmpValue = info.getString("category_name_chn");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setCategory_name_chn(tmpValue);

                            tmpValue = info.getString("name_kor");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setName_kor(tmpValue);

                            tmpValue = info.getString("name_chn");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setName_chn(tmpValue);

                            tmpValue = info.getString("address_kor");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setAddress_kor(tmpValue);

                            tmpValue = info.getString("address_chn");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setAddress_chn(tmpValue);

                            tmpValue = info.getString("main_menu_kor");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setMain_menu_kor(tmpValue);

                            tmpValue = info.getString("main_menu_chn");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setMain_menu_chn(tmpValue);

                            tmpValue = info.getString("tel_1");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setTel_1(tmpValue);

                            tmpValue = info.getString("tel_2");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setTel_2(tmpValue);

                            tmpValue = info.getString("tel_3");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setTel_3(tmpValue);

                            tmpValue = info.getString("business_hour_open");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setBusiness_hour_open(tmpValue);

                            tmpValue = info.getString("business_hour_closed");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setBusiness_hour_closed(tmpValue);

                            tmpValue = info.getString("detail_kor");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setDetail_kor(tmpValue);

                            tmpValue = info.getString("detail_chn");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setDetail_chn(tmpValue);

                            tmpValue = info.getString("budget");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setBudget(tmpValue);

                            tmpValue = info.getString("latitude");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "0";
                            store.setLatitude(tmpValue);

                            tmpValue = info.getString("longitude");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "0";
                            store.setLongitude(tmpValue);

                            tmpValue = info.getString("is_pay");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setIs_pay(Integer.parseInt(tmpValue));

                            tmpValue = info.getString("menu_input_type");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setMenu_input_type(tmpValue);

                            tmpValue = info.getString("distance");
                            if (tmpValue.equals("") || tmpValue.equals("null") || tmpValue.equals("NULL"))
                                tmpValue = "";
                            store.setDistance(tmpValue);

                        }//end else (result)
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        hidepDialog();
                    } //end jsonexception catch
                    hidepDialog();
//data setting
                    straight_pay_kor.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() < 1) {
                                straight_pay_chn.setText("");
                                straight_pay_chn_web.setText("");
                            } else {
                                price_kor = Integer.parseInt(s.toString());

//                            price_sale_kor = (int) (price_kor * (1 - sale));

                                if (price_kor <= 50000) {
                                    //5%
                                    sale = 0.05;
                                    price_sale_kor = (int) (price_kor * 0.95);
                                } else if (price_kor <= 100000) {
                                    //4%
                                    sale = 0.04;
                                    price_sale_kor = (int) (price_kor * 0.96);
                                } else {
                                    //3%
                                    sale = 0.03;
                                    price_sale_kor = (int) (price_kor * 0.97);
                                }

                                if (price_kor - price_sale_kor >= 10000) {
                                    //최대 10,000원 할인
                                    priceKorSale = (price_kor - 10000) + "";
                                    priceChnSale = new MakePaymentPrice().makePaymentPrice(price_kor - 10000 + "", getApplicationContext(), curr);
                                    straight_pay_chn.setText(getResources().getString(R.string.curr_chn) + " " + priceChnSale);
                                } else {
                                    priceKorSale = price_sale_kor + "";
                                    priceChnSale = new MakePaymentPrice().makePaymentPrice(price_sale_kor + "", getApplicationContext(), curr);
                                    straight_pay_chn.setText(getResources().getString(R.string.curr_chn) + " " + priceChnSale);

                                }
                                straight_pay_chn_web.setText(new MakePaymentPrice().makePaymentPrice(price_kor + "", getApplicationContext(), curr));

//                            straight_pay_chn.setText(getResources().getString(R.string.curr_chn) + " " + new MakePaymentPrice().makePaymentPrice(Integer.parseInt(s.toString()) * 0.9 + "", getApplicationContext(), curr));
//                            straight_pay_chn_web.setText(getResources().getString(R.string.curr_chn) + " " + new MakePaymentPrice().makePaymentPrice(Integer.parseInt(s.toString()) * 0.95 + "", getApplicationContext(), curr));
                            }
                        }
                    });

                }//end response

            }, new Response.ErrorListener() {   //end request

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),
                            "Internet Access Failed", Toast.LENGTH_SHORT).show();
                    showErrorAndFinish();
                    //hide the progress dialog
                    hidepDialog();
                }
            });

            // Adding request to request queue

            AppController.getInstance().addToRequestQueue(objreq);
        }//end internet check success

    }   //end getStoreInfo

//dialog

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showErrorAndFinish() {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(TDInstantPayActivity.this);
        alert_confirm.setMessage("本美食店跟甜点公司还没携手").setCancelable(false).setPositiveButton(getResources().getString(R.string.btn_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }
}
