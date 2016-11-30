package tndn.app.nyam;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.IsOnline;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;


public class SplashActivity extends AppCompatActivity {


    private int splash[] = new int[]{R.mipmap.img_splash_1, R.mipmap.img_splash_2, R.mipmap.img_splash_3, R.mipmap.img_splash_4, R.mipmap.img_splash_5};
    int versionCode;
    RelativeLayout splash_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        /**
         * set user code
         */
        if (new UserLog().getUsercode(this).equals("") || new UserLog().getUsercode(this) == null) {
            new UserLog().setUserCode(getApplicationContext());

        }

        splash_layout = (RelativeLayout) findViewById(R.id.splash_layout);
        int i = (int) (Math.random() * 4);   // 0~4 까지의 int 랜덤 생성
        splash_layout.setBackgroundResource(splash[i]);

        PreferenceManager.getInstance(getApplicationContext()).setLocationcheck("");

        if (new IsOnline().onlineCheck(this)) {                 //internet check true start

            try {
                versionCode = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            JsonObjectRequest req = new JsonObjectRequest(new TDUrls().chkVersion + "?version=" + versionCode + "&thisIs=androidUser", new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("data").equals("check")) {//if result failed
                            splash_layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                }
                            });
                            //점검중
                            AlertDialog.Builder alert_check = new AlertDialog.Builder(SplashActivity.this);
                            alert_check.setPositiveButton(getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();     //닫기
                                    finish();
                                }
                            });

                            alert_check.setMessage("正在检查中。。。");
                            alert_check.show();

                        } else if (response.getString("data").equals("update")) {
                            splash_layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    chkUserIsHandler();
                                }
                            });
                            //업데이트 필요
                            AlertDialog.Builder alert_check = new AlertDialog.Builder(SplashActivity.this);
                            alert_check.setPositiveButton(getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();     //닫기
                                    chkUserIsHandler();
//                                    finish();
                                }
                            });

                            alert_check.setMessage("更新已经完成。\n" +
                                    "请在对应的应用商城中下载更新。");
                            alert_check.show();


                        } else {
                            chkUserIsHandler();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        chkUserIsHandler();
                    } //end jsonexception catch


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    chkUserIsHandler();

                }
            });
            AppController.getInstance().addToRequestQueue(req);


        } else {
            chkUserIsHandler();
        }

        PreferenceManager.getInstance(this).setOrderby("distance");
        PreferenceManager.getInstance(this).setFoodId("");
        PreferenceManager.getInstance(this).setLocationId("");
    }

    private void chkUserIntent() {
//        if (PreferenceManager.getInstance(getApplicationContext()).getTndnid().equals("") || PreferenceManager.getInstance(getApplicationContext()).getTndnid() == null) {
//            //tndn id 가 없으면 회원가입이 되어있지 않은유저( or 앱을 지웠다가 다시깔았거나)
//            startActivity(new Intent(SplashActivity.this, TDLoginActivity.class));
//            finish();
//        } else {
        startActivity(new Intent(SplashActivity.this, TDHomeActivity.class));
        finish();
//        }
    }

    private void chkUserIsHandler() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (PreferenceManager.getInstance(SplashActivity.this).getUseris().equals("") || PreferenceManager.getInstance(getApplicationContext()).getUseris() == null) {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(SplashActivity.this);
                    alert_confirm.setMessage("您的旅行类型是什么").setCancelable(false).setPositiveButton("自由行",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    PreferenceManager.getInstance(getApplicationContext()).setUseris("fit");
                                    chkUserIntent();
                                }
                            }).setNegativeButton("团体游",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    PreferenceManager.getInstance(getApplicationContext()).setUseris("pack");
                                    chkUserIntent();
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();

                } else {
                    chkUserIntent();
                }

            }
        };

        handler.sendEmptyMessageDelayed(0, 2000);
    }
}
