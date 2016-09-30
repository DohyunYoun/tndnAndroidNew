package tndn.app.nyam.utils;

import android.content.Context;

import java.text.DecimalFormat;

import tndn.app.nyam.R;

public class MakePricePretty {

    public String makePricePretty(Context context, String price, boolean what) {
        //true : kor
        //false : chn
        String won_kor = context.getResources().getString(R.string.curr_kor) + " ";
        String won_chn = context.getResources().getString(R.string.curr_chn) + " ";
        if (price==null ||price.equals("")) {
            return "0";
        }else {
            double value = Double.parseDouble(price);
            DecimalFormat format = new DecimalFormat("###,###.##");
            String result = format.format(value);

            if (what) {
                return  won_kor + result;
            } else if (!what) {
                if (PreferenceManager.getInstance(context).getCurrency().equals("") || PreferenceManager.getInstance(context).getCurrency().equals("0.00")) {
                    new SaveExchangeRate().saveExchangeRate(context);
                }
                //exchange rate adapt?
                result = format.format(value / Double.parseDouble(PreferenceManager.getInstance(context).getCurrency()));

                return won_chn + result;

            } else {
                return "0";
            }
        }
    }
}
