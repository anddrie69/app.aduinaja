package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by elmee on 05/11/2015.
 */
public class DataStore {

    public static final String DB_NAME = "storeAtad.db";
    public static final String TABLE_KATEGORI = "kategori";
    public static final String TABLE_TEPEES = "tsp";
    public static final String ROW_ID = "_id";
    public static final String ROW_NAMA = "nama";
    public static final String ROW_SLUG = "slug";
    public static final String ROW_DESKRIPSI = "deskripsi";
    public static final String ROW_STATUS = "status";

    public static final String ROW_JML_KEL = "jml_kel";
    public static final String ROW_KEC_ID = "kec_id";
    public static final String ROW_JML_TPS = "jml_tps";
    public static final String ROW_KEL_ID = "kel_id";

    private static final int DB_VERSION = 1;
    // mendeklarasikan CREATE_TABLE = MEMBUAT TABLE"
    private static final String CREATE_TABLE3 = "create table " + TABLE_KATEGORI
            + " (" + ROW_ID + " integer PRIMARY KEY," + ROW_NAMA + " text , "+ ROW_SLUG + " text, "+ ROW_DESKRIPSI +" text, "+ ROW_STATUS +" text)";

    // membuat mendeklarasikan itu adalah context
    private final Context context;
    // membuat mendeklarasikan DatabaseOpenHelper itu adalah dbhelper
    private DatabaseOpenHelper dbhelper;
    // membuat mendeklarasikan SQLiteDatabase itu adalah db
    private SQLiteDatabase db;

    // mengambil context untuk mengakses system di android
    public DataStore(Context ctx) {
        // mendeklarasikan ctx adalah context ( context context di ganti ctx )
        this.context = ctx;
        // membuat DatabaseOpenHelper
        dbhelper = new DatabaseOpenHelper(context);
        // menuliskan DatabaseOpenHelper = SQLiteDatabase
        db = dbhelper.getWritableDatabase();
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {
        // membuat database
        public DatabaseOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            /*db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_TABLE1);
            db.execSQL(CREATE_TABLE2);*/
            db.execSQL(CREATE_TABLE3);
            //4db.execSQL(CREATE_TABLE4);
        }

        // memperbarui database bila sudah ada
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
            onCreate(db);
        }
    }

    // menutup DatabaseOpenHelper
    public void close() {
        dbhelper.close();
    }


    public ArrayList<ArrayList<Object>> getKategori() {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<>();
        Cursor cur;
        try {
            cur = db.query(TABLE_KATEGORI, new String[]{ROW_ID, ROW_NAMA, ROW_SLUG, ROW_DESKRIPSI, ROW_STATUS}, null, null,
                    null, null, ROW_ID);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getInt(0));
                    dataList.add(cur.getString(1));
                    dataArray.add(dataList);

                } while (cur.moveToNext());

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("DEBE ERROR", e.toString());
        }
        return dataArray;

    }

    public String getKategoriById(int id) {
        Cursor cur;
        String result = null;
        try {
            cur = db.rawQuery("SELECT " + ROW_NAMA + " FROM " + TABLE_KATEGORI + " WHERE " + ROW_ID + " = " + id, null);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                result = cur.getString(0);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("DEBE ERROR", e.toString());
        }
        return result;

    }

    public void insertKategori(int id, String nama, String slug, String deskripsi, String status ) {

        ContentValues values = new ContentValues();
        values.put(ROW_ID, id);
        values.put(ROW_NAMA, nama);
        values.put(ROW_SLUG, slug);
        values.put(ROW_DESKRIPSI, deskripsi);
        values.put(ROW_STATUS, status);
        try {
            // menambahkan nama tabel bila tidak akan error
            // db.delete(NAMA_TABEL, null, null);
            db.insert(TABLE_KATEGORI, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void updateKategori(int id, String nama, String slug, String deskripsi, String status) {

        ContentValues values = new ContentValues();
        values.put(ROW_ID, id);
        values.put(ROW_NAMA, nama);
        values.put(ROW_SLUG, slug);
        values.put(ROW_DESKRIPSI, deskripsi);
        values.put(ROW_STATUS, status);
        try {
            // menambahkan nama tabel bila tidak akan error
            // db.delete(NAMA_TABEL, null, null);
            db.update(TABLE_KATEGORI, values, ROW_ID + " = " + id, null);
        } catch (Exception e) {
            // Log.e("DB ERROR", e.toString());
            // e.printStackTrace();
            Toast.makeText(null, "Sory, the name alredy exist",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void deleteKategori() {
        try {
            db.execSQL("delete from " + TABLE_KATEGORI);
            // Toast.makeText(null, "Berhasil", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
        }
    }

    /*public ArrayList<ArrayList<Object>> search(String name) {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cur;
        try {
            cur = db.rawQuery("select * from kontak where first_name like "
                    + name + " order by first_name", null);
			*//*
			 * (NAMA_TABEL, new String[] { ROW_ID, ROW_FIRST_NAME,
			 * ROW_LAST_NAME, ROW_LONGITUDE, ROW_LATITUDE, ROW_MORE}, null,
			 * null, null, null, ROW_MORE);
			 *//*
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getString(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getString(2));
                    dataList.add(cur.getString(3));
                    dataList.add(cur.getString(4));
                    dataArray.add(dataList);
                } while (cur.moveToNext());

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("DEBE ERROR", e.toString());
        }
        return dataArray;

    }
*/
}
