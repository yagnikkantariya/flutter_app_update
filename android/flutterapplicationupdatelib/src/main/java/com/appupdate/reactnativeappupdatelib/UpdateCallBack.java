package com.appupdate.reactnativeappupdatelib;


public interface UpdateCallBack {
    void onSuccess(String response);
    void onFailure(String message);
}
