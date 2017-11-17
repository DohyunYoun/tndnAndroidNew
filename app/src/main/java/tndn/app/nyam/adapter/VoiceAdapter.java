package tndn.app.nyam.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tndn.app.nyam.R;
import tndn.app.nyam.TDHomeActivity;
import tndn.app.nyam.VoiceActivity;

public class VoiceAdapter extends BaseAdapter {


    private Context mcontext;
    private LayoutInflater mInflater;
    private ArrayList<String> list;
    ArrayList<Integer> sounds;

    public ImageView voice_image;
    public TextView voice_text;
    private LinearLayout voice_ll;
    MediaPlayer mp;

    private String where;

    boolean playingCheck = false;

    public VoiceAdapter(Context context, ArrayList<String> list, ArrayList<Integer> sounds, String where) {
        this.mcontext = context;
        this.list = list;
        this.sounds = sounds;
        this.where = where;
        mInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ImageView getItem(int position) {
        return voice_image;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        // 캐시된 뷰가 없을 경우 새로 생성하고 뷰홀더를 생성한다

        convertView = mInflater.inflate(R.layout.item_voice_lv, parent, false);

        voice_image = (ImageView) convertView.findViewById(R.id.voice_image);
        voice_text = (TextView) convertView.findViewById(R.id.voice_text);
        voice_ll = (LinearLayout) convertView.findViewById(R.id.voice_ll);


        // Set the result into ImageView

        voice_text.setText(list.get(position));
        if (!playingCheck) {

            if (where.equals("home")) {
                if (position == TDHomeActivity.mSelectedItem) {
//                    if (!playingCheck) {

                    // set your color
                    voice_image.setSelected(true);
                    mp = MediaPlayer.create(mcontext, sounds.get(position));
                    mp.start();
                    playingCheck = true;


                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            TDHomeActivity.mSelectedItem = -1;
                            voice_image.setSelected(false);
                            notifyDataSetChanged();
                            playingCheck = false;
                        }
                    });
//                }
                }
            } else {
                if (position == VoiceActivity.mSelectedItem) {
//                    if (!playingCheck) {
                    // set your color
                    voice_image.setSelected(true);
                    mp = MediaPlayer.create(mcontext, sounds.get(position));
                    mp.start();
                    playingCheck = true;

                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            VoiceActivity.mSelectedItem = -1;
                            voice_image.setSelected(false);
                            notifyDataSetChanged();
                            playingCheck = false;
                        }
                    });
//                    }

                }
            }
        }
        return convertView;


    }


}
