package com.bodybody.firebase.chat.activities;

import static com.bodybody.firebase.chat.activities.constants.IConstants.EXTRA_ACTIVE;
import static com.bodybody.firebase.chat.activities.constants.IConstants.EXTRA_CREATED_AT;
import static com.bodybody.firebase.chat.activities.constants.IConstants.EXTRA_EMAIL;
import static com.bodybody.firebase.chat.activities.constants.IConstants.EXTRA_ID;
import static com.bodybody.firebase.chat.activities.constants.IConstants.EXTRA_IMAGEURL;
import static com.bodybody.firebase.chat.activities.constants.IConstants.EXTRA_IS_ONLINE;
import static com.bodybody.firebase.chat.activities.constants.IConstants.EXTRA_PASSWORD;
import static com.bodybody.firebase.chat.activities.constants.IConstants.EXTRA_SEARCH;
import static com.bodybody.firebase.chat.activities.constants.IConstants.EXTRA_SIGNUP_TYPE;
import static com.bodybody.firebase.chat.activities.constants.IConstants.EXTRA_USERNAME;
import static com.bodybody.firebase.chat.activities.constants.IConstants.EXTRA_VERSION;
import static com.bodybody.firebase.chat.activities.constants.IConstants.IMG_DEFAULTS;
import static com.bodybody.firebase.chat.activities.constants.IConstants.REF_USERS;
import static com.bodybody.firebase.chat.activities.constants.IConstants.STATUS_ONLINE;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_EMAIL;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bodybody.firebase.chat.activities.managers.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.BuildConfig;

import java.util.HashMap;

//RegisterActivity는 베이스 액티비티를 상속받습니다.
//이메일, 사용자 이름, 비밀번호 등을 회원 가입시 작성하고 데이터를 받습니다.
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText mTxtEmail;
    private EditText mTxtUsername;
    private EditText mTxtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mTxtEmail = findViewById(R.id.txtEmail);
        mTxtUsername = findViewById(R.id.txtUsername);
        mTxtPassword = findViewById(R.id.txtPassword);
        final Button mBtnRegister = findViewById(R.id.btnRegister);
        final TextView mTxtExistingUser = findViewById(R.id.txtExistingUser);

//        mTxtExistingUser.setText(HtmlCompat.fromHtml(getString(R.string.strExistUser), HtmlCompat.FROM_HTML_MODE_LEGACY));
        Utils.setHTMLMessage(mTxtExistingUser, getString(R.string.strExistUser));

        mBtnRegister.setOnClickListener(this);
        mTxtExistingUser.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btnRegister) {
            String strEmail = mTxtEmail.getText().toString().trim();
            String strUsername = mTxtUsername.getText().toString().trim();
            String strPassword = mTxtPassword.getText().toString().trim();

            if (TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strPassword)) {
                screens.showToast(R.string.strAllFieldsRequired);
            } else if (!Utils.isValidEmail(strEmail)) {
                screens.showToast(R.string.strInvalidEmail);
            } else {
                register(strEmail, strUsername, strPassword);
            }
        } else if (id == R.id.txtExistingUser) {
            finish();
        }
    }

    private void register(final String email, final String username, final String password) {
        showProgress();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                assert firebaseUser != null;
                String userId = firebaseUser.getUid();

                reference = FirebaseDatabase.getInstance().getReference(REF_USERS).child(userId);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(EXTRA_ID, userId);
                hashMap.put(EXTRA_EMAIL, email);
                hashMap.put(EXTRA_USERNAME, Utils.getCapsWord(username));
                hashMap.put(EXTRA_PASSWORD, password);
                hashMap.put(EXTRA_IMAGEURL, IMG_DEFAULTS);
                hashMap.put(EXTRA_ACTIVE, true);
                hashMap.put(EXTRA_IS_ONLINE, STATUS_ONLINE);
                hashMap.put(EXTRA_SEARCH, username.toLowerCase().trim());
                hashMap.put(EXTRA_CREATED_AT, Utils.getDateTime());
                hashMap.put(EXTRA_VERSION, BuildConfig.VERSION_NAME);
                hashMap.put(EXTRA_SIGNUP_TYPE, TYPE_EMAIL);

                reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                    hideProgress();
                    screens.showClearTopScreen(MainActivity.class);
                });
            }
        }).addOnFailureListener(e -> {
            hideProgress();
            screens.showToast(e.getMessage());
        }).addOnCanceledListener(this::hideProgress);
    }
}
