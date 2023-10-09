package com.bodybody.firebase.chat.activities.models;

import java.io.Serializable;

// 기타 클래스는 메세지 작성 관련 내용들을 관여합니다.
public class Others implements Serializable {

    private boolean typing;
    private String typingwith;

    public Others() {
    }

    public Others(boolean typing) {
        this.typing = typing;
    }

    public boolean isTyping() {
        return typing;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }

    public String getTypingwith() {
        return typingwith;
    }

    public void setTypingwith(String typingwith) {
        this.typingwith = typingwith;
    }
}
