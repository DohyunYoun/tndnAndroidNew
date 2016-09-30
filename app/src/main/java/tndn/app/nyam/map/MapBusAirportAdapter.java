package tndn.app.nyam.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tndn.app.nyam.R;

public class MapBusAirportAdapter extends BaseAdapter {


    private Context mcontext;
    private LayoutInflater mInflater;

    private TextView item_map_bus_airport_busnum;
    private TextView item_map_bus_airport_busgoto;
    private TextView item_map_bus_airport_miniute;
    private TextView item_map_bus_airport_leftcount;

    private ArrayList<MapBusAirportData> list;

    public MapBusAirportAdapter(Context context, ArrayList<MapBusAirportData> list) {
        this.mcontext = context;
        this.list = list;
        mInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MapBusAirportData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        // 캐시된 뷰가 없을 경우 새로 생성하고 뷰홀더를 생성한다

        convertView = mInflater.inflate(R.layout.item_map_bus_airport_lv, parent, false);

        item_map_bus_airport_busnum = (TextView) convertView.findViewById(R.id.item_map_bus_airport_busnum);
        item_map_bus_airport_busgoto = (TextView) convertView.findViewById(R.id.item_map_bus_airport_busgoto);
        item_map_bus_airport_miniute = (TextView) convertView.findViewById(R.id.item_map_bus_airport_miniute);
        item_map_bus_airport_leftcount = (TextView) convertView.findViewById(R.id.item_map_bus_airport_leftcount);

        item_map_bus_airport_busnum.setText(list.get(position).getBusNum() + "번");
        item_map_bus_airport_busgoto.setText(list.get(position).getBusGoto());
        item_map_bus_airport_miniute.setText(list.get(position).getPredicttravtm() + " 분");
        item_map_bus_airport_leftcount.setText(list.get(position).getLeftstation() + " 정류소 전");

        return convertView;


    }


}
