package com.example.carmanagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.wang.avi.AVLoadingIndicatorView;

public class Splashscreen extends AppCompatActivity {
    private AVLoadingIndicatorView avi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        avi = (AVLoadingIndicatorView)findViewById(R.id.avisplash);

        Thread thread = new Thread(){
            @Override
            public void run() {

                try {
                    startAnim();
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(Splashscreen.this,Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }

    private void startAnim() {
        avi.smoothToShow();
    }
}