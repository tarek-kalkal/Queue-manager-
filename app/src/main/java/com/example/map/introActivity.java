package com.example.map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class introActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void createAcount(View view)
    {
        Intent intent = new Intent(introActivity.this ,signUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
        startActivity(intent);
        finish();
    }


    public void login(View view)
    {
        Intent intent = new Intent(introActivity.this ,signInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
        startActivity(intent);
        finish();
    }
}
