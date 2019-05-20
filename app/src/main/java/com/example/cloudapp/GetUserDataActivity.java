package com.example.cloudapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.PopupWindow;

public class GetUserDataActivity extends AppCompatActivity {

    private Button wyslij;
    private TextView textInput1;
    private TextView textInput2;
    private TextView textInput3;
    private boolean valid;
    PopupWindow popUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wyslij = findViewById(R.id.button_send);
        textInput1 = findViewById(R.id.textInput1);
        textInput2 = findViewById(R.id.textInput2);
        textInput3 = findViewById(R.id.textInput3);
        setContentView(R.layout.activity_get_user_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        popUp = new PopupWindow(this);
        valid = false;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        /*if (!textInput1.getText().toString().equals("") && !textInput2.getText().toString().equals("") && !textInput3.getText().toString().equals(""))
        {
            valid = true;
        }


        wyslij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid) {
                    Intent validDataSent = new Intent(GetUserDataActivity.this, AddDataActivity.class);
                    GetUserDataActivity.this.startActivity(validDataSent);
                }
                else {
                    // popup zeby uzupelnic
                }
            }
        });*/
    }

}
