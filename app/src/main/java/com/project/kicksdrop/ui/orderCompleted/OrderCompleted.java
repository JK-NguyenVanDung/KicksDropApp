package com.project.kicksdrop.ui.orderCompleted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.project.kicksdrop.MainActivity;
import com.project.kicksdrop.R;
import com.project.kicksdrop.SplashScreen;
import com.project.kicksdrop.ui.auth.LoginActivity;

public class OrderCompleted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_completed);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(OrderCompleted.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },1000);
    }
}