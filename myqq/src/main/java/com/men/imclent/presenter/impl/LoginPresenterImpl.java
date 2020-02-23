package com.men.imclent.presenter.impl;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.men.imclent.callback.MyEMCallBack;
import com.men.imclent.presenter.LoginPresenter;
import com.men.imclent.utils.ThreadUtils;
import com.men.imclent.view.ILoginView;

public class LoginPresenterImpl implements LoginPresenter {
    ILoginView iLoginView;

    public LoginPresenterImpl(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
    }

    @Override
    public void login(String useranme, String pwd) {

        EMClient.getInstance().login(useranme, pwd, new MyEMCallBack() {
            @Override
            public void success() {
                iLoginView.onLogin(useranme,true,null);
            }

            @Override
            public void error(int i, String s) {
                iLoginView.onLogin(useranme,false,s);
            }
        });

    }
}
