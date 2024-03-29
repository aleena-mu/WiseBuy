package com.example.wisebuy.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.wisebuy.MainActivity;
import com.example.wisebuy.R;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Animations
        //Variable
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        Animation botAnim = AnimationUtils.loadAnimation(this, R.anim.bot_anim);

        //hooks
        ImageView logo = findViewById(R.id.logo);


        logo.setAnimation(botAnim);
        logo.setAnimation(topAnim);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent dsp =new Intent(SplashScreen.this, MainActivity.class);
                startActivity(dsp);
                finish();
            }
        }, 4000);





    }
}