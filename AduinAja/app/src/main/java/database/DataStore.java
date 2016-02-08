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
    public static final String TABLE_KECAMATAN = "kecamatan";
    public static final String TABLE_KELURAHAN = "kelurahan";
    public static final String TABLE_TPS = "tps";
    public static final String TABLE_KATEGORI = "kategori";
    public static final String TABLE_TEPEES = "tsp";
    public static final String ROW_ID = "_id";
    public static final String ROW_NAMA = "nama";
    public static final String ROW_JML_KEL = "jml_kel";
    public static final String ROW_KEC_ID = "kec_id";
    public static final String ROW_JML_TPS = "jml_tps";
    public static final String ROW_KEL_ID = "kel_id";

    private static final int DB_VERSION = 1;
    // mendeklarasikan CREATE_TABLE = MEMBUAT TABLE"
    private static final String CREATE_TABLE = "create table " + TABLE_KECAMATAN
            + " (" + ROW_ID + " integer PRIMARY KEY,"
            + ROW_NAMA + " text," + ROW_JML_KEL + " integer)";
    private static final String CREATE_TABLE1 = "create table " + TABLE_KELURAHAN
            + " (" + ROW_ID + " integer PRIMARY KEY,"
            + ROW_NAMA + " text," + ROW_KEC_ID + " integer)";
    private static final String CREATE_TABLE2 = "create table " + TABLE_TPS
            + " (" + ROW_ID + " integer PRIMARY KEY,"+ ROW_JML_TPS + " integer," + ROW_KEL_ID + " integer)";
    private static final String CREATE_TABLE3 = "create table " + TABLE_KATEGORI
            + " (" + ROW_ID + " integer PRIMARY KEY," + ROW_NAMA + " text)";
    private static final String CREATE_TABLE4 = "create table " + TABLE_TPS
            + " (" + ROW_ID + " integer PRIMARY KEY," + ROW_NAMA + " text," + ROW_JML_TPS + " integer," + ROW_KEL_ID + " integer)";
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
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_TABLE1);
            db.execSQL(CREATE_TABLE2);
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

    public boolean existKecamatan(String id) {
        Cursor cur;
        boolean ex = false;
        try {
            cur = db.rawQuery("select * from "+TABLE_KECAMATAN+" where "+ROW_ID+"='" + id
                    + "'", null);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                ex = true;
            } else {
                ex = false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("DEBE ERROR", e.toString());
        }
        return ex;
    }

    public void insertKecamatan(int id, String name, int jml_kel) {

        ContentValues values = new ContentValues();
        values.put(ROW_ID, id);
        values.put(ROW_NAMA, name);
        values.put(ROW_JML_KEL, jml_kel);
        try {
            // menambahkan nama tabel bila tidak akan error
            // db.delete(NAMA_TABEL, null, null);
            db.insert(TABLE_KECAMATAN, null, values);
        } catch (Exception e) {
             Log.e("DB ERROR", e.toString());
             e.printStackTrace();
        }
    }

    public void updateKecamatan(int id, String name, int jml_kel) {

        ContentValues values = new ContentValues();
        values.put(ROW_ID, id);
        values.put(ROW_NAMA, name);
        values.put(ROW_JML_KEL, jml_kel);
        try {
            // menambahkan nama tabel bila tidak akan error
            // db.delete(NAMA_TABEL, null, null);
            db.update(TABLE_KECAMATAN, values, ROW_ID + " = " + id, null);
        } catch (Exception e) {
            // Log.e("DB ERROR", e.toString());
            // e.printStackTrace();
            Toast.makeText(null, "Sory, the name alredy exist",
                    Toast.LENGTH_LONG).show();
        }
    }

    // membuat array pada table layout
    public ArrayList<ArrayList<Object>> getKecamatan() {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cur;
        try {
            cur = db.query(TABLE_KECAMATAN, new String[]{ROW_ID, ROW_NAMA,
                            ROW_JML_KEL}, null, null,
                    null, null, ROW_ID);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getInt(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getInt(2));
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

    public String getKecamatanById(int id) {
        Cursor cur;
        String result = null;
        try {
            cur = db.rawQuery("SELECT " + ROW_NAMA + " FROM " + TABLE_KECAMATAN + " WHERE " + ROW_ID + " = " + id, null);
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


    public void deleteKecamatan(int id) {
        try {
            db.execSQL("delete from " + TABLE_KECAMATAN + " where " + ROW_ID + " = "
                    + id);
            // Toast.makeText(null, "Berhasil", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
        }
    }

    public boolean existKelurahan(String id) {
        Cursor cur;
        boolean ex = false;
        try {
            cur = db.rawQuery("select * from " + TABLE_KELURAHAN + " where " + ROW_ID + "='" + id
                    + "'", null);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                ex = true;
            } else {
                ex = false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("DEBE ERROR", e.toString());
        }
        return ex;
    }

    public void insertKelurahan(int id, String name, int kec_id) {

        ContentValues values = new ContentValues();
        values.put(ROW_ID, id);
        values.put(ROW_NAMA, name);
        values.put(ROW_KEC_ID, kec_id);
        try {
            // menambahkan nama tabel bila tidak akan error
            // db.delete(NAMA_TABEL, null, null);
            db.insert(TABLE_KELURAHAN, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void updateKelurahan(int id, String name, int kec_id) {

        ContentValues values = new ContentValues();
        values.put(ROW_ID, id);
        values.put(ROW_NAMA, name);
        values.put(ROW_KEC_ID, kec_id);
        try {
            // menambahkan nama tabel bila tidak akan error
            // db.delete(NAMA_TABEL, null, null);
            db.update(TABLE_KELURAHAN, values, ROW_ID + " = " + id, null);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    // membuat array pada table layout
    public ArrayList<ArrayList<Object>> getKelurahan(int id_kec) {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cur;
        try {
            cur = db.rawQuery("SELECT "+ ROW_ID + "," + ROW_NAMA + "," + ROW_KEC_ID +
                    " FROM " + TABLE_KELURAHAN + " WHERE " + ROW_KEC_ID + " = " + id_kec,null);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getInt(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getInt(2));
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

    public String getKelurahanById(int id) {
        Cursor cur;
        String result = null;
        try {
            cur = db.rawQuery("SELECT " + ROW_NAMA + " FROM " + TABLE_KELURAHAN + " WHERE " + ROW_ID + " = " + id, null);
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

    public void deleteKelurahan(int id) {
        try {
            db.execSQL("delete from " + TABLE_KELURAHAN + " where " + ROW_ID + " = "
                    + id);
            // Toast.makeText(null, "Berhasil", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
        }
    }

    public boolean existTPS(String id) {
        Cursor cur;
        boolean ex = false;
        try {
            cur = db.rawQuery("select * from "+TABLE_TPS+" where "+ROW_ID+"='" + id
                    + "'", null);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                ex = true;
            } else {
                ex = false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("DEBE ERROR", e.toString());
        }
        return ex;
    }

    public void insertTPS(int id, int jml_tps, int kel_id) {

        ContentValues values = new ContentValues();
        values.put(ROW_ID, id);
        values.put(ROW_JML_TPS, jml_tps);
        values.put(ROW_KEL_ID, kel_id);
        try {
            // menambahkan nama tabel bila tidak akan error
            // db.delete(NAMA_TABEL, null, null);
            db.rawQuery("insert into " + TABLE_TPS + "(_id,jml_tps,kel_id) values (" + id + "," + jml_tps + "," + kel_id + ")", null);
            Log.i("id", "> " + "insert into " + TABLE_TPS + "(_id,jml_tps,kel_id) values (" + id + "," + jml_tps + "," + kel_id + ")");
            //db.insert(TABLE_TPS, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void updateTPS(int id, int jml_tps, int jml_lk, int jml_pr, int jml_both, int kel_id) {

        ContentValues values = new ContentValues();
        values.put(ROW_ID, id);
        values.put(ROW_JML_TPS, jml_tps);
        values.put(ROW_KEL_ID, kel_id);
        try {
            // menambahkan nama tabel bila tidak akan error
            // db.delete(NAMA_TABEL, null, null);
            db.update(TABLE_TPS, values, ROW_ID + " = " + id, null);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public int getJmlTPS(int id_kel){
        int jmlTPS = 0;
        Cursor cur;
        try{
            cur = db.rawQuery("SELECT " + ROW_JML_TPS + " FROM " + TABLE_TPS + " WHERE " + ROW_KEL_ID + " = " + id_kel, null);
            cur.moveToFirst();
            if(!cur.isAfterLast()){
                jmlTPS = cur.getInt(0);
            }
        }catch (SQLiteException e){
            e.printStackTrace();
        }
        return  jmlTPS;
    }

    // membuat array pada table layout
    public ArrayList<ArrayList<Object>> getTPS() {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cur;
        try {
            cur = db.query(TABLE_TPS, new String[]{ROW_ID, ROW_JML_TPS,
                            ROW_KEL_ID}, null, null,
                    null, null, ROW_ID);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getInt(0));
                    dataList.add(cur.getInt(1));
                    dataList.add(cur.getInt(2));
                    dataList.add(cur.getInt(3));
                    dataList.add(cur.getInt(4));
                    dataList.add(cur.getInt(5));
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

    public void deleteAll() {
        try {
            db.execSQL("delete from " + TABLE_KECAMATAN + ", "+ TABLE_KELURAHAN + ", "+ TABLE_TPS);
            // Toast.makeText(null, "Berhasil", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
        }
    }

    public String getKecamatanId(String name){
        Cursor cur;
        String categories = "";
        try{
            cur = db.rawQuery("SELECT " + ROW_ID + " FROM " + TABLE_KECAMATAN + " WHERE " + ROW_NAMA + " = " + name, null);
            cur.moveToFirst();
            if(!cur.isAfterLast()){
                categories = cur.getString(0);
            }
        }catch(SQLException e){
            e.printStackTrace();
            Log.e("DEBE ERROR", e.toString());
        }
        return categories;
    }

    public void insertKategori(int id, String nama) {

        ContentValues values = new ContentValues();
        values.put(ROW_ID, id);
        values.put(ROW_NAMA, nama);
        try {
            // menambahkan nama tabel bila tidak akan error
            // db.delete(NAMA_TABEL, null, null);
            db.insert(TABLE_KATEGORI, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void insertSPT(int id, String nama, int jml_tps, int id_kel) {

        ContentValues values = new ContentValues();
        values.put(ROW_ID, id);
        values.put(ROW_NAMA, nama);
        values.put(ROW_JML_TPS, jml_tps);
        values.put(ROW_KEL_ID, id_kel);
        try {
            // menambahkan nama tabel bila tidak akan error
            // db.delete(NAMA_TABEL, null, null);
            db.insert(TABLE_TPS, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void deleteKategori() {
        try {
            db.execSQL("delete from " + TABLE_TPS);
            // Toast.makeText(null, "Berhasil", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e("ERROR", e.toString());
        }
    }

    public ArrayList<ArrayList<Object>> getKategori() {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<>();
        Cursor cur;
        try {
            cur = db.query(TABLE_KATEGORI, new String[]{ROW_ID, ROW_NAMA}, null, null,
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
