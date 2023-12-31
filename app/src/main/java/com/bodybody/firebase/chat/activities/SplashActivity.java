package com.bodybody.firebase.chat.activities;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bodybody.firebase.chat.activities.constants.IConstants;
import com.bodybody.firebase.chat.activities.managers.Screens;
import com.bodybody.firebase.chat.activities.managers.SessionManager;
import com.bodybody.firebase.chat.activities.managers.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

//SplashActivity는 AppCompatActivity를 상속받습니다.
public class SplashActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser; //현재 유저

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setWindow(getWindow());
        setContentView(R.layout.activity_splash);

        ((TextView) findViewById(R.id.txtName)).setText(String.format(getString(R.string.app_company_name), getString(R.string.app_company)));

        StartAnimations();
        load();
    }

    private Screens screens;

    private void load() {
        screens = new Screens(getApplicationContext());
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            try {
                if (SessionManager.get().isOnBoardingDone()) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser != null) {
                        firebaseUser.reload().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                screens.showClearTopScreen(MainActivity.class);
                            } else {
                                screens.showToast(Objects.requireNonNull(task.getException()).getMessage());
                                screens.showClearTopScreen(LoginActivity.class);
                            }
                        });
                    } else {
                        screens.showClearTopScreen(LoginActivity.class);
                    }
                } else {
                    screens.showClearTopScreen(OnBoardingActivity.class);
                }
            } catch (Exception e) {
                Utils.getErrors(e);
            }
        }, IConstants.SPLASH_DELAY);
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l = findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        LinearLayout iv = findViewById(R.id.layout);
        iv.clearAnimation();
        iv.startAnimation(anim);
    }

}
