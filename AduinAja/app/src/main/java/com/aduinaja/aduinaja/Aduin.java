package com.aduinaja.aduinaja;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aduinaja.application.MainApplication;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import database.DataStore;
import lib.BitmapHelper;
import lib.CameraIntentHelper;
import lib.CameraIntentHelperCallback;
import network.Base64;
import network.api.RetrofitApi;
import network.api.RetrofitApiSingleton;
import network.model.Aduan;
import network.model.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Triyandi on 02/02/2016.
 */
public class Aduin extends AppCompatActivity {

    private static final String TAG = Aduin.class.getSimpleName();

    final static int GALLERY_REQUEST_CODE = 893;
    final static int CAPTURE_REQUEST_CODE = 289;
    final static int ACCOUNT_REQUEST_CODE = 346;
    ImageView pic;
    Uri imageUri;
    private Bitmap bitmap;
    LinearLayout linear;
    private EditText etDeskripsi;
    private EditText etHeadline;
    //int kelurahan = 0, kecamatan = 0, tps = 0;
    DataStore db;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog loading;
    int kategori;
    private ProgressDialog progressBar;
    CameraIntentHelper mCameraIntentHelper;
    String filePath = null;
    Bitmap rotateBitmap;
    //Tracker t;

    private Button btnAduin;

