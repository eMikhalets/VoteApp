package com.supercasual.fourtop.network;

public interface VolleyCallBack {

    default public void onSuccess() {}
    default public void onSuccess(int status) {}
}
