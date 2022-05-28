package com.example.myapplication;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.Common.CodeReUse;
import com.example.myapplication.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity {

    private static int SPLASH_TIME_OUT = 3000;
    public ActivitySplashBinding binding;
    //Animations
    Animation topAnimation, bottomAnimation, middleAnimation, rightSlideAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
    }

    public void initView() {

        //Splash Screen Code to call new Activity after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(act, LoginActivity.class);
                startActivity(intent);
                finish();
                CodeReUse.slideEnter(act);
            }
        }, SPLASH_TIME_OUT);

        CodeReUse.makeStatusBarTransparent(act);
        //apply text Gradient
        TextPaint paint = binding.bmShareTxt.getPaint();
        float width = paint.measureText(binding.bmShareTxt.getText().toString().trim());
        Shader textShader = new LinearGradient(0, 0, width, binding.bmShareTxt.getTextSize(),
                new int[]{
                        ContextCompat.getColor(act, R.color.colorsecond),
                        ContextCompat.getColor(act, R.color.colorthird),
                }, null, Shader.TileMode.CLAMP);
        binding.bmShareTxt.getPaint().setShader(textShader);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        rightSlideAnimation = AnimationUtils.loadAnimation(this, R.anim.right_slide_enter);

        //Setting Animations to the elements of Splash
        binding.firstLine.setAnimation(middleAnimation);
        binding.sixthLine.setAnimation(rightSlideAnimation);
        binding.secondLine.setAnimation(topAnimation);
        binding.thirdLine.setAnimation(topAnimation);
        binding.fourthLine.setAnimation(topAnimation);
        binding.fifthLine.setAnimation(topAnimation);
        binding.bmShareTxt.setAnimation(middleAnimation);
        binding.tagLine.setAnimation(bottomAnimation);
    }
}