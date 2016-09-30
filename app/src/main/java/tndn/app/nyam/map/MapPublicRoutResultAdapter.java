package tndn.app.nyam.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tndn.app.nyam.R;

public class MapPublicRoutResultAdapter extends BaseAdapter {


    private Context mcontext;
    private LayoutInflater mInflater;


    private ArrayList<MapPublicPathData> pathDatas;

    MapPublicPathSubPathData subPathData;
    MapPublicPathSubPath01Data subPath01Data;
    MapPublicPathSubPath02Data subPath02Data;
    MapPublicPathSubPath02_laneData subPath02_laneData;
    MapPublicPathSubPath01Data subPath03Data;
    MapPublicPathInfoData infoData;


    public MapPublicRoutResultAdapter(Context context, ArrayList<MapPublicPathData> pathDatas) {
        this.mcontext = context;
        this.pathDatas = new ArrayList<MapPublicPathData>();
        this.pathDatas.addAll(pathDatas);

        mInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pathDatas.size();
    }

    @Override
    public MapPublicPathData getItem(int position) {
        return pathDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final CustomViewHolder viewHolder;
        // 캐시된 뷰가 없을 경우 새로 생성하고 뷰홀더를 생성한다
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.item_map_public_rout_result_lv, parent, false);

            viewHolder = new CustomViewHolder();
            viewHolder.item_map_public_rout_result_start = (TextView) convertView.findViewById(R.id.item_map_public_rout_result_start);
            viewHolder.item_map_public_rout_result_end = (TextView) convertView.findViewById(R.id.item_map_public_rout_result_end);
            viewHolder.item_map_public_rout_result_dist = (TextView) convertView.findViewById(R.id.item_map_public_rout_result_dist);
            viewHolder.item_map_public_rout_result_time = (TextView) convertView.findViewById(R.id.item_map_public_rout_result_time);
            viewHolder.item_map_public_rout_result_transit = (TextView) convertView.findViewById(R.id.item_map_public_rout_result_transit);
            viewHolder.item_map_public_rout_result_cost = (TextView) convertView.findViewById(R.id.item_map_public_rout_result_cost);
            viewHolder.item_map_public_rout_result_busno = (TextView) convertView.findViewById(R.id.item_map_public_rout_result_busno);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomViewHolder) convertView.getTag();
        }

        // 캐시된 뷰가 있을 경우 저장된 뷰홀더를 사용한다
        subPathData = pathDatas.get(position).getSubpath();
        subPath01Data = subPathData.getSub01();
        subPath02Data = subPathData.getSub02();
        subPath02_laneData = subPath02Data.getLane();
        subPath03Data = subPathData.getSub03();
        infoData = pathDatas.get(position).getInfo();

        viewHolder.item_map_public_rout_result_start.setText(infoData.getFirstStartStation() + "");
        viewHolder.item_map_public_rout_result_end.setText(infoData.getLastEndStation() + "");
        viewHolder.item_map_public_rout_result_dist.setText(getDistStr(String.valueOf(infoData.getTotalDistance())));
        viewHolder.item_map_public_rout_result_time.setText(getTimeStr(String.valueOf(infoData.getTotalTime())));
        viewHolder.item_map_public_rout_result_transit.setText("(转让 " + (infoData.getBusTransitCount() - 1) + "次)");
        viewHolder.item_map_public_rout_result_cost.setText(infoData.getPayment() + mcontext.getResources().getString(R.string.curr_kor));
        viewHolder.item_map_public_rout_result_busno.setText(subPath02_laneData.getBusNo() + "");

        return convertView;

    }

    public class CustomViewHolder {
        TextView item_map_public_rout_result_start;
        TextView item_map_public_rout_result_end;
        TextView item_map_public_rout_result_dist;
        TextView item_map_public_rout_result_time;
        TextView item_map_public_rout_result_transit;
        TextView item_map_public_rout_result_cost;
        TextView item_map_public_rout_result_busno;

    }

    public String getDistStr(String dist) {
        if (Integer.parseInt(dist) > 999) {
            return String.valueOf(Math.round(Integer.parseInt(dist) / 100) / 10) + "Km";
        } else {
            return dist + "m";
        }
    }

    public String getTimeStr(String s_sec) {
        int sec = Integer.parseInt(s_sec) * 60;
        double h = Math.floor(sec / 3600);
        double m = (h > 0) ? Math.floor((sec - (h * 3600)) / 60) : Math.floor(sec / 60);
        double s = sec - ((h * 3600) + (m * 60));
        String result = "";
        result = (h > 0) ? result + h + "点" : result;
        result = (m > 0) ? result + " " + m + "分钟" : result;
        return result.replace(".0", "");
    }

}
