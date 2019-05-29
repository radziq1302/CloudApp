package za.co.a101apps.cognitologinapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Cognito";
    private boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText editTextUsername = findViewById(R.id.editTextUsername);
        final EditText editTextPassword = findViewById(R.id.editTextPassword);

        final AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                flag=true;
                Log.i(TAG, "Login successfull, can get tokens here!");

                /*userSession contains the tokens*/

               /* Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(userSession);
                Log.i(TAG, "user session: "+json);*/
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation
                    , String userId) {

                Log.i(TAG, "in getAuthenticationDetails()....");

                /*need to get the userId & password to continue*/
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId
                        , String.valueOf(editTextPassword.getText()), null);

                // Pass the user sign-in credentials to the continuation
                authenticationContinuation.setAuthenticationDetails(authenticationDetails);

                // Allow the sign-in to continue
                authenticationContinuation.continueTask();

            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

                Log.i(TAG, "in getMFACode()....");

                // if Multi-factor authentication is required; get the verification code from user
//                multiFactorAuthenticationContinuation.setMfaCode(mfaVerificationCode);

                // Allow the sign-in process to continue
//                multiFactorAuthenticationContinuation.continueTask();
            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                Log.i(TAG, "in authenticationChallenge()....");
            }

            @Override
            public void onFailure(Exception exception) {
                Log.i(TAG, "Login failed: " + exception.getLocalizedMessage());
            }
        };

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CognitoSettings cognitoSettings = new CognitoSettings(LoginActivity.this);

                CognitoUser thisUser = cognitoSettings.getUserPool()
                        .getUser(String.valueOf(editTextUsername.getText()));
// Sign in the user
                Log.i(TAG, "in button clicked....");

                thisUser.getSessionInBackground(authenticationHandler);
                if (flag==true){
                    Intent i = new Intent(LoginActivity.this, GetUserDataActivity.class);
                    LoginActivity.this.startActivity(i);
                }
                else {
                    Toast.makeText(LoginActivity.this, "błędne dane", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}