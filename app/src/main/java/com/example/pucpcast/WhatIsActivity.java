package com.example.pucpcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class WhatIsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("msg", "Bienvenido");
        setContentView(R.layout.what_is);
    }
    public void irLogin(View view) {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }
}