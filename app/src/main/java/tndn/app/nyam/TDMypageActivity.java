package tndn.app.nyam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.BackPressCloseHandler;
import tndn.app.nyam.utils.LogHome;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;

public class TDMypageActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private TextView mypage_logout;
    private LinearLayout mypage_user;
    private ImageView mypage_more;
    private TextView mypage_name;
    private TextView mypage_email;
    private ImageView mypage_coupon;
    private ImageView mypage_assistant;

    boolean isUser = false;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td_mypage);
        /********************************************
         for actionbar
         ********************************************/

        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);
        Button refresh = (Button) findViewById(R.id.actionbar_refresh_button);

        refresh.setVisibility(View.VISIBLE);
        actionbar_text.setText(getResources().getString(R.string.mypage));
//        actionbar_qr_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                intent.putExtra("SCAN_MODE", "ALL");
//                startActivityForResult(intent, 0);
//            }
//        });
        back.setVisibility(View.GONE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), TDMypageActivity.class));
                finish();
            }
        });
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
/********************************************
 for actionbar
 ********************************************/

        /*********************************************
         for tabbar
         *********************************************/
        final ImageView tabbar_home = (ImageView) findViewById(R.id.tabbar_home);
        final ImageView tabbar_assistant = (ImageView) findViewById(R.id.tabbar_assistant);
        final ImageView tabbar_mypage = (ImageView) findViewById(R.id.tabbar_mypage);
//        tabbar_home.setSelected(true);
//        tabbar_assistant.setSelected(true);
        tabbar_mypage.setSelected(true);

        tabbar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogHome().send(getApplicationContext(),"tab-home");

                startActivity(new Intent(getApplicationContext(), TDHomeActivity.class));
                finish();
            }
        });

        tabbar_assistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogHome().send(getApplicationContext(),"tab-assistant");

                startActivity(new Intent(getApplicationContext(), TDAssistantActivity.class));
                finish();
            }
        });
