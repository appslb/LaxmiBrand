package com.e.laxmibrand.user;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.laxmibrand.R;

public class SplashScreen extends AppCompatActivity {

    private ImageView logo;
    private static int splashTimeOut=2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        logo=(ImageView)findViewById(R.id.splashImage);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this,UserLogin.class);
                startActivity(i);

                finish();


            }
        },splashTimeOut);

       Animation myanim = AnimationUtils.loadAnimation(this,R.anim.splash_animation);
        logo.startAnimation(myanim);
    }
}