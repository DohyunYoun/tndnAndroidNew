package tndn.app.nyam.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import tndn.app.nyam.R;

/**
 * Created by YounDit on 2016-02-12.
 */
public class TDActionBar extends LinearLayout implements View.OnClickListener {

    private View mBackButton;
    private View mQrButton;
    private View mRefreshButton;

    public TDActionBar(Context context) {
        super(context);
        initialize();
    }

    public TDActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TDActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.td_actionbar, this, true);

        mBackButton = findViewById(R.id.actionbar_back_button);
        mBackButton.setOnClickListener(this);

        mQrButton = findViewById(R.id.actionbar_qr_button);
        mQrButton.setOnClickListener(this);

        mRefreshButton = findViewById(R.id.actionbar_refresh_button);
        mRefreshButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                break;
            case R.id.actionbar_qr_button:
                break;
            case R.id.actionbar_refresh_button:
//                PreferenceManager.getInstance(getContext()).setLocationcheck("");
                break;
        }
    }

}