    private List<Category.CategoryDatum> categoryDatumList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_aduan);

        RetrofitApiSingleton.getInstance().init("http://adm.aduinaja.com/");

        ThemeManager.init(this, 2, 0, null);

        //Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        /*setSupportActionBar(toolbar);*/
        //toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        final Drawable upArrow = ContextCompat.getDrawable(this,R.drawable.ic_ab_up_compat);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetCategory().execute(new Void[0]);

        progressBar = new ProgressDialog(this);

        pref = getApplicationContext().getSharedPreferences("AtadResu", MODE_PRIVATE);
        editor = pref.edit();

        /*t = ((MainApplication) getApplication())
                .getTracker(MainApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Halaman Pelaporan");
        t.send(new HitBuilders.AppViewBuilder().build());*/

        db = new DataStore(this);

        final Button btnKategori = (Button)findViewById(R.id.btn_kategori);


        pic = (ImageView)findViewById(R.id.pic);
        linear = (LinearLayout)findViewById(R.id.linear);
        etDeskripsi = (EditText)findViewById(R.id.deskripsi);
        btnAduin = (Button)findViewById(R.id.btnAduin);
        etHeadline = (EditText)findViewById(R.id.headline);

        checkNetwork();

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pic.setEnabled(false);
                Dialog.Builder builder = null;

                boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
                builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog) {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                            if (mCameraIntentHelper != null) {
                                mCameraIntentHelper.startCameraIntent();
                            }
                        } else {
                            Toast.makeText(Aduin.this, "Device Anda tidak memiliki fitur Kamera", Toast.LENGTH_LONG).show();
                        }
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        Intent gintent = new Intent();
                        gintent.setType("image/*");
                        gintent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(
                                Intent.createChooser(gintent, "Select Picture"),
                                GALLERY_REQUEST_CODE);
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.title("Pilih Sumber Foto")
                        .positiveAction("Ambil Foto")
                        .negativeAction("Gallery");

                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getSupportFragmentManager(), null);
                pic.setEnabled(true);
            }
        });


        btnKategori.setOnClickListener(new View.OnClickListener() {
            Dialog.Builder builder = null;
            boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

            @Override
            public void onClick(View v) {
                btnKategori.setEnabled(false);

                String[] categoryItemTitles = new String[categoryDatumList.size()];
                for(int i = 0 ; i < categoryDatumList.size() ; i++){
                    categoryItemTitles[i] = categoryDatumList.get(i).getNama();
                }

                builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog) {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        btnKategori.setText("Kategori : " + getSelectedValue());
//                        kategori = id[getSelectedIndex()];
                        kategori = Integer.parseInt(categoryDatumList.get(getSelectedIndex()).getId());
                        Log.i("kategori","> "+kategori);
                        //Toast.makeText(Laporkan1.this, "Kategori : " + getSelectedValue(), Toast.LENGTH_SHORT).show();
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        Toast.makeText(Aduin.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        super.onNegativeActionClicked(fragment);
                    }
                };

                ((SimpleDialog.Builder) builder).items(categoryItemTitles, 0)
                        .title("Kategori")
                        .positiveAction("Pilih")
                        .negativeAction("Batal");

                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getSupportFragmentManager(), null);
                btnKategori.setEnabled(true);
            }
        });

        btnAduin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Aduin.this, AduanSucces.class);
                startActivity(intent);
            }
        });

        setupCameraIntentHelper();

    }

    private void setupCameraIntentHelper() {
        mCameraIntentHelper = new CameraIntentHelper(this, new CameraIntentHelperCallback() {
            @Override
            public void onPhotoUriFound(Date dateCameraIntentStarted, Uri photoUri, int rotateXDegrees) {
                Log.i("PHOTO", photoUri.toString());

                rotateBitmap = BitmapHelper.readBitmap(Aduin.this, photoUri);
                if (rotateBitmap != null) {
                    rotateBitmap = BitmapHelper.shrinkBitmap(rotateBitmap, 300, rotateXDegrees);
                    pic.setImageBitmap(rotateBitmap);
                }
                filePath = photoUri.toString();
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, Aduin.this);
            }

            @Override
            public void onSdCardNotMounted() {
                Toast.makeText(getApplicationContext(), getString(R.string.error_sd_card_not_mounted), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCanceled() {
                Toast.makeText(getApplicationContext(), getString(R.string.warning_camera_intent_canceled), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCouldNotTakePhoto() {
                Toast.makeText(getApplicationContext(), getString(R.string.error_could_not_take_photo), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPhotoUriNotFound() {
                Toast.makeText(getApplicationContext(), getString(R.string.activity_camera_intent_photo_uri_not_found), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void logException(Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_sth_went_wrong), Toast.LENGTH_LONG).show();
                Log.d(getClass().getName(), e.getMessage());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        mCameraIntentHelper.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCameraIntentHelper.onRestoreInstanceState(savedInstanceState);
    }

    /*@Override
    protected void onResume() {
        Log.i(MainApplication.TAG, "Setting screen name: Halaman PeAduin");
        t.setScreenName("Halaman Pelaporan");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }*/

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "UnFair");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + "UnFair" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_laporan, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (applyMenuOption(item));
    }

    private boolean applyMenuOption(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.btnAduin:
                if(kategori != 0 && !etDeskripsi.getText().toString().equals("")) {
                    /*try {
                        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                                new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
                        startActivityForResult(intent,ACCOUNT_REQUEST_CODE);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }*/
                }else{
                    Toast.makeText(this,"Maaf, Anda harus melengkapi semua data yang ada", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;
        filePath = null;
        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = data.getData();
                    if(selectedImageUri != null){
                        String filemanagerstring = null;
                        try {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                // OI FILE Manager
                                filemanagerstring = DocumentsContract.getDocumentId(selectedImageUri);
                            }else{
                                filemanagerstring = selectedImageUri.getPath();
                            }
                            // MEDIA GALLERY
                            String selectedImagePath = getPath(selectedImageUri);

                            Log.i("Path", "> "+selectedImagePath + "or >"+filemanagerstring);

                            if (selectedImagePath != null) {
                                filePath = selectedImagePath;
                            } else if (filemanagerstring != null) {
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    String id = filemanagerstring.split(":")[1];
                                    String[] column = {MediaStore.Images.Media.DATA};
                                    String sel = MediaStore.Images.Media._ID + "=?";
                                    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{id}, null);
                                    int columnIndex = cursor.getColumnIndex(column[0]);
                                    if (cursor.moveToFirst()) {
                                        filePath = cursor.getString(columnIndex);
                                    }
                                    cursor.close();
                                }else{
                                    filePath = filemanagerstring;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Unknown path",
                                        Toast.LENGTH_LONG).show();
                                Log.e("Bitmap", "Unknown path");
                            }

                            if (filePath != null) {
                                decodeFile(filePath);
                            } else {
                                bitmap = null;
                            }
                        } catch (Exception e) {
                            //Toast.makeText(getApplicationContext(), "Internal error",
                            //      Toast.LENGTH_LONG).show();
                            Log.e(e.getClass().getName(), e.getMessage(), e);
                        }
                    }
                }
                break;
            case ACCOUNT_REQUEST_CODE:
                try {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        loading = new ProgressDialog(Aduin.this);
                        loading.setTitle("Tunggu Sebentar");
                        loading.show();
                        if (rotateBitmap == null) {
                            loading.dismiss();
                            Toast.makeText(Aduin.this, "Silakan ambil gambar terlebih dahulu", Toast.LENGTH_LONG).show();
                        } else {
                            loading.setMessage("Silakan tunggu . . .");
                            /*t.send(new HitBuilders.EventBuilder()
                                    .setCategory("Action")
                                    .setAction("Lapor")
                                    .build());*/
                            TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            String mPhoneNumber = tMgr.getLine1Number();
                            String imei = tMgr.getDeviceId();
                            new Lapor(accountName, mPhoneNumber, imei).execute();
                        }
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
        }
        mCameraIntentHelper.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri){
        String [] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }else{
            return null;
        }
    }

    public void decodeFile(String filePath){
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        ExifInterface exif = null;
        try{
            exif = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        rotateBitmap = BitmapHelper.rotateBitmap(bitmap, orientation);

        pic.setImageBitmap(rotateBitmap);
    }

    private class Lapor extends AsyncTask<Void, Integer, String> {

        String desc;
        StringBuilder sb;
        String result = null;
        String email;
        String phone;
        String imei;

        public Lapor(String email, String phone, String imei) {
            this.email = email;
            this.phone = phone;
            this.imei = imei;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sb = null;
            desc = etDeskripsi.getText().toString();
            loading.setMessage("Mengirim Aduan");
            loading.show();
            progressBar.setProgress(0);
            progressBar.setTitle("Upload");
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            progressBar.setMessage("Sedang mengunggah . . . " + String.valueOf(progress[0]) + "%");

        }

        @Override
        protected String doInBackground(Void... params) {
            InputStream is = null;
            BitmapFactory.Options bfo;
            Bitmap bitmapOrg;
            ByteArrayOutputStream bao ;

            bfo = new BitmapFactory.Options();
            bfo.inSampleSize = 2;
            //bitmapOrg = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + customImage, bfo);

            bao = new ByteArrayOutputStream();
            rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte [] ba = bao.toByteArray();
            String ba1 = Base64.encodeBytes(ba);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("nik", pref.getString("nik", "")));
            //nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("laporan",desc));
            nameValuePairs.add(new BasicNameValuePair("kategori",String.valueOf(kategori)));
            //nameValuePairs.add(new BasicNameValuePair("foto",ba1));
            nameValuePairs.add(new BasicNameValuePair("filename",pref.getString("nik","")+"-"+String.valueOf(System.currentTimeMillis())+".jpg"));

            nameValuePairs.add(new BasicNameValuePair("imei",imei));

            // Extra parameters if you want to pass to server
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(MainApplication.urlAduan);
                //totalSize = entity.getContentLength();
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity2 = response.getEntity();
                is = entity2.getContent();
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            loading.dismiss();
            try {
                Log.i("JSON","> "+result);
                JSONObject dataObj = new JSONObject(result);
                switch (dataObj.getString("status")){
                    case "1":
                        //Toast.makeText(Aduin1.this, "Selamat, laporan Anda telah dikirim", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getBaseContext(),AduanSucces.class);
                        startActivity(intent);
                        finish();
                        break;
                    case "0":
                        //Toast.makeText(Aduin.this,"Maaf Login gagal. Silakan coba beberapa saat lagi.", Toast.LENGTH_LONG).show();
                        DialogMessage("Maaf Laporan gagal. Silakan coba beberapa saat lagi.");
                        break;
                    case "2":
                        //Toast.makeText(Aduin.this,"Maaf perangkat Smart Phone Anda tidak bisa melakukan laporan karena terdeteksi duplikat login. Hanya diperbolehkan menggunakan satu SmartPhone untuk satu akun.", Toast.LENGTH_LONG).show();
                        DialogMessage("Maaf perangkat Smart Phone Anda tidak bisa melakukan laporan karena terdeteksi duplikat login. Hanya diperbolehkan menggunakan satu SmartPhone untuk satu akun.");
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }  catch (NullPointerException e) {
                //Toast.makeText(Aduin.this,"Maaf terjadi kesalahan dengan jaringan Anda. Silakan coba beberapa saat lagi", Toast.LENGTH_LONG).show();
                DialogMessage("Maaf terjadi kesalahan dengan jaringan Anda. Silakan coba beberapa saat lagi");
            }
        }
    }

    private void checkNetwork(){
        Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
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
                    Toast.makeText(getBaseContext(), "Disagreed", Toast.LENGTH_SHORT).show();
                    super.onNegativeActionClicked(fragment);
                }
            };

            ((SimpleDialog.Builder)builder).message("Aplikasi ini membutuhkan akses ke jaringan Anda, silakan nyalakan terlebih dahulu.")
                    .title("Nyalakan Jaringan")
                    .positiveAction("Nyalakan")
                    .negativeAction("Tolak");

            DialogFragment fragment = DialogFragment.newInstance(builder);
            fragment.show(getSupportFragmentManager(), null);
        }
    }

    /*@Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GoogleAnalytics.getInstance(Aduin.this)
                .reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GoogleAnalytics.getInstance(Aduin.this).reportActivityStop(this);
    }
*/

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

    private class GetCategory extends AsyncTask<Void, Void, Call<Category>>{

        @Override
        protected Call<Category> doInBackground(Void... params) {
            RetrofitApi api = RetrofitApiSingleton.getInstance().getApi();
            Call<Category> callCategory = api.getCategoryList();
            try {
                Category category = callCategory.execute().body();
                categoryDatumList = category.getCategoryDatumList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return callCategory;
        }

        @Override
        protected void onPostExecute(Call<Category> aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}