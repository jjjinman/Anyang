package com.bodybody.firebase.chat.activities;

import static com.bodybody.firebase.chat.activities.constants.IConstants.ZERO;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bodybody.firebase.chat.activities.managers.Utils;
import com.google.firebase.auth.FirebaseAuth;


//ForgetPasswordActivity는 베이스 액티비티를 상속 받으며,
//이메일 혹은 비밀번호를 잊었을 때 보여지는 액티비티입니다.
//잘못 입력했을 때나 입력하지 않고 로그인을 시도했을 때 보여지는 메세지 등을
//보여줍니다.
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        backButton();
        txtEmail = findViewById(R.id.txtEmail);

        final Button btnSend = findViewById(R.id.btnSend);

        auth = FirebaseAuth.getInstance();

        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSend) {
            final String strEmail = txtEmail.getText().toString().trim();

            if (Utils.isEmpty(strEmail)) {
                screens.showToast(R.string.strAllFieldsRequired);
            } else if (!Utils.isValidEmail(strEmail)) {
                screens.showToast(R.string.strInvalidEmail);
            } else {
                showProgress();
                auth.sendPasswordResetEmail(strEmail).addOnCompleteListener(task -> {
                    hideProgress();
                    if (task.isSuccessful()) {
                        Utils.showOKDialog(mActivity, ZERO, R.string.lblSendYouForgetEmail,
                                () -> screens.showClearTopScreen(LoginActivity.class));
                    }
                });
            }
        }
    }

}
