package tndn.app.nyam.utils;

import android.content.Context;

import java.text.DecimalFormat;

public class MakePaymentPrice {

    public String makePaymentPrice(String price, Context context, double rate) {
        //원화*0.98/(위안화송금받을때-1)/0.97
        //20161227 변경 value*0.99  / (rate - 2) / 0.97;
        double value = Double.parseDouble(price);

        DecimalFormat format = new DecimalFormat("###,###.##");
        value = value  / (rate - 2) / 0.97;

        //20161227
        String result = format.format(value);

        return String.valueOf(result);
    }

}
