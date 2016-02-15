package com.aduinaja.aduinaja;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.aduinaja.application.MainApplication;
import card.CardAdapter;
import card.DataLaporan;

/**
 * Created by Triyandi on 06/02/2016.
 */
public class LihatAduan extends Fragment {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    SwipeRefreshLayout refresh;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final String KEY_TITLE = "title";


    public LihatAduan() {
    }

    public static LihatAduan newInstance(String title){
        LihatAduan f = new LihatAduan();

        Bundle args = new Bundle();
        //args.putString(KEY_TITLE, title);
        f.setArguments(args);

        return (f);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_aduan, container, false);

        /*Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"Museo_Slab_2.otf");
        TextView title = (TextView)toolbar.findViewById(R.id.toolbar_title);
        title.setText("aduinaja");
        title.setTypeface(tf);*/

        /*pref = getActivity().getApplicationContext().getSharedPreferences("AtadResu", getActivity().MODE_PRIVATE);
        editor = pref.edit();*/

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        refresh = (SwipeRefreshLayout)view.findViewById(R.id.refresh);
        refresh.setColorSchemeResources(R.color.diterima, R.color.ditolak);

        /*mAdapter = new CardAdapter(getContext(),pref.getString("nik",""),pref.getString("nama",""));
        mRecyclerView.setAdapter(mAdapter);*/
        checkNetwork();

        TelephonyManager tMgr = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        final String imei = tMgr.getDeviceId();

        new getLaporan(getContext(),getArguments().getString("category")).execute();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkNetwork();

                new getLaporan(getContext(),getArguments().getString("category")).execute();
            }
        });

        /*final Tracker t = ((MainApplication) getActivity().getApplication())
                .getTracker(MainApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Halaman Lihat Laporan");
        t.send(new HitBuilders.AppViewBuilder().build());*/

        return view;
    }

    private class getLaporan extends AsyncTask<Void, Void, Void> {

        InputStream is;
        StringBuilder sb;
        String result = null;
        ProgressDialog loading;
        String category;
        //DataStore db;

        public getLaporan(Context ctx, String category) {
            this.category = category;
            loading = new ProgressDialog(ctx);
            loading.setTitle("Tunggu Sebentar");
            loading.setMessage("Mengambil Data . . .");
            loading.setCancelable(false);
            if(!refresh.isRefreshing())
                loading.show();
            refresh.setRefreshing(true);

            //db = new DataStore(ctx);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            is = null;
            sb = null;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(MainApplication.urlGetLaporan);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try{
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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                Toast.makeText(getContext(), "Ada masalah dengan jaringan Anda, silakan coba lagi", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                Log.i("JSON", "> " + result);

                JSONObject dataObj = new JSONObject(result);
                if(dataObj.getString("status").equals("2")) {
                    JSONArray data = dataObj.getJSONArray("data");

                    ArrayList results = new ArrayList<DataLaporan>();
                    String waktu = "";
                    for (int i = 0; i < data.length(); i++) {

                        String rawDate = data.getJSONObject(i).getJSONObject("aduan").getString("tanggal_aduan").split(" ")[0];
                        String rawTime = data.getJSONObject(i).getJSONObject("aduan").getString("tanggal_aduan").split(" ")[1];
                        String[] date = rawDate.split("-");
                        String[] time = rawTime.split(":");
                        if((Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - Integer.parseInt(date[2])) <= 0) {
                            if ((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - Integer.parseInt(time[0])) <= 0) {
                                if ((Calendar.getInstance().get(Calendar.MINUTE) - Integer.parseInt(time[1])) <= 0) {
                                    waktu = "Baru saja";
                                } else {
                                    waktu = String.valueOf((Calendar.getInstance().get(Calendar.MINUTE) - Integer.parseInt(time[1]))) + " menit yang lalu";
                                }
                            } else {
                                waktu = String.valueOf((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - Integer.parseInt(time[0])) + " jam yang lalu");
                            }
                        }else if((Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - Integer.parseInt(date[2])) <= 7){
                            waktu = String.valueOf((Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - Integer.parseInt(date[2])))+" hari yang lalu";
                        }else if((Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - Integer.parseInt(date[2])) <= 28){
                            waktu = String.valueOf((Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - Integer.parseInt(date[2])) / 7)+" minggu yang lalu";
                        }else{
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                            Date tgl = null;
                            String str = null;
                            try{
                                tgl = inputFormat.parse(rawDate);
                                str = outputFormat.format(tgl);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            waktu = str;
                        }

                        if(data.getJSONObject(i).getJSONObject("aduan").getString("nama_category").equals(category)) {

                            DataLaporan obj = new DataLaporan(
                                    data.getJSONObject(i).getJSONObject("aduan").getString("foto"),
                                    data.getJSONObject(i).getJSONObject("aduan").getString("nama_member"),
                                    data.getJSONObject(i).getJSONObject("aduan").getString("tanggal_aduan"),
                                    data.getJSONObject(i).getJSONObject("aduan").getString("img"),
                                    data.getJSONObject(i).getJSONObject("aduan").getString("judul"),
                                    data.getJSONObject(i).getJSONObject("aduan").getString("deskripsi"),
                                    data.getJSONObject(i).getJSONObject("aduan").getString("status"),
                                    data.getJSONObject(i).getString("up_vote"),
                                    data.getJSONObject(i).getString("down_vote"),
                                    data.getJSONObject(i).getString("comments"),
                                    data.getJSONObject(i).getJSONObject("aduan").getString("nama_category"),
                                    LihatAduan.this.getContext()
                            );
                            results.add(obj);
                        }
                    }
                    mAdapter = new CardAdapter(results);
                    mRecyclerView.setAdapter(mAdapter);
                    if(refresh.isRefreshing()){
                        refresh.setRefreshing(false);
                    }
                }else if(dataObj.getString("status").equals("2")){
                    DialogMessage("Maaf perangkat Smart Phone Anda tidak bisa melakukan laporan karena terdeteksi duplikat login. Hanya diperbolehkan menggunakan satu SmartPhone untuk satu akun.");
                }else{
                    //Toast.makeText(LihatAduan.this.getContext(),"Tidak ada data",Toast.LENGTH_LONG).show();
                    DialogMessage("Tidak ada data");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                DialogMessage("Maaf terjadi kesalahan dengan jaringan Anda. Silakan coba beberapa saat lagi");
            }

            loading.dismiss();
        }

    }

    private void checkNetwork(){
        Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo()!=null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()){

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
                    Toast.makeText(getContext(), "Disagreed", Toast.LENGTH_SHORT).show();
                    super.onNegativeActionClicked(fragment);
                }
            };

            ((SimpleDialog.Builder)builder).message("Aplikasi ini membutuhkan akses ke jaringan Anda, silakan nyalakan terlebih dahulu.")
                    .title("Nyalakan Jaringan")
                    .positiveAction("Nyalakan")
                    .negativeAction("Tolak");

            DialogFragment fragment = DialogFragment.newInstance(builder);
            fragment.show(getFragmentManager(), null);
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
        fragment.show(getActivity().getSupportFragmentManager(), null);
    }
}
