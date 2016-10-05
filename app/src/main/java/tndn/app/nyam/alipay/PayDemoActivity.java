package tndn.app.nyam.alipay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.TDUrls;

public class PayDemoActivity {
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Context mcontext;
    private Activity activity;


    private String id;
    private String outTradeNo;

    /**
     * @param mcontext
     * @param activity
     */

    public void init(Context mcontext, Activity activity) {
        this.mcontext = mcontext;
        this.activity = activity;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    //  response message from alipay server
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();
                    String resultMemo = payResult.getMemo();

                    //success code is 9000
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mcontext, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        /* success log and send message*/
                        StringRequest req = new StringRequest(Request.Method.POST, new TDUrls().setStorePay, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("success")) {
                                    String url = "tndn://complete";
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mcontext.startActivity(intent);
                                    activity.finish();
                                } else {
                                    Log.e("paylog", "fail");
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("paylog", error.toString());
                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("id", id);
                                params.put("thisIs", "instant");
                                params.put("outTradeNo", outTradeNo);
                                params.put("useris", PreferenceManager.getInstance(mcontext).getUseris());
                                params.put("userfrom", PreferenceManager.getInstance(mcontext).getUserfrom());
                                params.put("os", "android");
                                params.put("usercode", PreferenceManager.getInstance(mcontext).getUsercode());

                                return params;
                            }
                        };
                        AppController.getInstance().addToRequestQueue(req);


                    } else {

//                        alipay error
//                        or connection error
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mcontext, "Wait for Result",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            //user canceled error
                            Toast.makeText(mcontext, "info : " + resultInfo + "\nstatus : " + resultStatus + "\nmemo : " + resultMemo,
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(mcontext, "Search Result is：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * start simple
     */
    public void pay(int _id, String kor_name, String pd_name, String pd_desc, String priceChnSale, String _outTradeNo) {
        if (TextUtils.isEmpty(Keys.PARTNER) || TextUtils.isEmpty(Keys.RSA_PUBLIC)
                || TextUtils.isEmpty(Keys.SELLER)) {
            new AlertDialog.Builder(mcontext)
                    .setTitle("Warning")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    //
                                    return;
//                                    finish();
                                }
                            }).show();
            return;
        }

        if (pd_name.equals("")) {
            pd_name = kor_name;
        }

        id = String.valueOf(_id);
        outTradeNo = _outTradeNo;
        /**
         * BILL
         * getOrderInfo(productName, description, price_chn);
         * @param productName is printed to customer
         * @param Description
         * @param price_chn is CNY
         */
        String orderInfo = getOrderInfo(pd_name, pd_desc, priceChnSale, outTradeNo);
        // RSA Signing for enable order
        String sign = sign(orderInfo);
        try {
            // signed url encoding
            sign = URLEncoder.encode(sign, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

// list for all payment detail
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                // Call payment interface, get paid results
                String result = alipay.pay(payInfo);


                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // You must asynchronously call
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }//end simple
    /*
    *//**
     * call alipay sdk pay.
     * buy Restaurant
     *//*
    public void pay(int idx, String pd_name, String kor_name, String pd_desc, String priceKor, String priceChn, String priceKorSale, String priceChnSale, String _data, double curr, double sale) {
        if (TextUtils.isEmpty(Keys.PARTNER) || TextUtils.isEmpty(Keys.RSA_PUBLIC)
                || TextUtils.isEmpty(Keys.SELLER)) {
            new AlertDialog.Builder(mcontext)
                    .setTitle("Warning")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    //
                                    return;
//                                    finish();
                                }
                            }).show();
            return;
        }

        if (pd_name.equals("")) {
            pd_name = kor_name;
        }
        success_idx = idx;
        success_name = kor_name;
        success_price_kor = priceKor;
        success_price_chn = priceChn;
        success_priceKorSale = priceKorSale;
        success_priceChnSale = priceChnSale;
        success_curr = curr;
        success_sale = sale;

        data = _data;

        *//**
     * BILL
     * getOrderInfo(productName, description, price_chn);
     * @param productName is printed to customer
     * @param Description
     * @param price_chn is CNY
     *//*
        String orderInfo = getOrderInfo(pd_name, pd_desc, priceChnSale);
        Log.e("TNDN_LOG", "alipay : " + orderInfo);
        // RSA Signing for enable order
        String sign = sign(orderInfo);
        try {
            // signed url encoding
            sign = URLEncoder.encode(sign, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // list for all payment detail
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                // Call payment interface, get paid results
                String result = alipay.pay(payInfo);


                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // You must asynchronously call
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
*/

    /**
     * check whether the device has authentication alipay account.
     * Query whether there Alipay terminal equipment certification account
     */
    public void check() {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask payTask = new PayTask(activity);
                // Call query interface, get query results
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version.
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(activity);
        String version = payTask.getVersion();
        Toast.makeText(mcontext, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info.
     */
    public String getOrderInfo(String subject, String body, String price, String outTradeNo) {

        // Signed collaborator identity ID
        String orderInfo = "partner=" + "\"" + Keys.PARTNER + "\"";

        // Signing sellers Alipay account
        orderInfo += "&seller_id=" + "\"" + Keys.SELLER + "\"";

        // Merchant website unique order number
        orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";

        // product name
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // product details
        orderInfo += "&body=" + "\"" + body + "\"";

        //  The amount of goods
        orderInfo += "&rmb_fee=" + "\"" + price + "\"";
//        orderInfo += "&total_fee=" + "\"" + price_chn + "\"";

        // Server asynchronous notification Page path
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
                + "\"";

        // Service interface name, the fixed value
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // Payment type, fixed value
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值Parameter coding, fixed value
        orderInfo += "&_input_charset=\"utf-8\"";

        /**
         * Setting unpaid transaction timeout
         * Default 30 minutes, once the timeout, the transaction will automatically be closed.
         * The value range: 1m ~ 15d.
         * m- minutes, h- hour, d- day, 1c- day (regardless of when the transaction is created, are closed at 0:00).
         * The parameter value is not to accept the decimal point, such as 1.5h, can be converted to 90m.
         */
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // Alipay processed the request, the current page to jump to a specific page of the merchant path, you can empty
        orderInfo += "&return_url=\"m.alipay.com\"";

        // Call the bank card payments, you need to configure this parameter, participate signature, fixed value (requires signing the "wireless bank card quick payment" in order to use)
        // orderInfo += "&paymethod=\"expressGateway\"";

        orderInfo += "&forex_biz=\"FP\"";

//        orderInfo += "&currency=\"RMB\"";
        orderInfo += "&currency=\"USD\"";

        //appenv=”system=ipad^version=4.0.1.1”
        String appennv = "system=android^version=4.0.1";

        orderInfo += "&appenv=" + "\"" + appennv + "\"";

        orderInfo += "&app_id=\"aliiiiii572731\"";

        return orderInfo;
    }


    /**
     * sign the order info.
     *
     * @param content Order information to be signed
     */
    public String sign(String content) {
        return SignUtils.sign(content, Keys.RSA_PRIVATE);
    }

    /**
     * get the sign type we use.
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
