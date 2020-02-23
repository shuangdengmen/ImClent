package com.men.imclent.callback;

import com.hyphenate.EMCallBack;
import com.men.imclent.utils.ThreadUtils;

public abstract class MyEMCallBack implements EMCallBack {
    public abstract void success();
    public abstract void error(int i,String  s);
    @Override
    public void onSuccess() {
        ThreadUtils.runOnMainUI(new Runnable() {
            @Override
            public void run() {
                success();
            }
        });
    }

    @Override
    public void onError(int i, String s) {
        ThreadUtils.runOnMainUI(new Runnable() {
            @Override
            public void run() {
                error(i,s);
            }
        });
    }

    @Override
    public void onProgress(int i, String s) {

    }
}
