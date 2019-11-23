package com.example.smarttrafficmanagement1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private TextView tv;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


            tv = (TextView) findViewById(R.id.tvname);
            iv =(ImageView) findViewById(R.id.imgLPT);

            Animation myanim = AnimationUtils.loadAnimation(this,R.anim.transition);
            Animation myanima = AnimationUtils.loadAnimation(this,R.anim.mytransition);
            tv.startAnimation(myanim);
            iv.startAnimation(myanima);

            final Intent i = new Intent(this, MainActivity.class);


            Thread timer  = new Thread(){
                public void run(){
                    try {
                        sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    finally {
                        startActivity(i);
                        finish();

                    }
                }
            };
            timer.start();
        }
    }

