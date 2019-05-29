package za.co.a101apps.cognitologinapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;

public class GetConfirmationCodeActivity extends AppCompatActivity {

    private static final String TAG = "Cognito";
    private EditText editTextUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_confirmation_code);

        editTextUsername = findViewById(R.id.editTextUsername);

        Button buttonGetCode = findViewById(R.id.buttonGetCode);
        buttonGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CognitoSettings cognitoSettings = new CognitoSettings(GetConfirmationCodeActivity.this);
                CognitoUser thisUser = cognitoSettings.getUserPool()
                        .getUser(String.valueOf(editTextUsername.getText()));

                new ResendConfirmationCodeAsyncTask().execute(thisUser);
            }
        });
    }

    private class ResendConfirmationCodeAsyncTask extends AsyncTask<CognitoUser, Void, String> {

        @Override
        protected String doInBackground(CognitoUser... cognitoUsers) {

            final String[] result = new String[1];

            VerificationHandler resendConfCodeHandler = new VerificationHandler() {
                @Override
                public void onSuccess(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                    result[0] = "Confirmation code was successfully sent to: "
                            + cognitoUserCodeDeliveryDetails.getDestination();
                }

                @Override
                public void onFailure(Exception exception) {
                    result[0] = exception.getLocalizedMessage();
                }
            };
/*Request to resend registration confirmation code for a user, in current thread.*/
            cognitoUsers[0].resendConfirmationCode(resendConfCodeHandler);

            return result[0];
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "Resend verification result: " + result);
        }
    }
}


