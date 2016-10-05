package tndn.app.nyam.alipay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import tndn.app.nyam.R;
import tndn.app.nyam.TDHomeActivity;

public class PayCompleteActivity extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pay_complete);


        mRunnable = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(PayCompleteActivity.this, TDHomeActivity.class);
                startActivity(i);
                finish();
            }
        };

        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 3000);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

}
