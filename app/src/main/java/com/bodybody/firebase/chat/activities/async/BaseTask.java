package com.bodybody.firebase.chat.activities.async;

//기본 작업 (로딩 작업을 위해 Ui 설정, DataAfterLoading 설정 등)
public abstract class BaseTask<R> implements CustomCallable<R> {
    @Override
    public void setUiForLoading() {

    }

    @Override
    public void setDataAfterLoading(R result) {

    }

    @Override
    public R call() throws Exception {
        return null;
    }
}