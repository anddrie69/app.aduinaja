package com.aduinaja.aduinaja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.rey.material.app.ThemeManager;

/**
 * Created by Triyandi on 09/02/2016.
 */
public class BottomNav extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_tabs);
        ThemeManager.init(this, 2, 0, null);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        pref = getApplicationContext().getSharedPreferences("AtadResu", MODE_PRIVATE);
        editor = pref.edit();

        Bundle b = new Bundle();
        b.putString("key", "Simple");
        //b.putString("id", pref.getString("id",""));
        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("", ContextCompat.getDrawable(this, R.drawable.ic_timeline)),
                TabFragment.class, b);
        //
        b = new Bundle();
        System.out.print("hello git");
        b.putString("key", "Contacts");
        mTabHost.addTab(mTabHost.newTabSpec("contacts")
                .setIndicator("", ContextCompat.getDrawable(this, R.drawable.ic_add)), Aduin.class, b);
        b = new Bundle();
        b.putString("key", "Custom");
        mTabHost.addTab(mTabHost.newTabSpec("custom").setIndicator("", ContextCompat.getDrawable(this, R.drawable.ic_profile)),
                Profil.class, b);
        // setContentView(mTabHost);

        setTabColor(mTabHost);

        try {
            if (getIntent().getExtras().containsKey("feedback")) {
                mTabHost.setCurrentTab(1);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                setTabColor(mTabHost);
            }
        });

        /*if (checkPlayServices()) {

            Intent intent = new Intent(MainActivity.this, RegistrationIntentService.class);
            startService(intent);
        }*/
    }

    public static void setTabColor(FragmentTabHost tabhost) {
        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        {
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#ff22252a")); //unselected
        }
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#ff131416")); // selected
        tabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_OK){
            switch (resultCode){
                case 267:
                    if(mTabHost.getCurrentTab() == 1){
                        mTabHost.setCurrentTab(1);
                    }
                    break;
            }
        }
    }

  /*  private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        9000).show();
            } else {
                Log.i("Play Services", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }*/



}
