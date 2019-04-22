package com.casasolarctpi.appeenergia.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.ImageView;

import com.casasolarctpi.appeenergia.R;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    ImageView imageView;
    public static Context context;
    boolean bandera = true;
    int valor =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = this;
        imageView = findViewById(R.id.imgSplash);
        imageView.setVisibility(View.INVISIBLE);
        bandera= true;
        valor=0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (bandera){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            valor++;
                            if (valor==2){
                                animationSplash();
                                bandera=false;
                            }

                        }
                    });
                }
            }
        }).start();


    }

    public void animationSplash(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = ViewAnimationUtils.createCircularReveal(imageView,0,imageView.getWidth(),0,imageView.getHeight()*1.5f);
            final Animator animator1 = ViewAnimationUtils.createCircularReveal(imageView,imageView.getWidth()/2,imageView.getHeight()/2,imageView.getHeight()*1.5f,0);
            animator.setDuration(800);
            animator1.setDuration(800);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animator1.start();
                }
            });

            animator1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    imageView.setVisibility(View.INVISIBLE);
                    super.onAnimationEnd(animation);
                    transicionToMenuActivity();
                }
            });

            animator.start();

        }else {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    imageView.setVisibility(View.VISIBLE);
                    transicionToMenuActivity();
                }
            };
            new Timer().schedule(timerTask,1000);
        }


    }

    private void transicionToMenuActivity() {
        Intent intent = new Intent(Splash.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
