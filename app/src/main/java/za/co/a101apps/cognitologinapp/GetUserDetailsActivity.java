package za.co.a101apps.cognitologinapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetUserDetailsActivity extends AppCompatActivity {

    private static final String TAG = "Cognito";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_details);

        CognitoSettings cognitoSettingsOut = new CognitoSettings(this);
        CognitoUser currentUser = cognitoSettingsOut.getUserPool().getCurrentUser();

        // Implement callback handler for getting details
        GetDetailsHandler getDetailsHandler = new GetDetailsHandler() {
            @Override
            public void onSuccess(CognitoUserDetails cognitoUserDetails) {

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(cognitoUserDetails);
                Log.i(TAG, "cognitoUserDetails: " + json);
            }

            @Override
            public void onFailure(Exception exception) {
                // Fetch user details failed, check exception for the cause
                Log.i(TAG, "failed getting cognitoUserDetails: " + exception.getLocalizedMessage());
            }
        };

// Fetch the user details
        currentUser.getDetailsInBackground(getDetailsHandler);
    }
}
