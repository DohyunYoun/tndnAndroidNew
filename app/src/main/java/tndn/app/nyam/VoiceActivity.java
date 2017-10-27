package tndn.app.nyam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tndn.app.nyam.adapter.VoiceAdapter;

public class VoiceActivity extends AppCompatActivity {

    private ArrayList<String> text;
    private ArrayList<Integer> w_sounds;
    private ArrayList<Integer> m_sounds;

    private ImageView voice_woman;
    private ImageView voice_man;

    ListView lv_voice;
    private VoiceAdapter mAdapter;

    public static int mSelectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
/********************************************
 for actionbar
 ********************************************/

        TextView actionbar_text = (TextView) findViewById(R.id.actionbar_text);
        Button back = (Button) findViewById(R.id.actionbar_back_button);
        Button actionbar_qr_button = (Button) findViewById(R.id.actionbar_qr_button);

        actionbar_qr_button.setVisibility(View.VISIBLE);
        actionbar_text.setText(getResources().getString(R.string.voice));
        actionbar_qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.tndn.SCAN");
                intent.putExtra("SCAN_MODE", "ALL");
                startActivityForResult(intent, 0);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
/********************************************
 for actionbar
 ********************************************/


        initialize();
        initView();
        addData();


        voice_woman.setSelected(true);
        voice_man.setSelected(false);


        if (voice_woman.isSelected()) {
            mAdapter = new VoiceAdapter(this, text, w_sounds, "voice");
            lv_voice.setAdapter(mAdapter);
        } else {
            mAdapter = new VoiceAdapter(this, text, m_sounds, "voice");
            lv_voice.setAdapter(mAdapter);
        }

        lv_voice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedItem = position;
                mAdapter.notifyDataSetChanged();
            }
        });
        voice_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!voice_woman.isSelected()) {
                    mSelectedItem = -1;
                    mAdapter = new VoiceAdapter(getApplicationContext(), text, w_sounds, "voice");
                    lv_voice.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    voice_woman.setSelected(true);
                    voice_man.setSelected(false);
                }
            }
        });

        voice_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voice_woman.isSelected()) {
                    mSelectedItem = -1;
                    mAdapter = new VoiceAdapter(getApplicationContext(), text, m_sounds, "voice");
                    lv_voice.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    voice_woman.setSelected(false);
                    voice_man.setSelected(true);
                }
            }
        });
    }

    private void initialize() {
        text = new ArrayList<String>();
        w_sounds = new ArrayList<Integer>();
        m_sounds = new ArrayList<Integer>();
    }

    private void initView() {
        voice_woman = (ImageView) findViewById(R.id.voice_woman);
        voice_man = (ImageView) findViewById(R.id.voice_man);
        lv_voice = (ListView) findViewById(R.id.voice_listview);
    }

    private void addData() {
        text.add("谢谢");
        text.add("可以点菜吗？");
        text.add("请帮我拿点碟子");
        text.add("老板 加一点小餐");
        text.add("请加辣");

        text.add("老板、 我要微辣");
        text.add("请不要加辣");
        text.add("请再拿点水");
        text.add("请问洗手间在哪儿");

        text.add("多少钱");
        text.add("打包可以吗");
        text.add("便宜一点吧");
        text.add("可以用人民币结算吗");
        text.add("请结算");

        text.add("请帮我叫个出租车");
        text.add("有名的旅行景点在哪里");

        w_sounds.add(R.raw.w_thanks);
        w_sounds.add(R.raw.w_order);
        w_sounds.add(R.raw.w_plate);
        w_sounds.add(R.raw.w_more);
        w_sounds.add(R.raw.w_hot);

        w_sounds.add(R.raw.w_lowhot);
        w_sounds.add(R.raw.w_nohot);
        w_sounds.add(R.raw.w_water);
        w_sounds.add(R.raw.w_toilet);
        w_sounds.add(R.raw.w_howmuch);

        w_sounds.add(R.raw.w_wrap);
        w_sounds.add(R.raw.w_discount);
        w_sounds.add(R.raw.w_china);
        w_sounds.add(R.raw.w_calculate);
        w_sounds.add(R.raw.w_taxi);

        w_sounds.add(R.raw.w_hotplace);

        m_sounds.add(R.raw.m_thanks);
        m_sounds.add(R.raw.m_order);
        m_sounds.add(R.raw.m_plate);
        m_sounds.add(R.raw.m_more);
        m_sounds.add(R.raw.m_hot);

        m_sounds.add(R.raw.m_lowhot);
        m_sounds.add(R.raw.m_nohot);
        m_sounds.add(R.raw.m_water);
        m_sounds.add(R.raw.m_toilet);
        m_sounds.add(R.raw.m_howmuch);

        m_sounds.add(R.raw.m_wrap);
        m_sounds.add(R.raw.m_discount);
        m_sounds.add(R.raw.m_china);
        m_sounds.add(R.raw.m_calculate);
        m_sounds.add(R.raw.m_taxi);

        m_sounds.add(R.raw.m_hotplace);
    }

}

