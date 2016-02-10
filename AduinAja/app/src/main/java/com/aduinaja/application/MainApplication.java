package com.aduinaja.application;

import android.app.Application;
import android.content.Context;


import java.util.HashMap;

/**
 * Created by elmee on 05/11/2015.
 */
public class MainApplication extends Application {

    public static String mainUrl = "http://adm.aduinaja.com";
    public static String urlAduan = mainUrl+"/backend/web/index.php?r=api/addnewaduan";
    public static String urlRegister= mainUrl+"/backend/web/index.php?r=api/register";
    public static String urlGetLaporan = mainUrl+"/backend/web/index.php?r=api/getaduan";
    public static String urlGetImages = mainUrl+"/backend/web/statics/aduan";
    public static String urlGetKategori = mainUrl+"/backend/web/index.php?r=api/getkategori";
    public static String urlVerifikasi = mainUrl+"/backend/web/index.php?r=api/verifikasinik&id=";
    private static MainApplication app;

    public static final String TAG = MainApplication.class.getSimpleName();

    public static synchronized MainApplication getInstance(){
        return app;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        app = this;
    }

    public static Context getAppContext() {
        return app;
    }

    public MainApplication() {
        super();
    }

 /*   // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-44827376-3";

 */   /**
     * Enum used to identify the tracker that needs to be used for tracking.
     *
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
    /* */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    /*HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();*/

   /* public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }*/

}
