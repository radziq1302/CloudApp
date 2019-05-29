package za.co.a101apps.cognitologinapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "Cognito";
    private ForgotPasswordContinuation resultContinuation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final ForgotPasswordHandler callback = new ForgotPasswordHandler() {
            @Override
            public void onSuccess() {
                 /*Forgotten password process completed successfully
                // , new password has been successfully set*/
                Log.i(TAG, "password changed successfully");
            }

            @Override
            public void getResetCode(ForgotPasswordContinuation continuation) {
                Log.i(TAG, "in getResetCode....");

                 /*A code will be sent
                 , use the "continuation" object to continue with the forgot password process*/

                // This will indicate where the code was sent
                CognitoUserCodeDeliveryDetails codeSentHere = continuation.getParameters();
                Log.i(TAG, "Code sent here: " + codeSentHere.getDestination());

//                create field so we can use the continuation object in our reset password button
                resultContinuation = continuation;
            }

            public void onFailure(Exception exception) {
                // Forgot password processing failed
                Log.i(TAG, "password changed failed: " + exception.getLocalizedMessage());
            }
        };

        final EditText editTextUsername = findViewById(R.id.editTextUsername);
        Button buttonGetCode = findViewById(R.id.buttonGetCode);
        buttonGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CognitoSettings cognitoSettings = new CognitoSettings(ForgotPasswordActivity.this);
                CognitoUser thisUser = cognitoSettings.getUserPool()
                        .getUser(String.valueOf(editTextUsername.getText()));

                Log.i(TAG, "calling forgot password to get confirmation code....");

                thisUser.forgotPasswordInBackground(callback);
            }
        });


        final EditText editTextCode = findViewById(R.id.editTextCode);
        final EditText editTextNewPassword = findViewById(R.id.editTextNewPassword);

        Button buttonResetPassword = findViewById(R.id.buttonResetPassword);
        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "got code & password, setting continuation object....");

                resultContinuation.setPassword(String.valueOf(editTextNewPassword.getText()));
                resultContinuation.setVerificationCode(String.valueOf(editTextCode.getText()));

                Log.i(TAG, "got code & password, calling continueTask()....");

                // Let the forgot password process continue
                resultContinuation.continueTask();
            }
        });
    }
}
