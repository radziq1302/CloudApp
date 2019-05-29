package za.co.a101apps.cognitologinapp;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class CognitoSettings {

    private String userPoolId = "us-east-2_JbhhbQMsj";
    private String clientId = "10cu8saui66s8279kd03mhmi75";
    private String clientSecret = "64vgc8lf0rb19fki2jglpvubrbk40gghh2etonep8s22m0u2g8j";
    private Regions cognitoRegion = Regions.US_EAST_2;

    private String identityPoolId = "us-east-2:b5e78aad-7888-42f7-b940-e8830b83eb9a";

    private Context context;


    public CognitoSettings(Context context) {
        this.context = context;
    }

    public String getUserPoolId() {
        return userPoolId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Regions getCognitoRegion() {
        return cognitoRegion;
    }

    /*the entry point for all interactions with your user pool from your application*/
    public CognitoUserPool getUserPool() {
        return new CognitoUserPool(context, userPoolId, clientId
                , clientSecret, cognitoRegion);
    }

    public CognitoCachingCredentialsProvider getCredentialsProvider() {
        return new CognitoCachingCredentialsProvider(
                context.getApplicationContext(),
                identityPoolId, // Identity pool ID
                cognitoRegion// Region;
        );
    }

}
