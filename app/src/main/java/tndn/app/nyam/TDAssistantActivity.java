package tndn.app.nyam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tndn.app.nyam.utils.BackPressCloseHandler;
import tndn.app.nyam.utils.LogHome;
import tndn.app.nyam.utils.SaveImagetoStorage;

/**
 * Created by YounDit on 2016-02-12.
 */
public class TDAssistantActivity extends AppCompatActivity {

    private RelativeLayout assistant_popup;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td_assistant);

/********************************************
 for actionbar
 ********************************************/
        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);

        actionbar_text.setText(getResources().getString(R.string.cs));
        back.setVisibility(View.GONE);
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
        tabbar_assistant.setSelected(true);
//        tabbar_mypage.setSelected(true);

        tabbar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogHome().send(getApplicationContext(), "tab-home");
                startActivity(new Intent(getApplicationContext(), TDHomeActivity.class));
                finish();
            }
        });

//        tabbar_assistant.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), TDAssistantActivity.class));
//                finish();
//            }
//        });
        tabbar_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogHome().send(getApplicationContext(),"tab-mypage");

                startActivity(new Intent(getApplicationContext(), TDMypageActivity.class));
                finish();
            }
        });


        /*********************************************
         for tabbar
         *********************************************/

        backPressCloseHandler = new BackPressCloseHandler(this);


        assistant_popup = (RelativeLayout) findViewById(R.id.assistant_popup);

        //open assistant popup
        findViewById(R.id.assistant_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assistant_popup.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.assistant_popup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assistant_popup.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.assistant_qr).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new SaveImagetoStorage().saveImagetoStorage(getApplicationContext(), R.mipmap.qr_cschat, "tndn-qr-assistant");

                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
