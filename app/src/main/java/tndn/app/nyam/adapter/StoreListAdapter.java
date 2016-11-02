package tndn.app.nyam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import tndn.app.nyam.R;
import tndn.app.nyam.data.StoreListData;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.TDUrls;

public class StoreListAdapter extends BaseAdapter {

    private String url = new TDUrls().getImageURL + "?size=150";

    private LayoutInflater mInflater;
    private Context mcontext;


    ArrayList<StoreListData> list;


    //imageloader using volley
    ImageLoader mImageLoader;

    public StoreListAdapter(Context context, ArrayList<StoreListData> list) {
        this.mcontext = context;
        this.list = new ArrayList<StoreListData>();
        this.list.addAll(list);
        mImageLoader = AppController.getInstance().getImageLoader();
        mInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public StoreListData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View v, ViewGroup parent) {

        CustomViewHolder viewHolder;
        View convertView = v;
        // 캐시된 뷰가 없을 경우 새로 생성하고 뷰홀더를 생성한다
//        if (convertView == null) {

        convertView = mInflater.inflate(R.layout.item_store_list_lv, parent, false);
        viewHolder = new CustomViewHolder();

        viewHolder.item_res_category_imageview = (NetworkImageView) convertView.findViewById(R.id.item_res_category_imageview);
        viewHolder.item_res_category_name = (TextView) convertView.findViewById(R.id.item_res_category_name);
        viewHolder.item_res_category_address = (TextView) convertView.findViewById(R.id.item_res_category_address);
        //desc is keyword
        viewHolder.item_res_category_mainmenu = (TextView) convertView.findViewById(R.id.item_res_category_mainmenu);
        viewHolder.item_res_category_distance = (TextView) convertView.findViewById(R.id.item_res_category_distance);
        viewHolder.item_res_category_category = (TextView) convertView.findViewById(R.id.item_res_category_category);
        viewHolder.item_res_category_alipay = (ImageView) convertView.findViewById(R.id.item_res_category_alipay);

//            convertView.setTag(viewHolder);
//        }
//        // 캐시된 뷰가 있을 경우 저장된 뷰홀더를 사용한다
//        else {
//
//            viewHolder = (CustomViewHolder) convertView.getTag();
//        }


        // Set the result into ImageView
        if (list.get(position).getIdx_image_file_path() == 0) {
            viewHolder.item_res_category_imageview.setImageResource(R.mipmap.noimg_food);
        } else {
            viewHolder.item_res_category_imageview.setImageUrl(url+"&id=" + list.get(position).getIdx_image_file_path(), mImageLoader);
        }
        String resName;
        if (list.get(position).getName_chn().equals("") || list.get(position).getName_chn().equals("NULL")) {
            resName = list.get(position).getName_kor();
        } else {
            resName = list.get(position).getName_chn();
        }
        //        viewHolder.item_res_category_name.setText(list.get(position).getIdx_restaurant()+"_"+list.get(position).getKor_restaurantName());
        viewHolder.item_res_category_name.setText(resName);
        if (list.get(position).getAddress_chn().length() == 0 || list.get(position).getAddress_chn().equals("") || list.get(position).getAddress_chn().equals("NULL")) {
            viewHolder.item_res_category_address.setText(list.get(position).getAddress_kor());
        } else {
            viewHolder.item_res_category_address.setText(list.get(position).getAddress_chn());
        }
        viewHolder.item_res_category_mainmenu.setText(list.get(position).getMain_menu_chn());
        viewHolder.item_res_category_category.setText(list.get(position).getClassify_chn());
        if (list.get(position).getDistance().equals("0") || list.get(position).getDistance() == null || list.get(position).getDistance().equals(mcontext.getString(R.string.no_distance))) {
            viewHolder.item_res_category_distance.setText(list.get(position).getDistance());
        } else {
            viewHolder.item_res_category_distance.setText(list.get(position).getDistance() + " km");
        }

        if (list.get(position).getIs_pay().equals("1")) {
            viewHolder.item_res_category_alipay.setVisibility(View.VISIBLE);
        }

        return convertView;

    }

    class CustomViewHolder {

        public NetworkImageView item_res_category_imageview;
        public TextView item_res_category_name;
        public TextView item_res_category_address;
        public TextView item_res_category_mainmenu;
        public TextView item_res_category_distance;
        public TextView item_res_category_category;
        public ImageView item_res_category_alipay;
    }

}
