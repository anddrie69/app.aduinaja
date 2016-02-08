package com.aduinaja.aduinaja;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aduinaja.application.MainApplication;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Triyandi on 08/02/2016.
 */
public class VerifikasiNIK extends AppCompatActivity {

    EditText nik, nama;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ThemeManager.init(this, 2, 0, null);
        //Setting up the toolbar
        //Toolbar toolBar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(toolBar);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // Obtain the shared Tracker instance.
        /*final Tracker t = ((MainApplication) getApplication())
                .getTracker(MainApplication.TrackerName.APP_TRACKER);*/
        t.setScreenName("Verifikasi");
        t.send(new HitBuilders.AppViewBuilder().build());

        pref = getApplicationContext().getSharedPreferences("AtadResu", MODE_PRIVATE);
        editor = pref.edit();

        if(pref.contains("nik") && pref.contains("nama")){
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
            finish();
        }

        nik = (EditText)findViewById(R.id.txtNIK);
        nama = (EditText)findViewById(R.id.txtNama);

        checkNetwork();


        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, WelcomeScreen.class));
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                if(!nik.getText().toString().equals("") || !nama.getText().toString().equals("")) {
                    t.send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Login")
                            .build());
                    if(checkNetwork())
                        new Login().execute();
                }else{
                    DialogMessage("Masukkan terlebih dahulu NIK dan Nama Lengkap Anda.");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Login extends AsyncTask<Void, Void, Void> {

        String nikLogin, namaLogin;
        InputStream is;
        StringBuilder sb;
        String result = null;
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nikLogin = nik.getText().toString();
            namaLogin = nama.getText().toString();
            is = null;
            sb = null;
            loading = new ProgressDialog(MainActivity.this);
            loading.setTitle("Tunggu Sebentar");
            loading.setMessage("Melakukan Autentifikasi . . .");
            loading.setCancelable(false);
            loading.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("nik", nikLogin));
            nameValuePairs.add(new BasicNameValuePair("nama", StringHelper.upperCase(namaLogin)));
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(MainApplication.urlLogin);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);
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
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                DialogMessage("Terjadi masalah dengan sambungan internet Anda. Silakan coba beberapa saat.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loading.dismiss();
            try {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("AtadResu", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                Log.i("JSON", "> " + result);
                JSONObject dataObj = new JSONObject(result);
                Log.i("JSON"," status : "+dataObj.getInt("status"));
                if (dataObj.getInt("status") == 1) {
                    JSONObject data = dataObj.getJSONObject("data");
                    editor.putString("no_tps", data.getString("noTPS"));
                    editor.putString("nik", data.getString("nik"));
                    editor.putString("nama", data.getString("nama"));
                    editor.putString("tempat_lahir", data.getString("kel"));
                    //editor.putString("tgl_lahir", data.getString("tanggal lahir"));
                    editor.putString("tgl_lahir", "");
                    editor.putString("jk", data.getString("jenis_kelamin"));
                    editor.putString("alamat", data.getString("kel"));
                    //editor.putString("rt", data.getString("rt"));
                    //editor.putString("rw", data.getString("rw"));
                    editor.putString("rt", "");
                    editor.putString("rw", "");
                    editor.putString("dusun", "");
                    //editor.putString("dusun", data.getString("dusun"));
                    editor.putString("kelurahan", data.getString("kel"));
                    editor.putString("kecamatan", data.getString("kec"));
                    editor.putString("kabupaten_kota", data.getString("kab"));
                    editor.putString("provinsi", data.getString("pro"));
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                }else if(dataObj.getInt("status") == 0) {
                    //Toast.makeText(MainActivity.this,"Maaf Login gagal. Silakan coba beberapa saat lagi.", Toast.LENGTH_LONG).show();
                    DialogMessage("Maaf Login gagal. Silakan coba beberapa saat lagi.");
                }else if(dataObj.getInt("status") == 2) {
                    //Toast.makeText(MainActivity.this,"Maaf Anda bukan warga Surabaya", Toast.LENGTH_LONG).show();
                    DialogMessage("Maaf Anda bukan warga Surabaya");
                }else {
                    //Toast.makeText(MainActivity.this,"Maaf Nama yang Anda masukkan tidak cocok!", Toast.LENGTH_LONG).show();
                    DialogMessage("Maaf Nama yang Anda masukkan tidak cocok!");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                DialogMessage("Maaf terjadi kesalahan dengan jaringan Anda. Silakan coba beberapa saat lagi");
            }
        }

    }

    private boolean checkNetwork(){
        Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo()!=null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()){
            return true;
        }else{
            builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog){
                @Override
                public void onPositiveActionClicked(DialogFragment fragment) {
                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    startActivity(intent);
                    super.onPositiveActionClicked(fragment);
                }

                @Override
                public void onNegativeActionClicked(DialogFragment fragment) {
                    //Toast.makeText(getBaseContext(), "Disagreed", Toast.LENGTH_SHORT).show();
                    super.onNegativeActionClicked(fragment);
                }
            };

            ((SimpleDialog.Builder)builder).message("Aplikasi ini membutuhkan akses ke jaringan Anda, silakan nyalakan terlebih dahulu.")
                    .title("Nyalakan Jaringan")
                    .positiveAction("Nyalakan")
                    .negativeAction("Tolak");

            DialogFragment fragment = DialogFragment.newInstance(builder);
            fragment.show(getSupportFragmentManager(), null);
            return false;
        }
    }

    private void DialogMessage(String pesan){
        Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

        builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog){
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                super.onPositiveActionClicked(fragment);
            }
        };

        ((SimpleDialog.Builder) builder).message(pesan)
                .title("Kesalahan")
                .positiveAction("OK");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

   /* @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GoogleAnalytics.getInstance(MainActivity.this)
                .reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GoogleAnalytics.getInstance(MainActivity.this).reportActivityStop(this);
    }*/

}
