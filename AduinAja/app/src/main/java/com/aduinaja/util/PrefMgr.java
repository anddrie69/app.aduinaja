package com.aduinaja.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Triyandi on 12/02/2016.
 */
public class PrefMgr {

    private static final String NIK = "NIK";
    private static final String NAMA = "NAMA";
    private static final String IDFB = "IDFB";

    private static PrefMgr singleton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static PrefMgr getInstance(){
        if(singleton == null){
            singleton = new PrefMgr();
        }
        return singleton;
    }

    public void init(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void savePreference(String key, Object object){
        if(object instanceof String){
            editor.putString(key, (String) object);
        }
        editor.commit();
    }

    public void saveNik(String nik){
        savePreference(NIK, nik);
    }

    public void saveIdFb(String idFb){
        savePreference(IDFB, idFb);
    }

    public String getIdFb(){
        return sharedPreferences.getString(IDFB, null);
    }

    public String getNik(){
        return sharedPreferences.getString(NIK, null);
    }

    public void saveNama(String nama){
        savePreference(NAMA, nama);
    }

    public String getNama(){
        return sharedPreferences.getString(NAMA, null);
    }
}
