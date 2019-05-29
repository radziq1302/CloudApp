package za.co.a101apps.cognitologinapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        Button buttonVerify = findViewById(R.id.buttonVerify);
        buttonVerify.setOnClickListener(this);

        Button buttonRequestCode = findViewById(R.id.buttonRequestCode);
        buttonRequestCode.setOnClickListener(this);

        Button buttonForgotPassword = findViewById(R.id.buttonForgotPassword);
        buttonForgotPassword.setOnClickListener(this);

        Button buttonChangePassword = findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(this);

        Button buttonGetTokens = findViewById(R.id.buttonGetTokens);
        buttonGetTokens.setOnClickListener(this);

        Button buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);

        Button buttonGetUserDetails = findViewById(R.id.buttonGetDetails);
        buttonGetUserDetails.setOnClickListener(this);

        Button buttonShowMe = findViewById(R.id.buttonShowMe);
        buttonShowMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonLogin:
                Intent intentLogin = new Intent(this, LoginActivity.class);
                startActivity(intentLogin);
                break;
            case R.id.buttonVerify:
                Intent intentVerify = new Intent(this, VerifyActivity.class);
                startActivity(intentVerify);
                break;
            case R.id.buttonRequestCode:
                Intent intentResendCode = new Intent(this, GetConfirmationCodeActivity.class);
                startActivity(intentResendCode);
                break;
            case R.id.buttonForgotPassword:
                Intent intentForgotPassword = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intentForgotPassword);
                break;
            case R.id.buttonChangePassword:
                Intent intentChangePassword = new Intent(this, ChangePasswordActivity.class);
                startActivity(intentChangePassword);
                break;
            case R.id.buttonGetTokens:
                Intent intentGetTokens = new Intent(this, GetTokensActivity.class);
                startActivity(intentGetTokens);
                break;
            case R.id.buttonLogout:
                Intent intentLogout = new Intent(this, LogoutActivity.class);
                startActivity(intentLogout);
                break;
            case R.id.buttonGetDetails:
                Intent intentGetUserDetails = new Intent(this, GetUserDetailsActivity.class);
                startActivity(intentGetUserDetails);
                break;
            case R.id.buttonShowMe:
                Intent intentShowMe = new Intent(this, ShowMeActivity.class);
                startActivity(intentShowMe);
                break;
        }
    }
}
