package com.bodybody.firebase.chat.activities.fcmmodels;

//Refresh Token 의 경우 보안을 위해서 만든 토큰으로
//소셜 로그인을 위해서 필요하기 때문에 해당 클래스를 선언해주었습니다.
public class Token {
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public Token() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
