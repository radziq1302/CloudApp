package za.co.a101apps.cognitologinapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Cognito";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_register);

        registerUser();
    }

    private void registerUser() {

        final EditText inputName = findViewById(R.id.editText2);
        final EditText inputTelephone = findViewById(R.id.editText);
        final EditText inputEmail = findViewById(R.id.editText3);

        final EditText inputPassword = findViewById(R.id.editText4);
        final EditText inputUsername = findViewById(R.id.editText5);

        // Create a CognitoUserAttributes object and add user attributes
        final CognitoUserAttributes userAttributes = new CognitoUserAttributes();

        final SignUpHandler signupCallback = new SignUpHandler() {
            @Override
            public void onSuccess(CognitoUser user, boolean signUpConfirmationState
                    , CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                // Sign-up was successful
                Log.i(TAG, "sign up success...is confirmed: " + signUpConfirmationState);
                // Check if this user (cognitoUser) needs to be confirmed
                if (!signUpConfirmationState) {
                    Log.i(TAG, "sign up success...not confirmed, verification code sent to: "
                            + cognitoUserCodeDeliveryDetails.getDestination());
                } else {
                    // The user has already been confirmed
                    Log.i(TAG, "sign up success...confirmed");
                }
            }

            @Override
            public void onFailure(Exception exception) {
// Sign-up failed, check exception for the cause
                Log.i(TAG, "sign up failure: " + exception.getLocalizedMessage());
            }
        };

        Button buttonRegister = findViewById(R.id.button);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userAttributes.addAttribute("given_name", String.valueOf(inputName.getText()));
                userAttributes.addAttribute("phone_number", String.valueOf(inputTelephone.getText()));
                userAttributes.addAttribute("email", String.valueOf(inputEmail.getText()));

                CognitoSettings cognitoSettings = new CognitoSettings(RegisterActivity.this);

                cognitoSettings.getUserPool().signUpInBackground(String.valueOf(inputUsername.getText())
                        , String.valueOf(inputPassword.getText()), userAttributes
                        , null, signupCallback);
            }
        });
    }
}
