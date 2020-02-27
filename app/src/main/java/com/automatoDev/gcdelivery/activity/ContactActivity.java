package com.automatoDev.gcdelivery.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.automatoDev.gcdelivery.R;

public class ContactActivity extends AppCompatActivity {

    public static boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }

    @Override
    protected void onStart() {
        super.onStart();
        status = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        status = false;
    }
}
