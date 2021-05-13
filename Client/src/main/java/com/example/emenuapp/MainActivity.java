package com.example.emenuapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import java.io.InputStreamReader;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {
    private TextView Text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Text = findViewById(R.id.warning);

    }






    @Override
    protected void onDestroy(){
        super.onDestroy();
        //



    }

    public void onClickSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        Text.setText("");

    }
    public void onClickStart(View view) {
        try{
            InputStreamReader r = new InputStreamReader(openFileInput("Config.txt"));
            r.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startLockTask();
            }
            Intent intent = new Intent(this, NetworkActivity.class);
            startActivity(intent);
            Text.setText("");

        }
        catch (IOException e)
        {
            Text.setText("Необоходима первоначальная настройка!!!");
        }


    }
}