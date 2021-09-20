package com.example.schoolNeedsOrganizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.exercises.R;
import com.example.schoolNeedsOrganizer.AppSettings.Settings;


public class MainActivity extends AppCompatActivity {

    private LinearLayout navigation;
    private ImageView banner, topBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Settings.getSettings(getApplicationContext());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_window);
        getSupportActionBar().hide();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
        Animation bannerStartAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.banner_anim);
        banner = findViewById(R.id.banner);
        banner.startAnimation(bannerStartAnimation);
        bannerStartAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation bannerEndAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.banner_end_animation);
                bannerEndAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        android.view.ViewGroup.LayoutParams layoutParams = banner.getLayoutParams();
                        layoutParams.height = (int) (banner.getHeight() * 0.25);
                        layoutParams.width = (int) (banner.getWidth() * 0.25);
                        banner.setLayoutParams(layoutParams);
                        banner.setTranslationY(-900);
                        banner.setTranslationX(-5);
                        banner.setAlpha(0);
                        overridePendingTransition(R.anim.banner_anim, R.anim.banner_end_animation);
                        Intent intent = new Intent("android.intent.action.SCREEN");
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                banner.startAnimation(bannerEndAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}