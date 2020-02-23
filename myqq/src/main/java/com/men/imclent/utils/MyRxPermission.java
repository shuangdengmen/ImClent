package com.men.imclent.utils;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.men.imclent.application.IMApplication;
import com.men.imclent.view.BaseFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public abstract class MyRxPermission {
    AppCompatActivity appCompatActivity;

    public MyRxPermission(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public void creator(){

        final RxPermissions rxPermission = new RxPermissions(appCompatActivity); // where this is an Activity or Fragment instance
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        Disposable disposable = rxPermission.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //申请的权限全部允许
                    Toast.makeText(IMApplication.appContext, "允许了权限!", Toast.LENGTH_SHORT).show();
                    //调用状态布局
                    initMyView();
                } else {
                    //只要有一个权限被拒绝，就会执行
                    Toast.makeText(IMApplication.appContext, "未授权权限，部分功能不能使用", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public abstract void initMyView();
}
