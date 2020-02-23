package com.men.imclent.presenter.impl;

import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.men.imclent.model.User;
import com.men.imclent.presenter.RegistPresenter;
import com.men.imclent.utils.ThreadUtils;
import com.men.imclent.view.IRegisterView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RegistPresenterImpl implements RegistPresenter {
    IRegisterView iRegisterView;
    private Observable<String> observable;

    public RegistPresenterImpl(IRegisterView iRegisterView) {
        this.iRegisterView = iRegisterView;
    }


    @Override
    public void registUser(String username, String pwd) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(pwd);

        user.signUp(new SaveListener<User>(){
            @Override
            public void done(User o, BmobException e) {
                if (e==null){
                    ThreadUtils.runOnNOUI(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(username,pwd);
                                ThreadUtils.runOnMainUI(new Runnable() {
                                    @Override
                                    public void run() {
                                        iRegisterView.regist(username,pwd,true,null);
                                    }
                                });
                            } catch (HyphenateException ex) {
                                ex.printStackTrace();
                                ThreadUtils.runOnMainUI(new Runnable() {
                                    @Override
                                    public void run() {
                                        user.delete(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                e.printStackTrace();
                                            }
                                        });
                                        iRegisterView.regist(username,pwd,false,e.getMessage());
                                    }
                                });
                            }
                        }
                    });
                }else {
                    iRegisterView.regist(username,pwd,false,e.getMessage());
                }
            }

        });
    }
}