//        tabbar_mypage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), TDMypageActivity.class));
//                finish();
//            }
//        });


        /*********************************************
         for tabbar
         *********************************************/

        initView();
        initialize();

        if (PreferenceManager.getInstance(this).getUsername().equals("") || PreferenceManager.getInstance(this).getUseremail().equals("")) {
            //이름이 등록되어 있지 않음.
            //tndn로그인일때 or 회원가입이 되어있지 않을때
            //(소셜로그인, 회원가입때는 등록되어있음)
            if (PreferenceManager.getInstance(this).getTndnid().equals("") || PreferenceManager.getInstance(this).getTndnid().equals("skip")) {
                //not registered
                isUser = false;

                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(TDMypageActivity.this);
                alert_confirm.setMessage(getResources().getString(R.string.plz_login)).setCancelable(false).setPositiveButton(getResources().getString(R.string.btn_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(TDMypageActivity.this, TDLoginActivity.class).putExtra("FROM","MYPAGE"));

                            }
                        }).setNegativeButton(getResources().getString(R.string.btn_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
                mypage_logout.setText(getResources().getString(R.string.login_login));
            } else {
                isUser = true;
                //isUser, tndn로그인을 했거나 그외 에러사항
                getUser(PreferenceManager.getInstance(this).getTndnid());
                mypage_logout.setText(getResources().getString(R.string.logout));
            }
        } else {
            isUser = true;
            mypage_name.setText(PreferenceManager.getInstance(this).getUsername());
            mypage_email.setText(PreferenceManager.getInstance(this).getUseremail());
            mypage_logout.setText(getResources().getString(R.string.logout));

        }

        /**
         * mypage coupon CLICK
         */
        mypage_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isUser) {
//                    startActivity(new Intent(TDMypageActivity.this, MypageCouponListACtivity.class));
//                } else {
//                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(TDMypageActivity.this);
//                    alert_confirm.setMessage(getResources().getString(R.string.plz_login)).setCancelable(false).setPositiveButton(getResources().getString(R.string.btn_ok),
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    startActivity(new Intent(TDMypageActivity.this, TDLoginActivity.class).putExtra("FROM","MYPAGE"));
//
//                                }
//                            }).setNegativeButton(getResources().getString(R.string.btn_cancel),
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    AlertDialog alert = alert_confirm.create();
//                    alert.show();
//                }
            }
        });

        /**
         * logout CLICK
         */
        mypage_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isUser) {
                    mypage_name.setText(getResources().getString(R.string.login_login));
                    mypage_email.setText("");
                    isUser = false;
                    PreferenceManager.getInstance(getApplicationContext()).setTndnid("");
                    PreferenceManager.getInstance(getApplicationContext()).setUsername("");
                    PreferenceManager.getInstance(getApplicationContext()).setUseremail("");
                } else {
                    startActivity(new Intent(TDMypageActivity.this, TDLoginActivity.class).putExtra("FROM","MYPAGE"));
                    PreferenceManager.getInstance(getApplicationContext()).setTndnid("");
                    PreferenceManager.getInstance(getApplicationContext()).setUsername("");
                    PreferenceManager.getInstance(getApplicationContext()).setUseremail("");

                }
            }
        });

        mypage_assistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(TDMypageActivity.this, TranslateActivity.class));
            }
        });
        /**
         * mypage_user CLICK
         * TO login or edit
         */

        mypage_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isUser)
                    startActivity(new Intent(TDMypageActivity.this, TDUserEditActivity.class));
                else {
                    startActivity(new Intent(TDMypageActivity.this, TDLoginActivity.class).putExtra("FROM", "MYPAGE"));
                    PreferenceManager.getInstance(getApplicationContext()).setTndnid("");
                    PreferenceManager.getInstance(getApplicationContext()).setUsername("");
                    PreferenceManager.getInstance(getApplicationContext()).setUseremail("");
                }


            }
        });

        /**
         * mypage_more CLICK
         * TO login or edit
         */

        mypage_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUser)
                    startActivity(new Intent(TDMypageActivity.this, TDUserEditActivity.class));
                else
                    startActivity(new Intent(TDMypageActivity.this, TDLoginActivity.class).putExtra("FROM","MYPAGE"));
            }
        });
    }

    private void initView() {
        mypage_logout = (TextView) findViewById(R.id.mypage_logout);

        mypage_user = (LinearLayout) findViewById(R.id.mypage_user);
        mypage_more = (ImageView) findViewById(R.id.mypage_more);

        mypage_name = (TextView) findViewById(R.id.mypage_name);
        mypage_email = (TextView) findViewById(R.id.mypage_email);

        mypage_coupon = (ImageView) findViewById(R.id.mypage_coupon);
        mypage_assistant = (ImageView) findViewById(R.id.mypage_assistant);

    }

    private void initialize() {

        backPressCloseHandler = new BackPressCloseHandler(this);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);

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

    private void getUser(String tndn_id) {
        JsonObjectRequest objreq = new JsonObjectRequest(new TDUrls().getUserInfoURL+"?id="+tndn_id + new UserLog().getLog(this), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {

                try {
                    if (res.getString("result").equals("success")) {
                        /**
                         {
                         "result": "success",
                         "data": [
                         {
                         "social_classify": "tndn",
                         "social_user_id": "1005@tndn.net",
                         "password": "100555",
                         "weixin_id": "",
                         "name": "1005",
                         "gender": "f",
                         "birthdate": "",
                         "location": ""
                         }
                         ]
                         }
                         */
                        JSONObject data = res.getJSONArray("data").getJSONObject(0);

                        String social_classify = data.getString("social_classify");
                        String social_user_id = data.getString("social_user_id");
                        String password = data.getString("password");
                        String weixin_id = data.getString("weixin_id");
                        String name = data.getString("name");
                        String gender = data.getString("gender");
                        String age = data.getString("birthdate");
                        String location = data.getString("location");


                        if (gender.equals("")) {
                            gender = "f";
                        }

                        if (age.equals("")) {
                            age = "19950505";
                        }


                        PreferenceManager.getInstance(getApplicationContext()).setUsername(name);
                        PreferenceManager.getInstance(getApplicationContext()).setUseremail(social_user_id);
                        mypage_name.setText(name);
                        mypage_email.setText(social_user_id);

                    } else {

                    }//end result fail else

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    hidepDialog();
                } //end jsonexception catch
                hidepDialog();
                /**data setting*/


            }   //end response

        }, new Response.ErrorListener() {   //end request

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Internet Access Failed", Toast.LENGTH_SHORT).show();
                //hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(objreq);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
