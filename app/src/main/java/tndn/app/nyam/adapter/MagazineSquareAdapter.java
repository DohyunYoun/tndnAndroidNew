package tndn.app.nyam.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import tndn.app.nyam.R;
import tndn.app.nyam.data.StoreListData;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.TDUrls;

public class MagazineSquareAdapter extends BaseAdapter {

    private String url = new TDUrls().getImageURL + "?size=150";


    private LayoutInflater mInflater;
    private Context mcontext;


    ArrayList<StoreListData> list;


    //imageloader using volley
    ImageLoader mImageLoader;

    public MagazineSquareAdapter(Context context, ArrayList<StoreListData> list) {
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

        convertView = mInflater.inflate(R.layout.item_magazine_square, parent, false);
        viewHolder = new CustomViewHolder();

        viewHolder.item_magazine_square = (NetworkImageView) convertView.findViewById(R.id.item_magazine_square);


        // Set the result into ImageView
        if (list.get(position).getIdx_image_file_path() == 0) {
//            viewHolder.item_magazine_square.setImageResource(R.mipmap.noimg_site);
            //위에께 안돼서 s3에 강제로 넣어놓음. 150*150이미지
            if (list.get(position).getIdx_store_major_classify() == 3) {
                viewHolder.item_magazine_square.setImageUrl(url + "&id=7630", mImageLoader);
            } else {
                viewHolder.item_magazine_square.setImageUrl(url + "&id=7581", mImageLoader);
            }

        } else {
            viewHolder.item_magazine_square.setImageUrl(url + "&id=" + list.get(position).getIdx_image_file_path(), mImageLoader);
        }

        return convertView;

    }

    class CustomViewHolder {

        public NetworkImageView item_magazine_square;
    }

}
