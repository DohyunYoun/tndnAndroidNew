package tndn.app.nyam;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.CustomRequest;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.utils.UserLog;
import tndn.app.nyam.widget.ClearEditText;

public class TDUserEditActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    /**
     * Declare View
     */
    private ProgressDialog pDialog;
    private TextView user_edit_id_textview;
    private TextView user_edit_id;
    private TextView user_edit_password_textview;
    private TextView user_edit_password;
    private TextView user_edit_name_textview;
    private ClearEditText user_edit_name;

    //user_edit_age.cliced : datePickerDialog
    private LinearLayout user_edit_age;
    private TextView user_edit_age_textview;
    private TextView user_edit_age_year;
    private TextView user_edit_age_month;
    private TextView user_edit_age_date;

    private RadioGroup user_edit_gender;
    private TextView user_edit_gender_textview;

    private ImageView user_edit_ok;

    /**
     * DatePickerDialog
     */
    private String age = "";
    private int year, month, day;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td_user_edit);
/********************************************
 for actionbar
 ********************************************/

        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);
//        Button actionbar_qr_button = (Button) findViewById(R.id.actionbar_qr_button);

//        actionbar_qr_button.setVisibility(View.VISIBLE);
        actionbar_text.setText(getResources().getString(R.string.login_join));
