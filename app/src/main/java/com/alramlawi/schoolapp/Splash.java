package com.alramlawi.schoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView = (ImageView)findViewById(R.id.logo);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash.this, MainActivity.class);

                startActivity(intent);
                finish();
            }
        }, 2000);

    }
}
