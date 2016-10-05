package tndn.app.nyam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import tndn.app.nyam.utils.SaveImagetoStorage;

/**
 * Created by YounDH on 2016-06-28.
 */
public class BannerInnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_inner);
/********************************************
 for actionbar
 ********************************************/

        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);

        actionbar_text.setText(getResources().getString(R.string.app_name));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
/********************************************
 for actionbar
 ********************************************/

        ImageView banner = (ImageView) findViewById(R.id.img_banner_inner);

        Intent i = getIntent();
        if (Intent.ACTION_VIEW.equals(i.getAction())) {
            //카테고리에서 아이템 클릭이나 홈에서 아이템 클릭
            Uri uri = i.getData();
            if (uri.getQueryParameter("id").equals("rentcar")) {
                banner.setImageResource(R.mipmap.img_rentcar);
                banner.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new SaveImagetoStorage().saveImagetoStorage(getApplicationContext(), R.mipmap.qr_rentcar, "tndn-qr-rentcar");
                        return false;
                    }
                });

            } else if (uri.getQueryParameter("id").equals("aidibao")) {
                banner.setImageResource(R.mipmap.img_aidibao);
                banner.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new SaveImagetoStorage().saveImagetoStorage(getApplicationContext(), R.mipmap.qr_aidibao, "tndn-qr-aidibao");
                        return false;
                    }
                });

            } else if (uri.getQueryParameter("id").equals("simya")) {
                banner.setImageResource(R.mipmap.img_simya);
                banner.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new SaveImagetoStorage().saveImagetoStorage(getApplicationContext(), R.mipmap.qr_simya, "tndn-qr-simya");
                        return false;
                    }
                });

            } else {
                banner.setImageResource(R.mipmap.img_logo);
            }
        } else {
            banner.setImageResource(R.mipmap.img_logo);
        }
    }
}
