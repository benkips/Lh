package com.mabnets.www.leaseholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import maes.tech.intentanim.CustomIntent;

public class launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);
        getSupportActionBar().hide();
        Logo logo=new Logo();
        logo.start();
    }
        private class Logo extends Thread {
            @Override
            public void run() {
                try {
                    sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent loginn = new Intent(launcher.this, Login.class);
                startActivity(loginn);
                launcher.this.finish();
            }


        }
}
