package com.aduinaja.aduinaja;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.aduinaja.application.MainApplication;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.ProgressView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import database.DataStore;

/**
 * Created by Triyandi on 03/02/2016.
 */
public class SplashScreen extends AppCompatActivity {

    ProgressView progress;
    DataStore db;
    JSONObject obj;

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splashscreen);
        ThemeManager.init(this, 2, 0, null);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, WelcomeScreen.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private class AssetsData extends AsyncTask<Void, Void, Void> {

        InputStream is;
        StringBuilder sb;
        String result = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.start();
            is = null;
            sb = null;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{

                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(MainApplication.urlGetKategori);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
                sb = new StringBuilder();
                sb.append(reader.readLine() + "\n");
                String line = "0";
                while((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();

            } catch (SQLiteException e){
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            db.deleteKategori();
            try {
                Log.i("JSON", "> " + result);
                JSONObject dataObj = new JSONObject(result);
                if(dataObj.getInt("status") == 2){
                    JSONArray data = dataObj.getJSONArray("data");
                    for(int i = 0; i < data.length(); i++){
                        db.insertKategori(data.getJSONObject(i).getInt("id"),
                                data.getJSONObject(i).getString("nama"),data.getJSONObject(i).getString("slug"),data.getJSONObject(i).getString("deskripsi"),data.getJSONObject(i).getString("status"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                Toast.makeText(SplashScreen.this, "Maaf terjadi kesalahan dengan jaringan Anda. Silakan coba beberapa saat lagi", Toast.LENGTH_LONG).show();
                SplashScreen.this.finish();
            }
            progress.stop();
            startActivity(new Intent(SplashScreen.this, WelcomeScreen.class));
            finish();
        }
    }

}
