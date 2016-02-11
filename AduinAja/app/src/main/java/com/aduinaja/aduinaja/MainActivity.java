package com.aduinaja.aduinaja;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.aduinaja.application.MainApplication;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import network.Base64;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.loginfb);
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        if(sp.contains("id_fb")){
            String isid = sp.getString("id_fb", "");
            if(!isid.equals("")){
                startActivity(new Intent(this, VerifikasiNIK.class));
                finish();
            }
        }else {

        }
        ThemeManager.init(this, 2, 0, null);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {

                                if (BuildConfig.DEBUG) {
                                    FacebookSdk.setIsDebugEnabled(true);
                                    FacebookSdk
                                            .addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

                                    System.out
                                            .println("AccessToken.getCurrentAccessToken()"
                                                    + AccessToken
                                                    .getCurrentAccessToken()
                                                    .toString());
                                    /*Profile.getCurrentProfile().getId();
                                    Profile.getCurrentProfile().getFirstName();
                                    Profile.getCurrentProfile().getLastName();
                                    Profile.getCurrentProfile().getProfilePictureUri(50, 50);
                                    Log.i("facebook", Profile.getCurrentProfile().getId());*/
                                    //String email=UserManager.asMap().get(“email”).toString();
                                }

                                new Register(Profile.getCurrentProfile().getId(), Profile.getCurrentProfile().getFirstName()+"%20"+Profile.getCurrentProfile().getLastName(),"").execute();
                                editor.putString("id_fb", Profile.getCurrentProfile().getId());
                                editor.commit();


                            }
                        });
                request.executeAsync();

            }



            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class Register extends AsyncTask<Void, Integer, String> {


        ProgressDialog loading;
        StringBuilder sb;
        String result = null;
        String id_fb;
        String nama;
        String email;

        public Register(String id_fb, String nama, String email) {
            loading = new ProgressDialog(MainActivity.this);
            sb = null;
            this.id_fb = id_fb;
            this.nama = nama;
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sb = null;

            loading.setMessage("Mengirim Aduan");
            loading.show();
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

            byte [] ba = bao.toByteArray();
            String ba1 = Base64.encodeBytes(ba);


            // Extra parameters if you want to pass to server
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(MainApplication.urlRegister+"&id_fb="+id_fb+"&nama="+nama+"&email="+email);
                //totalSize = entity.getContentLength();
                //httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpGet);
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
                if (!dataObj.getString("status").equals("1")){
                    DialogMessage("Registrasi gagal");
                }
                else{

                    loading.dismiss();
                    Intent next = new Intent(MainActivity.this, VerifikasiNIK.class );
                    startActivity(next);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }  catch (NullPointerException e) {
                //Toast.makeText(Aduin.this,"Maaf terjadi kesalahan dengan jaringan Anda. Silakan coba beberapa saat lagi", Toast.LENGTH_LONG).show();
                DialogMessage("Maaf terjadi kesalahan dengan jaringan Anda. Silakan coba beberapa saat lagi");
            }
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
}
