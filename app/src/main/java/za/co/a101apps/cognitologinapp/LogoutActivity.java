package za.co.a101apps.cognitologinapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LogoutActivity extends AppCompatActivity {

    private static final String TAG = "Cognito";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        CognitoSettings cognitoSettings = new CognitoSettings(this);
        CognitoUser currentUser = cognitoSettings.getUserPool().getCurrentUser();

        Log.i(TAG, "calling signout....");

        currentUser.signOut();

        AuthenticationHandler callbackAuthentication = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Log.i(TAG, "do we have vaild access & id tokens? " + userSession.isValid());
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation
                    , String userId) {
                Log.i(TAG, " Not logged in! needs login details");
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                Log.i(TAG, " get MFA ");
            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                Log.i(TAG, " in authentication challenge");
            }

            @Override
            public void onFailure(Exception exception) {
                Log.i(TAG, " authentication callback failure:  " + exception.getLocalizedMessage());
            }
        };
        currentUser.getSessionInBackground(callbackAuthentication);

//        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /*global signout*/
        GenericHandler callbackGlobalSignOut = new GenericHandler() {
            @Override
            public void onSuccess() {
//                successfully signed out
                Log.i(TAG, "global sign out success");
            }

            @Override
            public void onFailure(Exception exception) {
//failed sign out
                Log.i(TAG, "global sign out failure: " + exception.getLocalizedMessage());
            }
        };

//        currentUser.globalSignOutInBackground(callbackGlobalSignOut);
    }
}
