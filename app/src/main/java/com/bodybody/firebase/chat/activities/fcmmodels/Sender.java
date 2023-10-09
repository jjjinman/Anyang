package com.bodybody.firebase.chat.activities.fcmmodels;

//Sender 클래스를 생성해 데이터를 주고 받을 수 있도록 했습니다.
public class Sender {
    private final Data data;
    private final String to;

    public Sender(Data data, String to) {
        this.data = data;
        this.to = to;
    }
}
