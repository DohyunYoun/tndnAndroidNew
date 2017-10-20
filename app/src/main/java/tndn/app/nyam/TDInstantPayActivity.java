package tndn.app.nyam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import tndn.app.nyam.alipay.PayDemoActivity;
import tndn.app.nyam.data.StoreInfoData;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.ControlKeyboard;
import tndn.app.nyam.utils.CustomRequest;
import tndn.app.nyam.utils.GenerateQrCode;
import tndn.app.nyam.utils.GpsInfo;
import tndn.app.nyam.utils.IsOnline;
import tndn.app.nyam.utils.MakePaymentPrice;
import tndn.app.nyam.utils.MakePricePretty;
import tndn.app.nyam.utils.OutTradeNo;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.SaveExchangeRate;
import tndn.app.nyam.utils.SaveImagetoStorage;
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

    private RelativeLayout straight_pay_qrcode_ll;
    private ImageView pay_qrcode;



    /**
     * value
     */
    double curr = 0;
    double sale = 0;
    //    String priceKor;
//    String priceChn;
//    String priceKorSale;
//    String priceChnSale;
    String data = "";
    String outTradeNo = "";

    int price_kor;
    //    int price_sale_kor;
    private String id;
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
                id = uri.getQueryParameter("id");
            }
        } else {
            showErrorAndFinish();
        }
        new SaveExchangeRate().saveExchangeRate(getApplicationContext());
        curr = Double.parseDouble(PreferenceManager.getInstance(this).getCurrency());


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

                    payCscanB(id, "0", straight_pay_kor.getText().toString().replace(".0", "").replace(",", ""), straight_pay_chn.getText().toString().replace("¥ ", "").replace(",", ""));
                    ControlKeyboard.hide(TDInstantPayActivity.this);
                }
            }
        });


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

//                    if (price_kor <= 50000) {
//                        //5%
//                        sale = 0.05;
//                        price_sale_kor = (int) (price_kor * 0.95);
//                    } else if (price_kor <= 100000) {
//                        //4%
//                        sale = 0.04;
//                        price_sale_kor = (int) (price_kor * 0.96);
//                    } else {
//                        //3%
//                        sale = 0.03;
//                        price_sale_kor = (int) (price_kor * 0.97);
//                    }

//                    if (price_kor - price_sale_kor >= 10000) {
//                        //최대 10,000원 할인
//                        priceKorSale = (price_kor - 10000) + "";
//                        priceChnSale = new MakePaymentPrice().makePaymentPrice(price_kor - 10000 + "", getApplicationContext(), curr);
//                        straight_pay_chn.setText(getResources().getString(R.string.curr_chn) + " " + priceChnSale);
//                    } else {
//                        priceKorSale = price_sale_kor + "";
//                        priceChnSale = new MakePaymentPrice().makePaymentPrice(price_sale_kor + "", getApplicationContext(), curr);
//                        straight_pay_chn.setText(getResources().getString(R.string.curr_chn) + " " + priceChnSale);
//
//                    }
                    straight_pay_chn.setText(new MakePaymentPrice().makePaymentPrice(price_kor + "", getApplicationContext(), curr));

//                            straight_pay_chn.setText(getResources().getString(R.string.curr_chn) + " " + new MakePaymentPrice().makePaymentPrice(Integer.parseInt(s.toString()) * 0.9 + "", getApplicationContext(), curr));
//                            straight_pay_chn_web.setText(getResources().getString(R.string.curr_chn) + " " + new MakePaymentPrice().makePaymentPrice(Integer.parseInt(s.toString()) * 0.95 + "", getApplicationContext(), curr));
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
        straight_pay_qrcode_ll = (RelativeLayout) findViewById(R.id.straight_pay_qrcode_ll);
        pay_qrcode = (ImageView) findViewById(R.id.pay_qrcode);
    }

    private void initialize() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);
        gps = new GpsInfo(this);

        sale = 0.05;
//        price_kor = 0;
//        price_sale_kor = 0;
//        priceChnSale = "";
    }


//dialog

    private void showpDialog() {
        if (!TDInstantPayActivity.this.isFinishing()) {
            if (!pDialog.isShowing())
                pDialog.show();
        }
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


    private void payCscanB(String id, final String idxAppUser, final String priceKor, final String priceChn) {

        if (!new IsOnline().onlineCheck(this)) {                  //internet check failed start
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.txt_dialog), Toast.LENGTH_SHORT).show();
        } else { //internet check success start
            showpDialog();
            Map<String, String> params = new HashMap<>();
            params.put("idxAppUser", idxAppUser);
            params.put("idxStore", id);
            params.put("userCode", new UserLog().getUsercode(getApplicationContext()));
            params.put("os", new UserLog().getOs());
            params.put("priceKor", priceKor);

            CustomRequest req = new CustomRequest(Request.Method.POST, new TDUrls().cscanbHwaxherURL, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getJSONObject("data").getString("resultcode").equals("00")) {
                            //성공
                            final Bitmap qrcode = new GenerateQrCode().bitmap(response.getJSONObject("data").getString("codeurl"));
                            pay_qrcode.setImageBitmap(qrcode);
                            straight_pay_qrcode_ll.setVisibility(View.VISIBLE);
                            hidepDialog();
                            straight_pay_qrcode_ll.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    straight_pay_qrcode_ll.setVisibility(View.GONE);

                                }
                            });
                            pay_qrcode.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    new SaveImagetoStorage().saveImagetoStorage(getApplicationContext(), qrcode, "tndn-qr-wxpay");
                                    straight_pay_qrcode_ll.setVisibility(View.GONE);
                                    return false;
                                }
                            });
                        } else {
                            //결제실패
                            hidepDialog();
                            AlertDialog.Builder builder = new AlertDialog.Builder(TDInstantPayActivity.this);
                            builder.setTitle("[ 티엔디엔 결제 ]")        // 제목 설정
                                    .setMessage("결제에 실패하였습니다. 다시 시도해주세요\n문의전화 : 1544-3980")        // 메세지 설정
                                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        // 확인 버튼 클릭시 설정
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            finish();
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog dialog = builder.show();    // 알림창 객체 생성
                        }//end result is failed
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.txt_dialog), Toast.LENGTH_SHORT).show();
                        hidepDialog();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("paylog", error.toString());
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.txt_dialog), Toast.LENGTH_SHORT).show();
                    hidepDialog();
                }
            });

            AppController.getInstance().addToRequestQueue(req);
            req.setRetryPolicy(new DefaultRetryPolicy(
                    180000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        }//end internet check success
    }   //end payCscanB
    @Override
    protected void onDestroy() {
        if (pDialog.isShowing())
            pDialog.dismiss();
        super.onDestroy();
    }
}
