package za.co.a101apps.cognitologinapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetTokensActivity extends AppCompatActivity {

    private static final String TAG = "Cognito";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_tokens);

        CognitoSettings cognitoSettingsOut = new CognitoSettings(this);
        CognitoUser currentUser = cognitoSettingsOut.getUserPool().getCurrentUser();

        AuthenticationHandler callback=new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Log.i(TAG, "Access token: " + userSession.getAccessToken().getJWTToken());
                Log.i(TAG, "ID token: " + userSession.getIdToken().getJWTToken());
                Log.i(TAG, "Refresh token: " + userSession.getRefreshToken());

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(userSession.getRefreshToken());
                Log.i(TAG, "Refresh token: "+json);
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {

            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {

            }

            @Override
            public void onFailure(Exception exception) {
                Log.i(TAG, "Failure getting tokens: " + exception.getLocalizedMessage());
            }
        };

        currentUser.getSession(callback);
    }
}
