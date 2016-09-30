package tndn.app.nyam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tndn.app.nyam.R;
import tndn.app.nyam.data.StoreMenuData;
import tndn.app.nyam.utils.MakePricePretty;

public class FoodOrderAdapter extends BaseAdapter {


    private Context mcontext;
    private LayoutInflater mInflater;


    ArrayList<StoreMenuData> list;


    public FoodOrderAdapter(Context context, ArrayList<StoreMenuData> list) {
        this.mcontext = context;
        this.list = new ArrayList<StoreMenuData>();
        this.list.addAll(list);
        mInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public StoreMenuData getItem(int position) {
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

            convertView = mInflater.inflate(R.layout.item_food_order_lv, parent, false);

            viewHolder = new CustomViewHolder();
            viewHolder.item_food_order_name_chn = (TextView) convertView.findViewById(R.id.item_food_order_name_chn);
            viewHolder.item_food_order_name_kor = (TextView) convertView.findViewById(R.id.item_food_order_name_kor);
            viewHolder.item_food_order_price_kor = (TextView) convertView.findViewById(R.id.item_food_order_price_kor);
            viewHolder.item_food_order_price_chn = (TextView) convertView.findViewById(R.id.item_food_order_price_chn);
            viewHolder.item_food_order_count = (TextView) convertView.findViewById(R.id.item_food_order_count);

            convertView.setTag(viewHolder);
        }
        // 캐시된 뷰가 있을 경우 저장된 뷰홀더를 사용한다
        else {
            viewHolder = (CustomViewHolder) convertView.getTag();
        }


        // Set the result into ImageView

        viewHolder.item_food_order_name_chn.setText(list.get(position).getMenu_name_chn());
        viewHolder.item_food_order_name_kor.setText(list.get(position).getMenu_name_kor());
        viewHolder.item_food_order_price_kor.setText(new MakePricePretty().makePricePretty(mcontext, list.get(position).getMenu_price(), true));
        viewHolder.item_food_order_price_chn.setText(new MakePricePretty().makePricePretty(mcontext, list.get(position).getMenu_price(), false));
        viewHolder.item_food_order_count.setText(list.get(position).getCount() + "");


        return convertView;

    }

    public class CustomViewHolder {

        public TextView item_food_order_name_chn;
        public TextView item_food_order_name_kor;
        public TextView item_food_order_price_kor;
        public TextView item_food_order_price_chn;
        public TextView item_food_order_count;
    }


}
