package com.men.imclent.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.men.imclent.callback.MyEMCallBack;
import com.men.imclent.presenter.LogoutPresenter;
import com.men.imclent.view.IPluginView;

public class LogoutPresenterImpl implements LogoutPresenter {

    IPluginView iPluginView;
    public LogoutPresenterImpl(IPluginView iPluginView) {
        this.iPluginView = iPluginView;
    }
    @Override
    public void logout() {
        EMClient.getInstance().logout(true, new MyEMCallBack() {
            @Override
            public void success() {
                iPluginView.logout(true,null);
            }

            @Override
            public void error(int i, String s) {
                iPluginView.logout(false,s);
            }
        });
    }
}
