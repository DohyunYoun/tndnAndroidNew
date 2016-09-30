package tndn.app.nyam.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import tndn.app.nyam.R;

/**
 * Created by YounDH on 2016-07-01.
 */
public class SaveImagetoStorage {


    /**
     * Image  Save (input Bitmap -> saved file JPEG)
     * Writer intruder(Kwangseob Kim)
     * <p/>
     * if resource image file
     * Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.img_translate_popup);
     * new SaveImagetoStorage().saveImagetoStorage(bm, "tndn-qr");
     * <p/>
     * if get network url
     *
     */
    /**
     * resources
     */
    public void saveImagetoStorage(Context context, int resource_id, String name) {


        Calendar calendar = Calendar.getInstance();
        String realtime = calendar.getTime().toString();

        Bitmap bitmap = getImageFromResource(context, resource_id);
        String ex_storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        // Get Absolute Path in External Sdcard
        String foler_name = "/tndn/";
        String file_name = realtime + "_" + name + ".jpg";
        String string_path = ex_storage + foler_name;

        File file_path;
        try {
            file_path = new File(string_path);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }


            FileOutputStream out = new FileOutputStream(string_path + file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            Toast.makeText(context, context.getResources().getString(R.string.download_qr_image), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.parse("file://" + string_path + file_name);
            intent.setData(uri);
            context.sendBroadcast(intent);


        } catch (FileNotFoundException exception) {
            Log.e("FileNotFoundException", exception.getMessage());
        } catch (IOException exception) {
            Log.e("IOException", exception.getMessage());
        }
    }

    /**
     * @param context
     * @param strImageURL
     * @param name        url
     */
    public void saveImagetoStorage(Context context, String strImageURL, String name) {

        Bitmap bitmap = getImageFromURL(strImageURL);
        String ex_storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        // Get Absolute Path in External Sdcard
        String foler_name = "/tndn/";
        String file_name = name + ".jpg";
        String string_path = ex_storage + foler_name;

        File file_path;
        try {
            file_path = new File(string_path);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path + file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            Toast.makeText(context, context.getResources().getString(R.string.download_qr_image), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.parse("file://" + string_path + file_name);
            intent.setData(uri);
            context.sendBroadcast(intent);


        } catch (FileNotFoundException exception) {
            Log.e("FileNotFoundException", exception.getMessage());
        } catch (IOException exception) {
            Log.e("IOException", exception.getMessage());
        }
    }

    public void saveImagetoStorage(Context context, Bitmap bitmap, String name) {
//from resource


        Calendar calendar = Calendar.getInstance();
        String realtime = calendar.getTime().toString();

        String ex_storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        // Get Absolute Path in External Sdcard
        String foler_name = "/tndn/";
        String file_name = realtime + "_" + name + ".jpg";
        String string_path = ex_storage + foler_name;

        File file_path;
        try {
            file_path = new File(string_path);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }


            FileOutputStream out = new FileOutputStream(string_path + file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            Toast.makeText(context, context.getResources().getString(R.string.download_qr_image), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.parse("file://" + string_path + file_name);
            intent.setData(uri);
            context.sendBroadcast(intent);


        } catch (FileNotFoundException exception) {
            Log.e("FileNotFoundException", exception.getMessage());
        } catch (IOException exception) {
            Log.e("IOException", exception.getMessage());
        }
    }


    private Bitmap getImageFromResource(Context context, int resource_id) {
        Bitmap imgBitmap = null;

        try {
            imgBitmap = BitmapFactory.decodeResource(context.getResources(), resource_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgBitmap;
    }

    private Bitmap getImageFromURL(final String strImageURL) {
         Bitmap imgBitmap = null;
        try {
            URL url = new URL(strImageURL);
            URLConnection conn = url.openConnection();
            conn.connect();

            int nSize = conn.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
            bis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgBitmap;
    }


}
