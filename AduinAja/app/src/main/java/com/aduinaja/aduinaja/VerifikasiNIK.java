package com.aduinaja.aduinaja;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.aduinaja.util.PrefMgr;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.io.IOException;
import java.io.InputStream;

import network.api.RetrofitApi;
import network.api.RetrofitApiSingleton;
import network.model.VerNik;
import retrofit2.Call;

/**
 * Created by Triyandi on 08/02/2016.
 */
public class VerifikasiNIK extends AppCompatActivity implements OnClickListener{

//    SharedPreferences pref;
//    SharedPreferences.Editor editor;

    private RetrofitApi api;

    private Button btnLogin;
    private EditText txtNik;

    private String nik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrefMgr.getInstance().init(this);

        setContentView(R.layout.verifikasi_nik);

        api = RetrofitApiSingleton.getInstance().getApi();

//        Call<List<Category>> call = api.getCategoryList();
//        try {
//            List<Category> categoryList = call.execute().body();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        initViews();
        initListeners();

        String nikPref = PrefMgr.getInstance().getNik();
        if(null != nikPref) {
//            if (nikPref.contains("nik")) {
//                Intent intent = new Intent(this, Aduin.class);
            Intent intent = new Intent(this, TabFragment.class);
                startActivity(intent);
                finish();
//            }
        }

//        pref = getApplicationContext().getSharedPreferences("AtadResu", MODE_PRIVATE);
//        editor = pref.edit();
//
//        if(pref.contains("nik")){
//            Intent intent = new Intent(this, Aduin.class);
//            startActivity(intent);
//            finish();
//        }

        checkNetwork();
    }

    private void initViews(){
        ThemeManager.init(this, 2, 0, null);
        //Setting up the toolbar
        /*Toolbar toolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
        // Obtain the shared Tracker instance.
        /*final Tracker t = ((MainApplication) getApplication())
                .getTracker(MainApplication.TrackerName.APP_TRACKER);*/
        /*t.setScreenName("Verifikasi");
        t.send(new HitBuilders.AppViewBuilder().build());*/

        btnLogin = (Button)findViewById(R.id.btnLogin);
        txtNik = (EditText)findViewById(R.id.txtNIK);
    }

    private void initListeners(){
        btnLogin.setOnClickListener(this);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == btnLogin){
            String nik = txtNik.getText().toString();
            if(!TextUtils.isEmpty(nik)){
                //t.send(new HitBuilders.EventBuilder()
                            /*.setCategory("Action")
                            .setAction("Login")
                            .build());*/
                if(checkNetwork()) {
                    new Login().execute(nik);
                }
            }else{
                DialogMessage("Masukkan terlebih dahulu NIK dan Nama Lengkap Anda.");
            }
        }
    }

    private class Login extends AsyncTask<String, Void, VerNik> {

        String nikLogin, namaLogin;
        InputStream is;
        StringBuilder sb;
        String result = null;
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            nikLogin = nik.getText().toString();
            is = null;
            sb = null;
            loading = new ProgressDialog(VerifikasiNIK.this);
            loading.setTitle("Tunggu Sebentar");
            loading.setMessage("Melakukan Autentifikasi . . .");
            loading.setCancelable(false);
            loading.show();
        }

        @Override
        protected VerNik doInBackground(String... params) {
            String nik = params[0];
            String idFb = PrefMgr.getInstance().getIdFb();
            Call<VerNik> callVerNik = api.getVerNIK(nik,idFb);
            VerNik verNik = null;
            try {
                verNik = callVerNik.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return verNik;
        }

        @Override
        protected void onPostExecute(VerNik verNik) {
            super.onPostExecute(verNik);
            loading.dismiss();
            if(null != verNik){
                if(verNik.getStatus().equals("2")){
                    PrefMgr.getInstance().saveNik(verNik.getVerNikData().getNik());
                    PrefMgr.getInstance().saveNik(verNik.getVerNikData().getNama());

                    Intent intent = new Intent(VerifikasiNIK.this, Aduin.class);
                    startActivity(intent);
                    finish();
                }else{
                    DialogMessage("Maaf NIK yang Anda masukkan salah!");
                }
            }else{
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
