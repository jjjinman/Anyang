package com.bodybody.firebase.chat.activities.fcm;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import com.bodybody.firebase.chat.activities.managers.SessionManager;
import com.bodybody.firebase.chat.activities.managers.Utils;


public class ApplicationLifecycleManager implements ActivityLifecycleCallbacks {


    private static int visibleActivityCount = 0;

    /**
     * 개방 된 상태 : 폐쇄된 상태의 activity는 0 또는 1이어야 합니다.
     *이 값이 activity B onstart () 사이에 확인되면 2가 됩니다.
     * activity A onstop ().
     */
    private static int foregroundActivityCount = 0;

    /**
     * 앱에 Foreground가 있으면 true 값을 반환합니다.
     */
    public static boolean isAppInForeground() {
        return foregroundActivityCount > 0;
    }

    /**
     * 앱의 activity가 보이면 true를 반환합니다 (장치가 활동을 멈추면
     * activity가 보입니다.)
     */
    public static boolean isAppVisible() {
        return visibleActivityCount > 0;
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (SessionManager.get().isRTLOn()) {
            Utils.RTLSupport(activity.getWindow());
        }
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
        foregroundActivityCount++;
    }

    public void onActivityPaused(Activity activity) {
        foregroundActivityCount--;
    }


    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityStarted(Activity activity) {
        visibleActivityCount++;
    }

    public void onActivityStopped(Activity activity) {
        visibleActivityCount--;
    }
}