package com.bodybody.firebase.chat.activities.fcm;

import com.bodybody.firebase.chat.activities.fcmmodels.MyResponse;
import com.bodybody.firebase.chat.activities.fcmmodels.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization: key=AAAAOhvqwiM:APA91bF6wkeKnvgToikkrSqHu9uT62gorEvsfDzE_n57ARREujCeDDHj5l2VGjkLpNgEmd7wOhg37mu8JEBgsvVJ-42XYWIWRI56EuFzXpVuKpfM_lCL5mNOMeeo3C8YPh8FLQsx8l4c"
                   //파이어베이스 Cloud Messaging API 서버 키 받는 법
                    //해당 키를 받기 위해선 파이어베이스에서 프로젝트를 생성하고 사용자 인증으로
                    //이메일/비밀번호와 구글 로그인을 설정, 데이터베이스 생성후 규칙을 true로 바꾸고,
                    //스토리지도 마찬가지로 생성한 뒤 false를 true로 바꿔준 뒤, 프로젝트 설정에 들어가서
                    //데이터 지문을 추가해야 하는데 sha-1 지문이 없기 때문에 cmd 창을 열고
                    //C:\Users\사용자명\.android로 들어가줍니다. 그런 뒤
                    // keytool -list -v -keystore "C:\Users\{USER_NAME}\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
                    //해당 구문을 입력해 sha-1 코드를 받습니다. 그리고 다시 파이어베이스-> 프로젝트 설정에
                    //들어가서 데이터 지문 추가를 누르고 cmd 창에서 받았던 sha-1 코드를 입력하고 저장합니다.
                    //이후 google-service.json 파일을 새로 받고 안드로이드 스튜디오 -> 프로젝트로 전환한 뒤
                    //app에 google-service.json 파일을 넣어줍니다. 이후, 다시 파이어베이스로 들어가서
                    //프로젝트 설정 -> 클라우드 메세징에 들어가  Cloud Messaging API 서버 키를 복사한 뒤
                    //APIService의 "Authorization: key= "안에 복사한 키를 붙여줍니다.
                    //그런 뒤 광고나 현재 위치를 넣기 위해 build.gradle에서 키를 변경해줍니다.
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
