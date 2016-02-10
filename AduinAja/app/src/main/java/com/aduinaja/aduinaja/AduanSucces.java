package com.aduinaja.aduinaja;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.rey.material.widget.Button;

/**
 * Created by Triyandi on 05/02/2016.
 */
public class AduanSucces extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aduan_success);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        final Drawable upArrow = ContextCompat.getDrawable(this,R.drawable.ic_ab_up_compat);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnLagi = (com.rey.material.widget.Button)findViewById(R.id.btn_aduan_lain);
        Button btnKembali = (Button)findViewById(R.id.btn_kembali);

        btnLagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Aduin.class);
                startActivity(intent);
                finish();
            }
        });

        /*final Tracker t = ((MainApplication) getApplication())
                .getTracker(MainApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Halaman Aduan Sukses");
        t.send(new HitBuilders.AppViewBuilder().build());*/

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (applyMenuOption(item));
    }

    private boolean applyMenuOption(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }

    /*@Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GoogleAnalytics.getInstance(AduanSucces.this)
                .reportActivityStart(this);
    }*/
/*
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GoogleAnalytics.getInstance(AduanSucces.this).reportActivityStop(this);
    }*/

}
