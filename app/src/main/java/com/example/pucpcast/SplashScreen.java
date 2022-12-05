package com.example.pucpcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(() -> {

            Intent intent = new Intent(SplashScreen.this, WhatIsActivity.class);
            startActivity(intent);

        }, 3000);
    }
}