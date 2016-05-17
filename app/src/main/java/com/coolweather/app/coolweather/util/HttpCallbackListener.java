package com.coolweather.app.coolweather.util;

/**
 * Created by syt on 16/5/16.
 */
public interface HttpCallbackListener {
    public abstract void onFinish(String response);
    public abstract void onError(Exception e);
}
