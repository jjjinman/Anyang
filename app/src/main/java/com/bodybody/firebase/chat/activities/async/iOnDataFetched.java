package com.bodybody.firebase.chat.activities.async;
//데이터 가져오기 (프로그래스바 숨기기, 보여주기 여부/ 데이터 설정)
public interface iOnDataFetched {
    void showProgressBar(int progress);

    void hideProgressBar();

    void setDataInPageWithResult(Object result);
}