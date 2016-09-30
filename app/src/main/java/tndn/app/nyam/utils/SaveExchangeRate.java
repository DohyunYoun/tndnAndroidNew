package tndn.app.nyam.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by YounDit on 2016-02-16.
 */
public class SaveExchangeRate {


    public void saveExchangeRate(final Context context) {
        if (new IsOnline().onlineCheck(context)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // call API by using HTTPURLConnection
                        URL url = new URL(new TDUrls().currencyURL);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setConnectTimeout(5000);
                        urlConnection.setReadTimeout(1000);

                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        JSONObject res = new JSONObject(getStringFromInputStream(in));

                        // parse JSON
                        String curr = res.getString("nowCurrency");
                        String date = res.getString("currrencyTime");
                        PreferenceManager.getInstance(context).setCurrency(curr);
                        PreferenceManager.getInstance(context).setCurrDate(date);

                    } catch (MalformedURLException e) {
                        System.err.println("Malformed URL");
                        e.printStackTrace();
                        PreferenceManager.getInstance(context).setCurrency("165");
                        PreferenceManager.getInstance(context).setCurrDate("- 00:00");

                    } catch (JSONException e) {
                        System.err.println("JSON parsing error");
                        e.printStackTrace();
                        PreferenceManager.getInstance(context).setCurrency("165");
                        PreferenceManager.getInstance(context).setCurrDate("- 00:00");
                    } catch (IOException e) {
                        System.err.println("URL Connection failed");
                        e.printStackTrace();
                        PreferenceManager.getInstance(context).setCurrency("165");
                        PreferenceManager.getInstance(context).setCurrDate("- 00:00");
                    }
                }
            });
            thread.start();

            //http://www.tndn.net/#/map/33.505373/126.525912/33.495603/126.523251/3

            try {
                thread.join();  // 쓰레드 작업 끝날때까지 다른 작업들은 대기
            } catch (Exception e) {
            }
        } else {                   //internet check failed start
            PreferenceManager.getInstance(context).setCurrency("165");
            PreferenceManager.getInstance(context).setCurrDate("- 00:00");
        }

    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}