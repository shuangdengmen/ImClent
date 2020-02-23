package com.men.imclent.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.men.imclent.presenter.SplashPresenter;
import com.men.imclent.view.ISplashView;

public class SplashPresenterImpl implements SplashPresenter {

    private ISplashView iView;

    public SplashPresenterImpl(ISplashView iView) {
        this.iView = iView;
    }

    @Override
    public void login() {
        if (EMClient.getInstance().isConnected()&&EMClient.getInstance().isLoggedInBefore()){
            iView.onLogin(true);
        }else {
            iView.onLogin(false);
        }
    }
}
