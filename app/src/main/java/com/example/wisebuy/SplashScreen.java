package com.example.wisebuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    Handler handler;
    Runnable runnable;
    //Variable
    Animation topAnim,botAnim;
    ImageView l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Animatios
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_anim);
        botAnim= AnimationUtils.loadAnimation(this,R.anim.bot_anim);

        //hooks
        l=findViewById(R.id.logo);


        l.setAnimation(botAnim);
        l.setAnimation(topAnim);
        handler = new Handler();
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