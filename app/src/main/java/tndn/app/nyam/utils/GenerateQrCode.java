package tndn.app.nyam.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.tndn.Contents;
import com.google.zxing.client.tndn.QRCodeEncoder;

/**
 * Created by ellen on 2017. 7. 3..
 */

public class GenerateQrCode {

    public Bitmap bitmap(String data) {
        int qrCodeDimention = 450;

        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(data, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimention);

        try {
            final Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
//            view.setImageBitmap(bitmap);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

}
