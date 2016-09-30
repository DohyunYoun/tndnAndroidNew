package tndn.app.nyam.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import tndn.app.nyam.R;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.TDUrls;
import tndn.app.nyam.widget.HeightWrappingViewPager;

public class NetworkImagePagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<Integer> mImages;

    String what;

    /**
     * imageloader using volley
     * viewHolder.item_res_category_imageview.setImageUrl(url + list.get(position).getIdx_imageFilePath(), mImageLoader);
     * res_info_imageview.setImageUrl(imgurl + shop.getIdx_imageFilePath().get(0), mImageLoader);
     */
    private String url = new TDUrls().getImageURL + "?size=350";

    ImageLoader mImageLoader;

    public NetworkImagePagerAdapter(Context context, ArrayList<Integer> mImages, String what) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mImages = mImages;
        this.what = what;
        mImageLoader = AppController.getInstance().getImageLoader();


    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final NetworkImageView imageView = new NetworkImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        if (mImages.size() == 0 || mImages.get(position) == 0 || mImages.get(position).equals("")) {
            if (what.equals("food")) {
                imageView.setImageResource(R.mipmap.noimg_big_food);
            } else {
                imageView.setImageResource(R.mipmap.noimg_big_site);
            }
        } else {
            imageView.setImageUrl(url + "&id=" + mImages.get(position), mImageLoader);
        }

        ((HeightWrappingViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((HeightWrappingViewPager) container).removeView((ImageView) object);
    }
}