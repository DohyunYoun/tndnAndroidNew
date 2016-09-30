package tndn.app.nyam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import tndn.app.nyam.R;
import tndn.app.nyam.StoreMenuActivity;
import tndn.app.nyam.data.StoreMenuData;
import tndn.app.nyam.utils.AppController;
import tndn.app.nyam.utils.MakePricePretty;
import tndn.app.nyam.utils.TDUrls;

public class StoreMenuAdapter extends BaseAdapter {

    private String url = new TDUrls().getImageURL + "?size=150";

    private Context mcontext;
    private LayoutInflater mInflater;
    private StoreMenuActivity activity;
    private int input_type;

    ArrayList<StoreMenuData> list;

    int per_count = 0;

    //imageloader using volley
    ImageLoader mImageLoader;

    public StoreMenuAdapter(Context context, ArrayList<StoreMenuData> list, StoreMenuActivity activity, int input_type) {
        this.mcontext = context;
        this.list = new ArrayList<StoreMenuData>();
        this.list.addAll(list);
        this.activity = activity;
        this.input_type = input_type;
        mImageLoader = AppController.getInstance().getImageLoader();
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

        convertView = mInflater.inflate(R.layout.item_store_menu_lv, parent, false);

        viewHolder = new CustomViewHolder();

        //for menu
        viewHolder.item_res_menu_imageview = (NetworkImageView) convertView.findViewById(R.id.item_res_menu_imageview);
        viewHolder.item_res_menu_name_chn = (TextView) convertView.findViewById(R.id.item_res_menu_name_chn);
        viewHolder.item_res_menu_name_kor = (TextView) convertView.findViewById(R.id.item_res_menu_name_kor);
        viewHolder.item_res_menu_price_kor = (TextView) convertView.findViewById(R.id.item_res_menu_price_kor);
        viewHolder.item_res_menu_price_chn = (TextView) convertView.findViewById(R.id.item_res_menu_price_chn);
        viewHolder.item_res_menu_minus = (ImageButton) convertView.findViewById(R.id.item_res_menu_minus_button);
        viewHolder.item_res_menu_count = (TextView) convertView.findViewById(R.id.item_res_menu_count);
        viewHolder.item_res_menu_plus = (ImageButton) convertView.findViewById(R.id.item_res_menu_plus_button);

        //for cafe
        viewHolder.item_res_menu_cafe_imageview = (NetworkImageView) convertView.findViewById(R.id.item_res_menu_cafe_imageview);
        viewHolder.item_res_menu_cafe_name_chn = (TextView) convertView.findViewById(R.id.item_res_menu_cafe_name_chn);
        viewHolder.item_res_menu_cafe_name_kor = (TextView) convertView.findViewById(R.id.item_res_menu_cafe_name_kor);
        viewHolder.menu_cafe_order = (ImageView) convertView.findViewById(R.id.menu_cafe_order);
        viewHolder.menu_cafe_order_textview = (TextView) convertView.findViewById(R.id.menu_cafe_order_textview);

        viewHolder.item_menu_layout = (RelativeLayout) convertView.findViewById(R.id.item_menu_layout);
        viewHolder.item_cafe_layout = (RelativeLayout) convertView.findViewById(R.id.item_cafe_layout);

        if (input_type == 10) {
            //카페일때
            if (list.get(position).getMenu_category().equals("1")) {
                viewHolder.item_menu_layout.setVisibility(View.GONE);
                viewHolder.item_cafe_layout.setVisibility(View.VISIBLE);
                // Set the result into ImageView
                if (list.get(position).getIdx_image_file_path() == 0) {
                    viewHolder.item_res_menu_cafe_imageview.setImageResource(R.mipmap.noimg_food);
                } else {
                    viewHolder.item_res_menu_cafe_imageview.setImageUrl(url + "&id=" + list.get(position).getIdx_image_file_path(), mImageLoader);
                }

                viewHolder.item_res_menu_cafe_name_chn.setText(list.get(position).getMenu_name_chn());
                viewHolder.item_res_menu_cafe_name_kor.setText(list.get(position).getMenu_name_kor());


                viewHolder.menu_cafe_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.openCafeCheck(true, position);
                    }
                });
                viewHolder.menu_cafe_order_textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.openCafeCheck(true, position);
                    }
                });


                //冰 热
                if (list.get(position).getCount() != 0) {
                    //hot이 한개라도 주문이 됨
                    if (list.get(position).getCountForIce() != 0) {
                        //hot주문 됨, ice 주문됨
                        viewHolder.menu_cafe_order.setVisibility(View.GONE);
                        viewHolder.menu_cafe_order_textview.setVisibility(View.VISIBLE);
                        viewHolder.menu_cafe_order_textview.setText("冰" + list.get(position).getCountForIce() + " / 热" + list.get(position).getCount());

                    } else {
                        //hot주문됨, ice 주문안됨
                        viewHolder.menu_cafe_order.setVisibility(View.GONE);
                        viewHolder.menu_cafe_order_textview.setVisibility(View.VISIBLE);
                        viewHolder.menu_cafe_order_textview.setText("热" + list.get(position).getCount());

                    }
                } else {
                    //hot이 하나도 주문되지 않음
                    if (list.get(position).getCountForIce() != 0) {
                        //hot주문 안됨, ice 주문됨
                        viewHolder.menu_cafe_order.setVisibility(View.GONE);
                        viewHolder.menu_cafe_order_textview.setVisibility(View.VISIBLE);
                        viewHolder.menu_cafe_order_textview.setText("冰" + list.get(position).getCountForIce());

                    } else {
                        //hot주문 안됨, ice 주문안됨
                        viewHolder.menu_cafe_order.setVisibility(View.VISIBLE);
                        viewHolder.menu_cafe_order_textview.setVisibility(View.GONE);

                    }
                }
            } else {
                //카페인데 브라우니처럼 기본메뉴가 들어가 있을때
                //카페가 아닐때
                viewHolder.item_menu_layout.setVisibility(View.VISIBLE);
                viewHolder.item_cafe_layout.setVisibility(View.GONE);

                if (list.get(position).getIdx_image_file_path() == 0) {
                    viewHolder.item_res_menu_imageview.setImageResource(R.mipmap.noimg_food);
                } else {
                    viewHolder.item_res_menu_imageview.setImageUrl(url + list.get(position).getIdx_image_file_path(), mImageLoader);
                }

//        viewHolder.item_res_menu_name_chn.setText(list.get(position).getIdx_imageFilePath()+"_"+list.get(position).getMenuCHNName());
                viewHolder.item_res_menu_name_chn.setText(list.get(position).getMenu_name_chn());
                viewHolder.item_res_menu_name_kor.setText(list.get(position).getMenu_name_kor());
                viewHolder.item_res_menu_price_kor.setText(new MakePricePretty().makePricePretty(mcontext, list.get(position).getMenu_price(), true));
                viewHolder.item_res_menu_price_chn.setText(new MakePricePretty().makePricePretty(mcontext, list.get(position).getMenu_price(), false));
                viewHolder.item_res_menu_count.setText(list.get(position).getCount() + "");
                if (list.get(position).getCount() != 0) {
                    viewHolder.item_res_menu_count.setTextColor(mcontext.getResources().getColor(R.color.tndn_pink));
                } else {
                    viewHolder.item_res_menu_count.setTextColor(mcontext.getResources().getColor(R.color.gray_9b));
                }

                viewHolder.item_res_menu_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (list.get(position).getMenu_price().equals(0)) {
                            return;
                        }
                        per_count = list.get(position).getCount();
                        if (per_count > 29) {
                            return;
                        } else {
                            per_count++;
                            viewHolder.item_res_menu_count.setText(per_count + "");

                            list.get(position).setCount(per_count);
                            activity.setTotalCount(true, Integer.parseInt(list.get(position).getMenu_price()));

                            if (list.get(position).getCount() != 0) {
                                viewHolder.item_res_menu_count.setTextColor(mcontext.getResources().getColor(R.color.tndn_pink));
                            } else {
                                viewHolder.item_res_menu_count.setTextColor(mcontext.getResources().getColor(R.color.gray_9b));
                            }
                        }
                    }
                });

                viewHolder.item_res_menu_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (list.get(position).getMenu_price().equals(0)) {
                            return;
                        }
                        per_count = list.get(position).getCount();
                        if (per_count <= 0) {
                            return;
                        } else {
                            per_count--;
                            viewHolder.item_res_menu_count.setText(per_count + "");

                            list.get(position).setCount(per_count);
                            activity.setTotalCount(false, Integer.parseInt(list.get(position).getMenu_price()));

                            if (list.get(position).getCount() != 0) {
                                viewHolder.item_res_menu_count.setTextColor(mcontext.getResources().getColor(R.color.tndn_pink));
                            } else {
                                viewHolder.item_res_menu_count.setTextColor(mcontext.getResources().getColor(R.color.gray_9b));
                            }
                        }
                    }
                });

            }
        } else {
            //카페가 아닐때
            viewHolder.item_menu_layout.setVisibility(View.VISIBLE);
            viewHolder.item_cafe_layout.setVisibility(View.GONE);
            if (list.get(position).getIdx_image_file_path() == 0) {
                viewHolder.item_res_menu_imageview.setImageResource(R.mipmap.noimg_food);
            } else {
                viewHolder.item_res_menu_imageview.setImageUrl(url + list.get(position).getIdx_image_file_path(), mImageLoader);
            }

//        viewHolder.item_res_menu_name_chn.setText(list.get(position).getIdx_imageFilePath()+"_"+list.get(position).getMenuCHNName());
            viewHolder.item_res_menu_name_chn.setText(list.get(position).getMenu_name_chn());
            viewHolder.item_res_menu_name_kor.setText(list.get(position).getMenu_name_kor());
            viewHolder.item_res_menu_price_kor.setText(new MakePricePretty().makePricePretty(mcontext, list.get(position).getMenu_price(), true));
            viewHolder.item_res_menu_price_chn.setText(new MakePricePretty().makePricePretty(mcontext, list.get(position).getMenu_price(), false));
            viewHolder.item_res_menu_count.setText(list.get(position).getCount() + "");

            if (list.get(position).getCount() != 0) {
                viewHolder.item_res_menu_count.setTextColor(mcontext.getResources().getColor(R.color.tndn_pink));
            } else {
                viewHolder.item_res_menu_count.setTextColor(mcontext.getResources().getColor(R.color.gray_9b));
            }

            viewHolder.item_res_menu_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (list.get(position).getMenu_price().equals(0)) {
                        return;
                    }
                    per_count = list.get(position).getCount();
                    if (per_count > 29) {
                        return;
                    } else {
                        per_count++;
                        viewHolder.item_res_menu_count.setText(per_count + "");

                        list.get(position).setCount(per_count);
                        activity.setTotalCount(true, Integer.parseInt(list.get(position).getMenu_price()));

                        if (list.get(position).getCount() != 0) {
                            viewHolder.item_res_menu_count.setTextColor(mcontext.getResources().getColor(R.color.tndn_pink));
                        } else {
                            viewHolder.item_res_menu_count.setTextColor(mcontext.getResources().getColor(R.color.gray_9b));
                        }
                    }
                }
            });

            viewHolder.item_res_menu_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (list.get(position).getMenu_price().equals(0)) {
                        return;
                    }
                    per_count = list.get(position).getCount();
                    if (per_count <= 0) {
                        return;
                    } else {
                        per_count--;
                        viewHolder.item_res_menu_count.setText(per_count + "");

                        list.get(position).setCount(per_count);
                        activity.setTotalCount(false, Integer.parseInt(list.get(position).getMenu_price()));

                        if (list.get(position).getCount() != 0) {
                            viewHolder.item_res_menu_count.setTextColor(mcontext.getResources().getColor(R.color.tndn_pink));
                        } else {
                            viewHolder.item_res_menu_count.setTextColor(mcontext.getResources().getColor(R.color.gray_9b));
                        }
                    }
                }
            });


        }

        return convertView;

    }

    public class CustomViewHolder {

        public NetworkImageView item_res_menu_imageview;
        public TextView item_res_menu_name_chn;
        public TextView item_res_menu_name_kor;
        public TextView item_res_menu_price_kor;
        public TextView item_res_menu_price_chn;
        public ImageButton item_res_menu_minus;
        public TextView item_res_menu_count;
        public ImageButton item_res_menu_plus;

        public NetworkImageView item_res_menu_cafe_imageview;
        public TextView item_res_menu_cafe_name_chn;
        public TextView item_res_menu_cafe_name_kor;
        public ImageView menu_cafe_order;
        public TextView menu_cafe_order_textview;

        public RelativeLayout item_menu_layout;
        public RelativeLayout item_cafe_layout;
    }


}
