package com.men.imclent.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.men.imclent.MainActivity;
import com.men.imclent.R;
import com.men.imclent.presenter.SplashPresenter;
import com.men.imclent.presenter.impl.SplashPresenterImpl;
import com.men.imclent.utils.MyRxPermission;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashActivity extends BaseActivity implements ISplashView {

    SplashPresenter splashPresenter;
    @InjectView(R.id.splash_image)
    ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        splashPresenter = new SplashPresenterImpl(this);
        splashPresenter.login();
        initData();
    }

    private void initData() {

    }

    @Override
    public void onLogin(boolean isLogin) {
        if(isLogin){
            startActivity(MainActivity.class,true);
        }else {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(splashImage, "alpha", 0, 1).setDuration(3000);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    startActivity(LoginActivity.class,true);
                }
            });
            objectAnimator.start();
        }
    }
}
