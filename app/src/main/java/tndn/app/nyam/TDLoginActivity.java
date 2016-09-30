package tndn.app.nyam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.widget.LoginButton;
import com.sina.weibo.sdk.widget.LoginoutButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.CustomRequest;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.weibo.AccessTokenKeeper;
import tndn.app.nyam.weibo.Constants;
import tndn.app.nyam.widget.ClearEditText;

/**
 * about access tokent
 * https://api.weibo.com/oauth2/access_token
 * <p/>
 * login_button_default
 */
public class TDLoginActivity extends AppCompatActivity {

    /**
     * UI元素列表
     */
//    private TextView mTokenView;
    private ProgressDialog pDialog;

    //skip
    private TextView login_skip;

    private ImageView login_id_imageview;
    private ClearEditText login_id;
    private ImageView login_password_imageview;
    private ClearEditText login_password;

    private TextView login_join;

    private ImageView login_tndn_login;
    private ImageView login_sina_weibo_login;


    /**
     * 该按钮用于记录当前点击的是哪一个 Button，用于在 {@link #onActivityResult}
     * 函数中进行区分。通常情况下，我们的应用中只需要一个合适的 {@link LoginButton}
     * 或者 {@link LoginoutButton} 即可。
     */

    private AuthInfo mAuthInfo;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    /**
     * 用户信息接口
     */
    private UsersAPI mUsersAPI;

    private String from;


    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td_login);

        if (getIntent().getStringExtra("FROM").equals("MYPAGE")) {
            from = "MYPAGE";
        } else if (getIntent().getStringExtra("FROM").equals("BUYCOUPON")) {
            from = "BUYCOUPON";
        } else {
            from = "BUYCOUPON";
        }


        /********************************************
         for actionbar
         ********************************************/

        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);
//        Button actionbar_qr_button = (Button) findViewById(R.id.actionbar_qr_button);

//        actionbar_qr_button.setVisibility(View.VISIBLE);
        actionbar_text.setText(getResources().getString(R.string.login_login));
//        actionbar_qr_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                intent.putExtra("SCAN_MODE", "ALL");
//                startActivityForResult(intent, 0);
//            }
//        });
        if (from.equals("MYPAGE")) {

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), TDMypageActivity.class));
                    finish();
                }
            });
        } else if (from.equals("BUYCOUPON")) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
/********************************************
 for actionbar
 ********************************************/




        initView();
        initialize();

        /**
         * hidelayout CLICK
         */
        findViewById(R.id.login_hidelayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
        /**
         * weibo login CLICK
         */
        login_sina_weibo_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorize(new AuthListener());
            }
        });

        /**
         * logout
         */
//        final Button logoutButton = (Button) findViewById(R.id.logout_button);
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AccessTokenKeeper.clear(getApplicationContext());
//                mAccessToken = new Oauth2AccessToken();
//                updateTokenView(false);
//            }
//        });

        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
            updateTokenView(true);
        }

        /**
         * edittext id / password focust chage listener
         */
        login_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (login_id.hasFocus()) {
                    login_id_imageview.setSelected(true);
                    login_password_imageview.setSelected(false);
                } else {
                    login_id_imageview.setSelected(false);
                    login_password_imageview.setSelected(true);
                }
            }
        });

        login_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (login_password.hasFocus()) {
                    login_id_imageview.setSelected(false);
                    login_password_imageview.setSelected(true);
                } else {
                    login_id_imageview.setSelected(true);
                    login_password_imageview.setSelected(false);
                }
            }
        });

        /**
         * login from tndn id and password
         * id : 이메일형식
         * pw : 6자 이상
         * url : userLogin
         * @param user_id
         * @param social_classify
         * @param password
         */
        login_tndn_login.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (login_id.length() == 0) {
                                                        Toast.makeText(getApplicationContext(), R.string.login_email_hint, Toast.LENGTH_SHORT).show();

                                                    } else if (login_password.length() == 0) {
                                                        Toast.makeText(getApplicationContext(), R.string.login_password_hint, Toast.LENGTH_SHORT).show();
                                                    } else if (login_password.length() < 6) {
                                                        Toast.makeText(getApplicationContext(), R.string.login_password_hint, Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        Map<String, String> params = new HashMap<>();
                                                        params.put("user_id", login_id.getText().toString());
                                                        params.put("social_classify", "tndn");
                                                        params.put("password", login_password.getText().toString());
                                                        params.put("os", "3");
                                                        params.put("useris", PreferenceManager.getInstance(getApplicationContext()).getUseris());

                                                        CustomRequest req = new CustomRequest(Request.Method.POST, new TDUrls().userLoginURL, params, new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject res) {
                                                                try {
                                                                    String result = res.getString("result");
                                                                    String data = res.getString("data");

                                                                    if (result.equals("success")) {
                                                                        PreferenceManager.getInstance(getApplicationContext()).setTndnid(data);
                                                                        PreferenceManager.getInstance(getApplicationContext()).setUseremail(login_id.getText().toString());
                                                                        PreferenceManager.getInstance(getApplicationContext()).setUsercode(data);
//                                                                        startActivity(new Intent(TDLoginActivity.this, TDHomeActivity.class));
                                                                        if (from.equals("MYPAGE")) {
                                                                            startActivity(new Intent(getApplicationContext(), TDMypageActivity.class));
                                                                            finish();
                                                                        } else if (from.equals("BUYCOUPON")) {
                                                                            finish();
                                                                        } else {
                                                                            finish();
                                                                        }
                                                                    } else {
                                                                        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                                                                        login_id.setText("");
                                                                        login_password.setText("");
                                                                    }

                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                    Toast.makeText(getApplicationContext(),
                                                                            "Error: " + e.getMessage(),
                                                                            Toast.LENGTH_LONG).show();
                                                                    hidepDialog();
                                                                } //end jsonexception catch

                                                                hidepDialog();

                                                            }//end response

                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Log.e("loginActivity", error.getMessage());
                                                                Toast.makeText(getApplicationContext(), "Internet Access Failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        AppController.getInstance().addToRequestQueue(req);
                                                    }
                                                }
                                            }

        );
        /**
         * skip CLICK
         */
        login_skip.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View v) {
                                              PreferenceManager.getInstance(getApplicationContext()).setTndnid("skip");
//                                              startActivity(new Intent(TDLoginActivity.this, TDHomeActivity.class));
                                              finish();

                                          }
                                      }

        );

        /**
         * join CLICK
         */
        login_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TDLoginActivity.this, TDJoinActivity.class));
            }
        });
    }

    private void initView() {
        //        mTokenView = (TextView) findViewById(R.id.result);

        login_skip = (TextView) findViewById(R.id.login_skip);

        login_id_imageview = (ImageView) findViewById(R.id.login_id_imageview);
        login_id = (ClearEditText) findViewById(R.id.login_id);
        login_password_imageview = (ImageView) findViewById(R.id.login_password_imageview);
        login_password = (ClearEditText) findViewById(R.id.login_password);

        login_join = (TextView) findViewById(R.id.login_join);

        login_tndn_login = (ImageView) findViewById(R.id.login_tndn_login);
        login_sina_weibo_login = (ImageView) findViewById(R.id.login_sina_weibo_login);
    }

    private void initialize() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);


        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(this, Constants.SINA_APP_KEY, Constants.SINA_REDIRECT_URL, Constants.SINA_SCOPE);
        mSsoHandler = new SsoHandler(TDLoginActivity.this, mAuthInfo);
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息
            String phoneNum = mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                updateTokenView(false);

                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(TDLoginActivity.this, mAccessToken);
