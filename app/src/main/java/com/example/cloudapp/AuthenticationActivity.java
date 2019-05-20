package com.example.cloudapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;

public class AuthenticationActivity extends AppCompatActivity {

    private final String TAG = AuthenticationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

            @Override
            public void onResult(UserStateDetails userStateDetails) {
               /* if(getIntent().getExtras() != null)
                {
                    String napis = getIntent().getStringExtra("logout_message");
                    Log.i(TAG, napis);
                    userStateDetails.equals(napis);
                }
                else {  }*/
                    Log.i(TAG, userStateDetails.getUserState().toString());

                    switch (userStateDetails.getUserState()) {
                        case SIGNED_IN:

                            // tutaj trzeba sprawdzac czy uzytkownik istnieje
                            if(true){
                                Intent i = new Intent(AuthenticationActivity.this, GetUserDataActivity.class);
                                startActivity(i);
                            } else{
                                Intent i = new Intent(AuthenticationActivity.this, MainActivity.class);
                                startActivity(i);
                            }


                            break;
                        case SIGNED_OUT:
                            showSignIn();
                            break;
                        default:
                            AWSMobileClient.getInstance().signOut();
                            showSignIn();
                            break;
                    }

            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, e.toString());
            }
        });
    }

    private void showSignIn() {
        try {
            AWSMobileClient.getInstance().showSignIn(this,
                    SignInUIOptions.builder().nextActivity(GetUserDataActivity.class).build());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}