package tndn.app.nyam;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import tndn.app.nyam.utils.MakePricePretty;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.utils.SaveExchangeRate;
import tndn.app.nyam.utils.SoftKeyboardDectectorView;
import tndn.app.nyam.widget.ClearEditText;

public class TDExchangeRateActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView actionbar_text;
    private Button back;
    private ProgressDialog pDialog;

    private ScrollView myScrollView;
    private TextView exchange_rate_rate;
    private TextView exchange_rate_date;
    private ClearEditText exchange_rate_edittext;
    private TextView exchange_rate_result;
    private ImageView exchange_rate_kr_imageview;
    private ImageView exchange_rate_rk_imageview;
    private LinearLayout hidelayout;

    private ImageView exchange_rate_change;
    private boolean isKrw = true;

    private String date;
    private String rate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rate);

        date = PreferenceManager.getInstance(this).getCurrDate();
        rate = PreferenceManager.getInstance(this).getCurrency();

        initView();
        initialize();


        if (date == null || date.equals("") || date.equals(null) || rate == null || rate.equals("") || rate.equals(null)) {
            //첫화면에서든 언제든 날짜와 환율이 저장되지 않았으면
            new SaveExchangeRate().saveExchangeRate(this);
            date = PreferenceManager.getInstance(this).getCurrDate();
            rate = PreferenceManager.getInstance(this).getCurrency();
        }
        exchange_rate_date.setText(date);
        exchange_rate_rate.setText(rate);


        hidelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
        exchange_rate_change.setOnClickListener(this);

        final SoftKeyboardDectectorView softKeyboardDecector = new SoftKeyboardDectectorView(this);
        addContentView(softKeyboardDecector, new FrameLayout.LayoutParams(-1, -1));

        softKeyboardDecector.setOnShownKeyboard(new SoftKeyboardDectectorView.OnShownKeyboardListener() {
            @Override
            public void onShowSoftKeyboard() {
                //키보드 등장할 때
                myScrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        exchange_rate_edittext.requestFocus();
                    }

                }, 100);
            }
        });

        softKeyboardDecector.setOnHiddenKeyboard(new SoftKeyboardDectectorView.OnHiddenKeyboardListener() {

            @Override
            public void onHiddenSoftKeyboard() {
                // 키보드 사라질 때
                myScrollView.postDelayed(new Runnable() {
                    //
                    @Override
                    public void run() {
                        myScrollView.fullScroll(ScrollView.FOCUS_UP);
                    }

                }, 200);
            }
        });


        exchange_rate_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isKrw) {
                    //중국 > 한국
                    if (s.length() < 1) {
                        exchange_rate_result
                                .setText("");
                    } else {
                        exchange_rate_result
                                .setText(new MakePricePretty().makePricePretty(getApplicationContext(), String.valueOf(Double.parseDouble(s.toString()) * Double.parseDouble(rate)), true));
                    }
                } else if (isKrw) {
//                    한국 > 중국
                    if (s.length() < 1) {
                        exchange_rate_result
                                .setText("");
                    } else {
                        exchange_rate_result
                                .setText(new MakePricePretty().makePricePretty(getApplicationContext(), s.toString(), false));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Access Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void initialize() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.plz_wait));
        pDialog.setCancelable(false);

        actionbar_text.setText(getResources().getString(R.string.exchange_rate));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (isKrw) {
            exchange_rate_kr_imageview.setSelected(true);
            exchange_rate_rk_imageview.setSelected(false);
            //krw > rmb 일때
        } else {
            exchange_rate_kr_imageview.setSelected(false);
            exchange_rate_rk_imageview.setSelected(true);
            //rmb > krw 일때
        }

//        exchange_rate_delete.setOnClickListener(this);

    }

    private void initView() {
        actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        back = (Button) findViewById(R.id.actionbar_back_button);

        myScrollView = (ScrollView) findViewById(R.id.myScrollView);
        exchange_rate_rate = (TextView) findViewById(R.id.exchange_rate_rate);
        exchange_rate_date = (TextView) findViewById(R.id.exchange_rate_date);
        exchange_rate_edittext = (ClearEditText) findViewById(R.id.exchange_rate_edittext);
        exchange_rate_result = (TextView) findViewById(R.id.exchange_rate_result);
        exchange_rate_change = (ImageView) findViewById(R.id.exchange_rate_change);

        exchange_rate_kr_imageview = (ImageView) findViewById(R.id.exchange_rate_kr_imageview);
        exchange_rate_rk_imageview = (ImageView) findViewById(R.id.exchange_rate_rk_imageview);

        hidelayout = (LinearLayout) findViewById(R.id.hidelayout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exchange_rate_change:
                isKrw = !isKrw;
                if (isKrw) {
                    exchange_rate_kr_imageview.setSelected(true);
                    exchange_rate_rk_imageview.setSelected(false);
                    //krw > rmb 일때
                    if (!exchange_rate_edittext.getText().toString().equals(""))
                        exchange_rate_result
                                .setText(new MakePricePretty().makePricePretty(getApplicationContext(), exchange_rate_edittext.getText().toString(), false));
                } else {
                    exchange_rate_kr_imageview.setSelected(false);
                    exchange_rate_rk_imageview.setSelected(true);
                    //rmb > krw 일때
                    if (!exchange_rate_edittext.getText().toString().equals(""))
                        exchange_rate_result
                                .setText(new MakePricePretty().makePricePretty(getApplicationContext(), String.valueOf(Double.parseDouble(exchange_rate_edittext.getText().toString()) * Double.parseDouble(rate)), true));

                }
                break;
        }
    }

    private void hideKeyboard() {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
}
