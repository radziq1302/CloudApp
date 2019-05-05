package com.example.cloudapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;

public class MainActivity extends AppCompatActivity {
    private Button wyloguj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wyloguj = (Button) findViewById(R.id.logout);
            wyloguj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "abc", Toast.LENGTH_SHORT).show();
                    signOut(v);
                }
            });
    }
    public void signOut(View view) {
        AWSMobileClient.getInstance().signOut();
        Intent a = new Intent(MainActivity.this, AuthenticationActivity.class);
        startActivity(a);
    }
}
