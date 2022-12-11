package com.example.pucpcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_password);
    }

    public void volverLogin(View view) {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }
}