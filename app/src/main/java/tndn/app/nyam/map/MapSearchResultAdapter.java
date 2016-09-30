package tndn.app.nyam.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tndn.app.nyam.R;

public class MapSearchResultAdapter extends BaseAdapter {


    private Context mcontext;
    private LayoutInflater mInflater;


    ArrayList<MapSearchResultData> list;


    public MapSearchResultAdapter(Context context, ArrayList<MapSearchResultData> list) {
        this.mcontext = context;
        this.list = new ArrayList<MapSearchResultData>();
        this.list.addAll(list);
        mInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MapSearchResultData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final CustomViewHolder viewHolder;
        // 캐시된 뷰가 없을 경우 새로 생성하고 뷰홀더를 생성한다
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.item_map_search_result_lv, parent, false);

            viewHolder = new CustomViewHolder();
            viewHolder.item_map_search_result_name = (TextView) convertView.findViewById(R.id.item_map_search_result_name);
            viewHolder.item_map_search_result_address_kor = (TextView) convertView.findViewById(R.id.item_map_search_result_address_kor);
            viewHolder.item_map_search_result_address_chn = (TextView) convertView.findViewById(R.id.item_map_search_result_address_chn);

            convertView.setTag(viewHolder);
        }
        // 캐시된 뷰가 있을 경우 저장된 뷰홀더를 사용한다
        else {
            viewHolder = (CustomViewHolder) convertView.getTag();
        }


        // Set the result into ImageView

        viewHolder.item_map_search_result_name.setText(list.get(position).getName()+" ("+list.get(position).getName_kor()+")");
        viewHolder.item_map_search_result_address_kor.setText(list.get(position).getSido_kor()+" "+list.get(position).getSgg_kor()+" "+list.get(position).getHemd_kor()+" ("+list.get(position).getBemd_kor()+")");
        viewHolder.item_map_search_result_address_chn.setText(list.get(position).getSido_chn()+" "+list.get(position).getSgg_chn()+" "+list.get(position).getHemd_chn());


        return convertView;

    }

    public class CustomViewHolder {

        public TextView item_map_search_result_name;
        public TextView item_map_search_result_address_kor;
        public TextView item_map_search_result_address_chn;
    }


}
