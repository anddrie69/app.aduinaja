package com.aduinaja.aduinaja;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.rey.material.widget.TextView;

/**
 * Created by Triyandi on 05/02/2016.
 */
public class About extends Fragment {

    private static final String KEY_TITLE = "title";

    public About() {
    }

    public static About newInstance(String title) {
        About f = new About();

        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        f.setArguments(args);

        return (f);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about, container, false);

        ImageView ig = (ImageView) view.findViewById(R.id.ig);
        ImageView fb = (ImageView) view.findViewById(R.id.fb);
        ImageView tw = (ImageView) view.findViewById(R.id.tw);
        TextView versi = (TextView) view.findViewById(R.id.versi);

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            versi.setText("Versi " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(About.this.getContext(), "Coming soon", Toast.LENGTH_LONG).show();
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/AduinAJa"));
                startActivity(browserIntent);
            }
        });

/*
        final Tracker t = ((MainApplication) getActivity().getApplication())
                .getTracker(MainApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Tentang Aduin AJa");
        t.send(new HitBuilders.AppViewBuilder().build());*/

        return view;
    }

}
