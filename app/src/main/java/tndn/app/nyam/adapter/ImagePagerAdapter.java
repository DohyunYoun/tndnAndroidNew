package tndn.app.nyam.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import tndn.app.nyam.utils.LogHome;
import tndn.app.nyam.utils.PreferenceManager;
import tndn.app.nyam.widget.HeightWrappingViewPager;

public class ImagePagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    int[] mImages;
    String intentURL = "";

    public ImagePagerAdapter(Context context, int[] mImages) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mImages = mImages;
    }

    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(this.mImages[position]);
        ((HeightWrappingViewPager) container).addView(imageView, 0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {

                    new LogHome().send(mContext, "banner-food");
                    intentURL = "tndn://getStoreList?mainId=1";
                    PreferenceManager.getInstance(mContext).setLocalization("1");
                    PreferenceManager.getInstance(mContext).setFoodId("");
                    PreferenceManager.getInstance(mContext).setLocationId("");

                } else if (position == 1) {

                    new LogHome().send(mContext, "banner-weibo");
                    intentURL = "http://m.weibo.cn/d/tndn?jumpfrom=weibocom";
//                    PreferenceManager.getInstance(mContext).setSiteLocalization("1");

                } else if (position == 2) {
                    new LogHome().send(mContext, "banner-aidibao");
                    intentURL = "tndn://banner?id=aidibao";
                } else if (position == 3) {
                    new LogHome().send(mContext, "banner-rentcar");
                    intentURL = "tndn://banner?id=rentcar";
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentURL));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((HeightWrappingViewPager) container).removeView((ImageView) object);
    }
}