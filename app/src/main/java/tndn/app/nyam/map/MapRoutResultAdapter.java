package tndn.app.nyam.map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tndn.app.nyam.R;

public class MapRoutResultAdapter extends BaseAdapter {


    private Context mcontext;
    private LayoutInflater mInflater;


    ArrayList<MapRoutNodeData> routNodes;
    ArrayList<MapRoutLinkData> routLinks;
    int[] number = {R.mipmap.ic_number_1_small, R.mipmap.ic_number_2_small, R.mipmap.ic_number_3_small, R.mipmap.ic_number_4_small, R.mipmap.ic_number_5_small, R.mipmap.ic_number_6_small, R.mipmap.ic_number_7_small,
            R.mipmap.ic_number_8_small, R.mipmap.ic_number_9_small, R.mipmap.ic_number_10_small, R.mipmap.ic_number_11_small, R.mipmap.ic_number_12_small, R.mipmap.ic_number_13_small, R.mipmap.ic_number_14_small,
            R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small, R.mipmap.ic_number_15_small};


    public MapRoutResultAdapter(Context context, ArrayList<MapRoutNodeData> routNodes, ArrayList<MapRoutLinkData> routLinks) {
        this.mcontext = context;
        this.routNodes = new ArrayList<MapRoutNodeData>();
        this.routNodes.addAll(routNodes);

        this.routLinks = new ArrayList<MapRoutLinkData>();
        this.routLinks.addAll(routLinks);

        mInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return routNodes.size();
    }

    @Override
    public MapRoutNodeData getItem(int position) {
        return routNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final CustomViewHolder viewHolder;
        // 캐시된 뷰가 없을 경우 새로 생성하고 뷰홀더를 생성한다
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.item_map_rout_result_lv, parent, false);

            viewHolder = new CustomViewHolder();
            viewHolder.item_map_rout_result_number = (ImageView) convertView.findViewById(R.id.item_map_rout_result_number);
            viewHolder.item_map_rout_result_text = (TextView) convertView.findViewById(R.id.item_map_rout_result_text);
            viewHolder.item_map_rout_result_direction = (ImageView) convertView.findViewById(R.id.item_map_rout_result_direction);

            convertView.setTag(viewHolder);
        }
        // 캐시된 뷰가 있을 경우 저장된 뷰홀더를 사용한다
        else {
            viewHolder = (CustomViewHolder) convertView.getTag();
        }


        // Set the result into ImageView

        viewHolder.item_map_rout_result_number.setImageResource(number[position]);
        if (position == routLinks.size()) {
            viewHolder.item_map_rout_result_text.setText("到达");
        } else {
            viewHolder.item_map_rout_result_text.setText(getDistStr(String.valueOf(routLinks.get(position).getLength())));
        }

        if (routNodes.get(position).getRotation() == 0) {
            //직진
            viewHolder.item_map_rout_result_direction.setImageResource(R.mipmap.ic_direction_0);
        } else if (routNodes.get(position).getRotation() == 1) {
            //우회전
            viewHolder.item_map_rout_result_direction.setImageResource(R.mipmap.ic_direction_1);

        } else if (routNodes.get(position).getRotation() == 2) {
            //유턴
            viewHolder.item_map_rout_result_direction.setImageResource(R.mipmap.ic_direction_2);

        } else if (routNodes.get(position).getRotation() == 3) {
            //좌회전
            viewHolder.item_map_rout_result_direction.setImageResource(R.mipmap.ic_direction_3);

        } else {
            Log.e("TNDN_LOG", "map rout error");
        }


        return convertView;

    }

    public class CustomViewHolder {

        public ImageView item_map_rout_result_number;
        public TextView item_map_rout_result_text;
        public ImageView item_map_rout_result_direction;
    }

    public String getDistStr(String dist) {
        if (Integer.parseInt(dist) > 900) {
            return String.valueOf(Math.round(Integer.parseInt(dist) / 100) / 10) + "Km";
        } else {
            return dist + "m";
        }
    }


}
