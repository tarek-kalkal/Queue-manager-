package com.example.map;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {

    private static int delay = 3000;
    private Animation animBounce;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);

        animBounce = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bouncing);
        img.startAnimation(animBounce);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    Intent intent1 = new Intent(MainActivity.this, introActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
                    startActivity(intent1);
                    finish();

            }
        }, delay);
    }
}