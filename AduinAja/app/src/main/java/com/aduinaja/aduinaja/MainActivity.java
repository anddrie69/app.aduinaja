package com.aduinaja.aduinaja;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

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
import com.rey.material.app.ThemeManager;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.loginfb);
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(sp.contains("First")){
            boolean isFirst = sp.getBoolean("First",true);
            if(!isFirst){
                startActivity(new Intent(this, LihatAduan.class));
                finish();
            }
        }else {
            editor.putBoolean("First",false);
            editor.commit();
        }
        ThemeManager.init(this, 2, 0, null);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                    // App code

                    // login ok get access token
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                if (BuildConfig.DEBUG) {
                                    FacebookSdk.setIsDebugEnabled(true);
                                    FacebookSdk
                                            .addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
                                    System.out
                                            .println("AccessToken.getCurrentAccessToken()"
                                                    + AccessToken
                                                    .getCurrentAccessToken()
                                                    .toString());
                                    //Log.i("FB", )
                                    Profile.getCurrentProfile().getId();
                                    Profile.getCurrentProfile().getFirstName();
                                    Profile.getCurrentProfile().getLastName();
                                    Profile.getCurrentProfile().getProfilePictureUri(50, 50);
                                    Intent next = new Intent(MainActivity.this, TabFragment.class );
                                    startActivity(next);
                                    //String email=UserManager.asMap().get(“email”).toString();
                                }
                            }
                        });
                    request.executeAsync();
                /*  Bundle parameters = new Bundle();
                    parameters
                            .putString("fields",
                                    "id,firstName,lastName,name,email,gender,birthday,address");
                    request.setParameters(parameters);



                    Intent loginintent = new Intent(getActivity(),
                            EditProfile.class);
                    startActivity(loginintent);
                    System.out.println("XXXX " + getId());
                 */
                    //makeJsonObjReq();

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
}
