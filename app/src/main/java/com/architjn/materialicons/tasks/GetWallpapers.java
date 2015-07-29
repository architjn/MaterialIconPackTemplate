package com.architjn.materialicons.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.architjn.materialicons.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by architjn on 15/07/15.
 */
public class GetWallpapers extends AsyncTask<Void, Void, Void> {

    public interface Callbacks {
        public void onListLoaded(String jsonResult);
    }

    private String url, jsonResult;
    private Callbacks callbacks;

    public GetWallpapers(Context context, Callbacks callbacks) {
        this.callbacks = callbacks;
        url = context.getResources().getString(R.string.wall_url);
    }

    @Override
    protected Void doInBackground(Void... z) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpclient.execute(httppost);
            jsonResult = inputStreamToString(response.getEntity().getContent())
                    .toString();
        } catch (ClientProtocolException e) {
            Log.e("e", "error1");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("e", "error2");
            e.printStackTrace();
        }
        return null;
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (callbacks != null)
            callbacks.onListLoaded(jsonResult);
        super.onPostExecute(aVoid);
    }

}
