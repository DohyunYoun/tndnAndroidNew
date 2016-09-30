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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.ChkUserJoin;
import tndn.app.nyam.utils.CustomRequest;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.widget.ClearEditText;

public class TDJoinActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    /**
     * Declare View
     */
    private ProgressDialog pDialog;
    private TextView join_id_textview;
    private ClearEditText join_id;
    private TextView join_password_textview;
    private ClearEditText join_password;
    private TextView join_name_textview;
    private ClearEditText join_name;

    //join_age.cliced : datePickerDialog
    private LinearLayout join_age;
    private TextView join_age_textview;
    private TextView join_age_year;
    private TextView join_age_month;
    private TextView join_age_date;

    private RadioGroup join_gender;
    private TextView join_gender_textview;

    private ImageView join_ok;

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
        setContentView(R.layout.activity_td_join);
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
        findViewById(R.id.join_hidelayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
        /**
         * edittext id / password / name focust chage listener
         */
        join_id.setOnFocusChangeListener(this);
        join_password.setOnFocusChangeListener(this);
        join_name.setOnFocusChangeListener(this);
        join_age.setOnFocusChangeListener(this);
        join_gender.setOnFocusChangeListener(this);

        /**
         * datePicker Dialog
         */
        join_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join_id_textview.setSelected(false);
                join_password_textview.setSelected(false);
                join_name_textview.setSelected(false);
                join_age_textview.setSelected(true);
                join_gender_textview.setSelected(false);
                new DatePickerDialog(TDJoinActivity.this, android.R.style.Theme_Holo_Light_Dialog, dateSetListener, year, month, day).show();

            }
        });

        /**
         * UserJoin
         */
        join_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ChkUserJoin.validateEmail(join_id.getText().toString()) || join_id.length() == 0) {
                    // 이메일 형식이 맞지 않음
                    Toast.makeText(getApplicationContext(), R.string.login_email_hint, Toast.LENGTH_SHORT).show();

                } else if (!ChkUserJoin.validatePassword(join_password.getText().toString()) || join_password.length() < 6) {
                    // 비밀번호 형식이 맞지 않음
                    Toast.makeText(getApplicationContext(), R.string.login_password_hint, Toast.LENGTH_SHORT).show();

                } else {
                    //회원가입 api 실행
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", join_id.getText().toString());
                    params.put("social_classify", "tndn");
                    params.put("password", join_password.getText().toString());
                    String gender;
                    int id = join_gender.getCheckedRadioButtonId();
                    int _id = R.id.join_gender_male;
                    if (join_gender.getCheckedRadioButtonId() == R.id.join_gender_male) {
                        //female
                        gender = "m";
                    } else {
                        //male
                        gender = "f";
                    }
                    params.put("gender", gender);
                    params.put("location", "");
                    params.put("name", join_name.getText().toString());
                    params.put("age", age);
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
                                    PreferenceManager.getInstance(getApplicationContext()).setUsercode(data);
                                    PreferenceManager.getInstance(getApplicationContext()).setTndnid(data);

                                    PreferenceManager.getInstance(getApplicationContext()).setUsername(join_name.getText().toString());
                                    PreferenceManager.getInstance(getApplicationContext()).setUseremail(join_id.getText().toString());

                                    startActivity(new Intent(TDJoinActivity.this, TDHomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                                    join_id.setText("");
                                    join_password.setText("");
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
                            Log.e("joinActivity", error.getMessage());
                            Toast.makeText(getApplicationContext(), "Internet Access Failed", Toast.LENGTH_SHORT).show();

                        }
                    });
                    AppController.getInstance().addToRequestQueue(req);
                }

            }
        });

    }

    private void initView() {

        join_id_textview = (TextView) findViewById(R.id.join_id_textview);
        join_id = (ClearEditText) findViewById(R.id.join_id);
        join_password_textview = (TextView) findViewById(R.id.join_password_textview);
        join_password = (ClearEditText) findViewById(R.id.join_password);
        join_name_textview = (TextView) findViewById(R.id.join_name_textview);
        join_name = (ClearEditText) findViewById(R.id.join_name);

        join_age = (LinearLayout) findViewById(R.id.join_age);
        join_age_textview = (TextView) findViewById(R.id.join_age_textview);
        join_age_year = (TextView) findViewById(R.id.join_age_year);
        join_age_month = (TextView) findViewById(R.id.join_age_month);
        join_age_date = (TextView) findViewById(R.id.join_age_date);

        join_gender = (RadioGroup) findViewById(R.id.join_gender);
        join_gender_textview = (TextView) findViewById(R.id.join_gender_textview);

        join_ok = (ImageView) findViewById(R.id.join_ok);
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
        join_id_textview.setSelected(false);
        join_password_textview.setSelected(false);
        join_name_textview.setSelected(false);
        join_age_textview.setSelected(false);
        join_gender_textview.setSelected(false);
        switch (v.getId()) {
            case R.id.join_id:
                if (join_id.hasFocus()) {
                    join_id_textview.setSelected(true);
                }
                break;
            case R.id.join_password:
                if (join_password.hasFocus()) {
                    join_password_textview.setSelected(true);
                }
                break;
            case R.id.join_name:
                if (join_name.hasFocus()) {
                    join_name_textview.setSelected(true);
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
//            Toast.makeText(TDJoinActivity.this, msg, Toast.LENGTH_SHORT).show();
            join_age_year.setText(String.format("%d", year));
            join_age_month.setText(String.format("%d", monthOfYear + 1));
            join_age_date.setText(String.format("%d", dayOfMonth));
            age = String.format("%d%02d%02d", year, monthOfYear + 1, dayOfMonth);
        }
    };

    private void hideKeyboard() {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
}
