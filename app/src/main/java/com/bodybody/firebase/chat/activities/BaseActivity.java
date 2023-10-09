package com.bodybody.firebase.chat.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bodybody.firebase.chat.activities.managers.Screens;
import com.bodybody.firebase.chat.activities.managers.Utils;
import com.bodybody.firebase.chat.activities.views.SingleClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

//베이스 액티비티는 베이스 프레그먼트와 마찬가지로
//다른 클래스에서도 중복되는 내용들을 담은 액티비티입니다.

public class BaseActivity extends AppCompatActivity {
    protected final String[] permissionsRecord = {Manifest.permission.VIBRATE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    protected final String[] permissionsContact = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    protected final String[] permissionsStorage = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public Activity mActivity;
    public FirebaseAuth auth; // 파이어베이스 인증 시작
    public FirebaseAuth.AuthStateListener authStateListener;
    public DatabaseReference reference; //데이터베이스 관련
    public FirebaseUser firebaseUser; //현재 사용자
    public Screens screens;
    public ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        screens = new Screens(mActivity);
        try {
            authStateListener = firebaseAuth -> {
                try {
                    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                        if (getClass().getSimpleName().equalsIgnoreCase("LoginActivity") || getClass().getSimpleName().equalsIgnoreCase("RegisterActivity")) {
                        } else {
                            screens.showClearTopScreen(LoginActivity.class);
                        }
                    }
                } catch (Exception ignored) {
                }
            };
            FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    public void backButton() {
        try {
            imgBack = findViewById(R.id.imgBack);
            imgBack.setOnClickListener(new SingleClickListener() {
                @Override
                public void onClickView(View v) {
                    finish();
                }
            });
        } catch (Exception ignored) {

        }
    }

    private ProgressDialog pd = null;

    public void showProgress() {
        try {
            if (pd == null) {
                pd = new ProgressDialog(mActivity);
            }
            pd.setMessage(getString(R.string.msg_please_wait));
            pd.show();
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    public void hideProgress() {
        try {
            if (pd != null) {
                pd.dismiss();
                pd = null;
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    protected boolean permissionsAvailable(String[] permissions) {
        boolean granted = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            }
        }
        return granted;
    }
}
