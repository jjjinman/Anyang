package com.bodybody.firebase.chat.activities.async;

import java.util.concurrent.Callable;
//사용자 정의 호출 가능
public interface CustomCallable<R> extends Callable<R> {
    void setDataAfterLoading(R result);

    void setUiForLoading();
}