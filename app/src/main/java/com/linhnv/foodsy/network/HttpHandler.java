package com.linhnv.foodsy.network;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by linhnv on 17/06/2017.
 */

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();
    public HttpHandler(){

    }
    public String makeServiceCall(String reqUrl){
        String reponse = null;
        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //read the reponse
            InputStream in = new BufferedInputStream(conn.getInputStream());
            reponse = convertStreamToString(in);
        }catch(MalformedURLException e){
            Log.e(TAG, "MalfromedURLException" +e.getMessage());
        }catch (ProtocolException e){
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        }catch (IOException e){
            Log.e(TAG, "IOException: " + e.getMessage());
        }catch (Exception e){
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return reponse;
    }
    private String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try{
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public String getPostDataString(JSONObject params) throws Exception{
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