//                Toast.makeText(TDLoginActivity.this,
//                        "success", Toast.LENGTH_SHORT).show();

                /**
                 * 소셜 로그인 성공
                 * TDHomeActivity.class로 intent하고
                 * 서버에 tocken, userID, 성별 보내서 디비에 저장하고
                 * 서버에서 tndnID받아와서 usercode로 씁시다
                 *
                 */

                // getUserInform
                mAccessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
                // 获取用户信息接口
                mUsersAPI = new UsersAPI(getApplication(), Constants.SINA_APP_KEY, mAccessToken);
                long uid = Long.parseLong(mAccessToken.getUid());
                mUsersAPI.show(uid, userListener);


            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "failed";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(TDLoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(TDLoginActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(TDLoginActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);

//        mTokenView.setText(String.format(format, mAccessToken.getToken(), date));

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = "weibosdk_demo_token_has_existed" + "\n" + message;
        }
//        mTokenView.setText(message);
    }


    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener userListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
//                LogUtil.i(TAG, response);
                // 调用 User#parse 将JSON串解析成User对象
                final User user = User.parse(response);
                if (user != null) {
                    showpDialog();
//                    여기서 서버로 고고하고 인텐트 고고?
                    /**
                     * 소셜 로그인 성공
                     * TDHomeActivity.class로 intent하고
                     * 서버에 tocken, userID, 성별 보내서 디비에 저장하고
                     * 서버에서 tndnID받아와서 usercode로 씁시다
                     *
                     */
                    /**
                     * @param user_Id, social_classify, password, gender, location, name, age, wexin_id
                     */
                    Map<String, String> params = new HashMap<>();

                    params.put("user_id", user.id);
                    params.put("social_classify", "weibo");
                    params.put("password", "");
                    params.put("gender", user.gender);
                    params.put("location", user.location);
                    params.put("name", user.screen_name);
                    params.put("age", "");
                    params.put("wexin_id", "");
                    params.put("os", "3");
                    params.put("useris", PreferenceManager.getInstance(getApplicationContext()).getUseris());
                    CustomRequest req = new CustomRequest(Request.Method.POST, new TDUrls().userJoinURL, params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject res) {
                            try {
                                String result = res.getString("result");
                                String data = res.getString("data");

                                if (result.equals("success")) {
                                    PreferenceManager.getInstance(getApplicationContext()).setTndnid(data);
                                    PreferenceManager.getInstance(getApplicationContext()).setUsername(user.name);
                                    PreferenceManager.getInstance(getApplicationContext()).setUseremail(user.id);

                                    PreferenceManager.getInstance(getApplicationContext()).setUsercode(data);
//                                    startActivity(new Intent(TDLoginActivity.this, TDHomeActivity.class));
                                    if (from.equals("MYPAGE")) {
                                        startActivity(new Intent(getApplicationContext(), TDMypageActivity.class));
                                        finish();
                                    } else if (from.equals("BUYCOUPON")) {
                                        finish();
                                    } else {
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                                    login_id.setText("");
                                    login_password.setText("");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                hidepDialog();
                            } //end jsonexception catch

                            hidepDialog();

                        }//end response

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("loginActivity", error.getMessage());
                        }
                    });
                    AppController.getInstance().addToRequestQueue(req);

//                    mTokenView.setText(mTokenView.getText().toString() + "\n" + "\n" + user.gender);

                } else {
                    Toast.makeText(TDLoginActivity.this, response, Toast.LENGTH_LONG).show();

                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
//            LogUtil.e(TAG, e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(TDLoginActivity.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };

    //dialog
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void hideKeyboard() {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
}
