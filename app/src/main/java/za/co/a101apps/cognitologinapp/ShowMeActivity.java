package za.co.a101apps.cognitologinapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShowMeActivity extends AppCompatActivity {

    private static final String TAG = "Cognito";

    private CognitoCachingCredentialsProvider credentialsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_me);

        AWSMobileClient.getInstance().initialize(this).execute();

        Button buttonUpload = findViewById(R.id.buttonUpload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceed(1);
            }
        });

        Button buttonDownload = findViewById(R.id.buttonDownload);
        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceed(2);
            }
        });
    }

    private void proceed(final int action) {
        final CognitoSettings cognitoSettings = new CognitoSettings(this);

        /*Identity pool credentials provider*/
        Log.i(TAG, "getting Identity Pool credentials provider");
        credentialsProvider = cognitoSettings.getCredentialsProvider();

        /*get user - User Pool*/
        Log.i(TAG, "getting user Pool user");
        CognitoUser currentUser = cognitoSettings.getUserPool().getCurrentUser();

        /*get token for logged in user - user pool*/
        Log.i(TAG, "calling getSessionInBackground....");
        currentUser.getSessionInBackground(new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {

                if (userSession.isValid()) {
                    Log.i(TAG, "user session valid, getting token...");
                    // Get id token from CognitoUserSession.
                    String idToken = userSession.getIdToken().getJWTToken();

                    if (idToken.length() > 0) {
                        // Set up as a credentials provider.
                        Log.i(TAG, "got id token - setting credentials using token");
                        Map<String, String> logins = new HashMap<>();
                        logins.put("cognito-idp.eu-west-1.amazonaws.com/eu-west-1_2n6uKeWCd", idToken);
                        credentialsProvider.setLogins(logins);

                        Log.i(TAG, "using credentials for the logged in user");

                        /*refresh provider off main thread*/
                        Log.i(TAG, "refreshing credentials provider in asynctask..");
                        new RefreshAsyncTask().execute(action);

                    } else {
                        Log.i(TAG, "no token...");
                    }
                } else {
                    Log.i(TAG, "user session not valid - using identity pool credentials - guest user");
                }

                performAction(action);

            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                Log.i(TAG, " Not logged in! using identity pool credentials for guest user");

                performAction(action);

            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
//                not used
            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
//                not used
            }

            @Override
            public void onFailure(Exception exception) {
                Log.i(TAG, "error getting session: " + exception.getLocalizedMessage());
//                proceed using guest user credentials

                performAction(action);

            }
        });
    }

    private void performAction(int action) {
        switch (action) {
            case 1:
                uploadWithTransferUtility();
                break;
            case 2:
                downloadWithTransferUtility();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()... clearing credentials provider");
        /*clear the cached/saved credentials so we don't use them for guest user if not logged in*/
        credentialsProvider.clear();
    }


    private void downloadWithTransferUtility() {

        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);

        final ImageView imageView = findViewById(R.id.imageView);

        try {
            /*need to clear cached files at some stage*/
            File outputDir = getCacheDir();
            final File tempCacheFile = File.createTempFile("images", "extension", outputDir);

            TransferUtility transferUtility =
                    TransferUtility.builder()
                            .context(getApplicationContext())
                            .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                            .s3Client(s3Client)
                            .build();

            TransferObserver downloadObserver =
                    transferUtility.download(
                            "advert.png", tempCacheFile);

            // Attach a listener to the observer to get state update and progress notifications
            downloadObserver.setTransferListener(new TransferListener() {

                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        // Handle a completed upload.
                        Log.i(TAG, "state change, image download complete");

                        Bitmap bmp = BitmapFactory.decodeFile(tempCacheFile.getAbsolutePath());
                        imageView.setImageBitmap(bmp);
                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int) percentDonef;
                    Log.i(TAG, "   ID:" + id + "   bytesCurrent: "
                            + bytesCurrent + "   bytesTotal: " + bytesTotal + " " + percentDone + "%");
                }

                @Override
                public void onError(int id, Exception ex) {
                    // Handle errors
                    Log.i(TAG, "error downloading image: " + ex.getLocalizedMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "downloading image error: " + e.getLocalizedMessage());
        }
    }

    private void uploadWithTransferUtility() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();

        File file = new File(path, "advert.png");

        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);
        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();

        TransferObserver uploadObserver =
                transferUtility.upload("advert.png", file);

        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                    Log.i(TAG, "Upload completed");
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;

                Log.i("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.i(TAG, "upload error: " + ex.getLocalizedMessage());
            }
        });
    }

    private class RefreshAsyncTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            Log.i(TAG, "in asynctask doInBackground()");
            credentialsProvider.refresh();
            return integers[0];
        }

        @Override
        protected void onPostExecute(Integer action) {
            Log.i(TAG, "in asynctask onPostExecute()");

            performAction(action);

        }
    }
}