//        actionbar_qr_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                intent.putExtra("SCAN_MODE", "ALL");
//                startActivityForResult(intent, 0);
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
/********************************************
 for actionbar
 ********************************************/

        initView();
        initialize();
        /**
         * hidelayout CLICK
         */
        findViewById(R.id.useredit_hidelayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
        getUser(PreferenceManager.getInstance(this).getTndnid());

        /**
         * edittext id / password / name focust chage listener
         */
        user_edit_id.setOnFocusChangeListener(this);
        user_edit_password.setOnFocusChangeListener(this);
        user_edit_name.setOnFocusChangeListener(this);
        user_edit_age.setOnFocusChangeListener(this);
        user_edit_gender.setOnFocusChangeListener(this);

        /**
         * datePicker Dialog
         */
        user_edit_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_edit_id_textview.setSelected(false);
                user_edit_password_textview.setSelected(false);
                user_edit_name_textview.setSelected(false);
                user_edit_age_textview.setSelected(true);
                user_edit_gender_textview.setSelected(false);
                new DatePickerDialog(TDUserEditActivity.this, android.R.style.Theme_Holo_Light_Dialog, dateSetListener, year, month, day).show();

            }
        });

        /**
         * Useruser_edit
         */
        user_edit_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입 api 실행
                Map<String, String> params = new HashMap<>();
                params.put("tndn_id", PreferenceManager.getInstance(getApplicationContext()).getTndnid());
                String gender;
                int id = user_edit_gender.getCheckedRadioButtonId();
                int _id = R.id.user_edit_gender_male;
                if (user_edit_gender.getCheckedRadioButtonId() == R.id.user_edit_gender_male) {
                    //female
                    gender = "m";
                } else {
                    //male
                    gender = "f";
                }
                params.put("gender", gender);
                params.put("location", "");
                params.put("name", user_edit_name.getText().toString());
                params.put("age", age);
                params.put("wexin_id", "");
                params.put("os", "3");
                params.put("usercode", PreferenceManager.getInstance(getApplicationContext()).getUsercode());
                params.put("useris", PreferenceManager.getInstance(getApplicationContext()).getUseris());

                CustomRequest req = new CustomRequest(Request.Method.POST, new TDUrls().setUserURL, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject res) {
                        try {
                            String result = res.getString("result");
                            String data = res.getString("data");

                            if (result.equals("success")) {

                                PreferenceManager.getInstance(getApplicationContext()).setUsername(user_edit_name.getText().toString());
                                PreferenceManager.getInstance(getApplicationContext()).setUseremail(user_edit_id.getText().toString());

                                startActivity(new Intent(TDUserEditActivity.this, TDMypageActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
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
                        Log.e("usereditActivity", error.getMessage());
                        Toast.makeText(getApplicationContext(), "Internet Access Failed", Toast.LENGTH_SHORT).show();

                    }
                });
                AppController.getInstance().addToRequestQueue(req);

            }
        });

    }

    private void initView() {

        user_edit_id_textview = (TextView) findViewById(R.id.user_edit_id_textview);
        user_edit_id = (TextView) findViewById(R.id.user_edit_id);
        user_edit_password_textview = (TextView) findViewById(R.id.user_edit_password_textview);
        user_edit_password = (TextView) findViewById(R.id.user_edit_password);
        user_edit_name_textview = (TextView) findViewById(R.id.user_edit_name_textview);
        user_edit_name = (ClearEditText) findViewById(R.id.user_edit_name);

        user_edit_age = (LinearLayout) findViewById(R.id.user_edit_age);
        user_edit_age_textview = (TextView) findViewById(R.id.user_edit_age_textview);
        user_edit_age_year = (TextView) findViewById(R.id.user_edit_age_year);
        user_edit_age_month = (TextView) findViewById(R.id.user_edit_age_month);
        user_edit_age_date = (TextView) findViewById(R.id.user_edit_age_date);

        user_edit_gender = (RadioGroup) findViewById(R.id.user_edit_gender);
        user_edit_gender_textview = (TextView) findViewById(R.id.user_edit_gender_textview);

        user_edit_ok = (ImageView) findViewById(R.id.user_edit_ok);
    }

    private void initialize() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);

        /**
         * DatePickerDialog
         */
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
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


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        user_edit_id_textview.setSelected(false);
        user_edit_password_textview.setSelected(false);
        user_edit_name_textview.setSelected(false);
        user_edit_age_textview.setSelected(false);
        user_edit_gender_textview.setSelected(false);
        switch (v.getId()) {
            case R.id.user_edit_id:
                if (user_edit_id.hasFocus()) {
                    user_edit_id_textview.setSelected(true);
                }
                break;
            case R.id.user_edit_password:
                if (user_edit_password.hasFocus()) {
                    user_edit_password_textview.setSelected(true);
                }
                break;
            case R.id.user_edit_name:
                if (user_edit_name.hasFocus()) {
                    user_edit_name_textview.setSelected(true);
                }
                break;
        }
    }

    /**
     * DatePickerDialog Listener
     */
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
//            String msg = String.format("%d / %d / %d", year, monthOfYear + 1, dayOfMonth);
//            Toast.makeText(TDuser_editActivity.this, msg, Toast.LENGTH_SHORT).show();
            user_edit_age_year.setText(String.format("%d", year));
            user_edit_age_month.setText(String.format("%d", monthOfYear + 1));
            user_edit_age_date.setText(String.format("%d", dayOfMonth));
            age = String.format("%d%02d%02d", year, monthOfYear + 1, dayOfMonth);
        }
    };

    private void getUser(String tndn_id) {
        JsonObjectRequest objreq = new JsonObjectRequest(new TDUrls().getUserURL+"?id="+tndn_id+ new UserLog().getLog(this), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject res) {

                try {
                    String result = res.getString("result");
                    if (result.equals("success")) {
                        /**
                         * {
                         "result": "success",
                         "data": {
                         "social_classify": "tndn",
                         "social_user_id": "tndn@tndn.net",
                         "password": "tndn0505",
                         "wexin_id": "",
                         "name": "tndn",
                         "gender": "f",
                         "age": "20160624",
                         "location": ""
                         }
                         }
                         */
                        JSONObject obj = res.getJSONObject("data");
                        String social_classify = obj.getString("social_classify");
                        String social_user_id = obj.getString("social_user_id");
                        String password = obj.getString("password");
                        String wexin_id = obj.getString("wexin_id");
                        String name = obj.getString("name");
                        String gender = obj.getString("gender");
                        String age = obj.getString("age");
                        String location = obj.getString("location");

                        if (gender.equals("")) {
                            gender = "f";
                        }
                        if (age.equals("")) {
                            age = "19950505";
                        }

                        PreferenceManager.getInstance(getApplicationContext()).setUsername(name);
                        PreferenceManager.getInstance(getApplicationContext()).setUseremail(social_user_id);
                        user_edit_id.setText(social_user_id);
                        String pw = "";
                        for (int i = 0; i < password.length(); i++) {
                            pw = pw + "*";
                        }
                        user_edit_password.setText(pw);
                        user_edit_name.setText(name);
                        user_edit_age_year.setText(age.substring(0, 4));
                        user_edit_age_month.setText(age.substring(4, 6));
                        user_edit_age_date.setText(age.substring(6, 8));

                        if (gender.equals("m")) {
                            user_edit_gender.check(R.id.user_edit_gender_male);
                        } else {
                            user_edit_gender.check(R.id.user_edit_gender_female);
                        }

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

    private void hideKeyboard() {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
}
