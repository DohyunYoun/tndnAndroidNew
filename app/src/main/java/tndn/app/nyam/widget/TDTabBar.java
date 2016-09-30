package tndn.app.nyam.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import tndn.app.nyam.R;

public class TDTabBar extends LinearLayout implements View.OnClickListener {

    private View tabbar_home;
    private View tabbar_assistant;
    private View tabbar_mypage;

    public TDTabBar(Context context) {
        super(context);
        initialize();
    }

    public TDTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TDTabBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.td_tabbar, this, true);

        tabbar_home = findViewById(R.id.tabbar_home);
        tabbar_home.setOnClickListener(this);

        tabbar_assistant = findViewById(R.id.tabbar_assistant);
        tabbar_assistant.setOnClickListener(this);

        tabbar_mypage = findViewById(R.id.tabbar_mypage);
        tabbar_mypage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        tabbar_home.setSelected(false);
        tabbar_assistant.setSelected(false);
        tabbar_mypage.setSelected(false);
        switch (v.getId()) {
            case R.id.tabbar_home:
                tabbar_home.setSelected(true);
                break;
            case R.id.tabbar_assistant:
                tabbar_assistant.setSelected(true);
                break;
            case R.id.tabbar_mypage:
                tabbar_mypage.setSelected(true);
                break;
        }
    }

}
