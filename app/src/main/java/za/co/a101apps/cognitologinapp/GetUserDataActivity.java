package za.co.a101apps.cognitologinapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.EditText;
import android.support.design.widget.TextInputEditText;
import android.widget.Spinner;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.util.ArrayList;

public class GetUserDataActivity extends AppCompatActivity {

    private static final String TAG = "Cognito";

    private Button wyslij;
    private TextInputEditText wzrost;
    private TextInputEditText waga;
    private TextInputEditText wiek;
    private Spinner plec; //gender_spinner
    private Spinner aktywnosc; //activity_spinner

    private boolean valid = true;
    PopupWindow popUp;

    private CognitoCachingCredentialsProvider credentialsProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_data);
        wyslij = (Button) findViewById(R.id.button_send);
        wzrost = (TextInputEditText) findViewById(R.id.textInputEdit1);
        waga = (TextInputEditText) findViewById(R.id.textInputEdit2);
        wiek =(TextInputEditText) findViewById(R.id.textInputEdit3);
        plec = (Spinner) findViewById(R.id.gender_spinner);
        aktywnosc = (Spinner) findViewById(R.id.activity_spinner);

        wyslij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (wzrost.getText().toString().equals("") || waga.getText().toString().equals("") || wiek.getText().toString().equals("")) {
                    valid = false;
                    Log.i("warunek","true");
                } else {
                    valid = true;
                    Log.i("warunek","false");
                }

                if(valid) {
                    Log.i("hi","jest ok");

                    //uploadWithTransferUtility();

                    Intent validDataSent = new Intent(GetUserDataActivity.this, MainViewActivity.class);
                    GetUserDataActivity.this.startActivity(validDataSent);
                }
                else {
                    // popup zeby uzupelnic
                }
            }
        });


    }

    private void uploadWithTransferUtility() {

        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File file = new File(path, "advert.png");

        // zamiast file chcemy wyslac to co uzytkownik wpisal
        //S3Object stringObject = new S3Object("HelloWorld.txt", "Hello World!");
        //s3Client.putObject(s3Client, stringObject);


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()... clearing credentials provider");
        /*clear the cached/saved credentials so we don't use them for guest user if not logged in*/
        credentialsProvider.clear();
    }

}
