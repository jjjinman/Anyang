package com.bodybody.firebase.chat.activities.managers;


import static com.bodybody.firebase.chat.activities.constants.IConstants.FALSE;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TRUE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


// 세션 관리자 클래스를 생성한 이유는 앱이 종료되어도 데이터가 보존되어야
// 하기 때문에 데이터를 저장하기 위해 사용합니다.
public class SessionManager {
    // 파일 이름 환경 설정
    // 이렇게 상단에 설정해 줌으로써 작업을 보다 수월하게 진행할 수 있습니다.
    private static final String PREF_NAME = "BytesBeeChatV1";
    private static final String KEY_ON_OFF_NOTIFICATION = "onOffNotification";
    private static final String KEY_ON_OFF_RTL = "onOffRTL";
    private static final String KEY_ONBOARDING = "isOnBoardingDone";
    private final SharedPreferences pref;



    private static SessionManager mInstance;

    public static SessionManager get() {
        return mInstance;
    }

    public static void init(Context ctx) {
        if (mInstance == null) mInstance = new SessionManager(ctx);
    }



    public SessionManager(final Context context) {
        pref = context.getSharedPreferences(context.getPackageName() + PREF_NAME, 0);
    }

    public void setOnOffNotification(final boolean value) {
        final Editor editor = pref.edit();
        editor.putBoolean(KEY_ON_OFF_NOTIFICATION, value);
        editor.apply();
    }

    public boolean isNotificationOn() {
        return pref.getBoolean(KEY_ON_OFF_NOTIFICATION, TRUE);
    }

    public void setOnOffRTL(final boolean value) {
        final Editor editor = pref.edit();
        editor.putBoolean(KEY_ON_OFF_RTL, value);
        editor.apply();
    }

    public boolean isRTLOn() {
        return pref.getBoolean(KEY_ON_OFF_RTL, FALSE);
    }

    public void setOnBoardingDone(final boolean value) {
        final Editor editor = pref.edit();
        editor.putBoolean(KEY_ONBOARDING, value);
        editor.apply();
    }

    public boolean isOnBoardingDone() {
        return pref.getBoolean(KEY_ONBOARDING, FALSE);
    }

    public void clearAll() {
        final Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
