package za.co.a101apps.cognitologinapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "Cognito";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        final EditText editTextUsername = findViewById(R.id.editTextUsername);
        final EditText editTextOldPassword = findViewById(R.id.editTextOldPassword);
        final EditText editTextNewPassword = findViewById(R.id.editTextNewPassword);

        Button buttonChangePassword = findViewById(R.id.buttonChangePassword);

        final GenericHandler handler = new GenericHandler() {

            @Override
            public void onSuccess() {
                // Password change was successful!
                Log.i(TAG, "password changed");
            }

            @Override
            public void onFailure(Exception exception) {
                // Password change failed, probe exception for details
                Log.i(TAG, "failed changing password: " + exception.getLocalizedMessage());
            }
        };

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CognitoSettings cognitoSettings = new CognitoSettings(ChangePasswordActivity.this);
                cognitoSettings.getUserPool().getUser(String.valueOf(editTextUsername.getText()))
                        .changePasswordInBackground(String.valueOf(editTextOldPassword.getText())
                        , String.valueOf(editTextNewPassword.getText()),handler);
            }
        });
    }
}
