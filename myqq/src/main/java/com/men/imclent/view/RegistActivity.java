package com.men.imclent.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.men.imclent.R;
import com.men.imclent.presenter.RegistPresenter;
import com.men.imclent.presenter.impl.RegistPresenterImpl;
import com.men.imclent.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegistActivity extends BaseActivity implements  IRegisterView {

    @InjectView(R.id.iv_avatar)
    ImageView ivAvatar;
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.til_username)
    TextInputLayout tilUsername;
    @InjectView(R.id.et_pwd)
    EditText etPwd;
    @InjectView(R.id.til_pwd)
    TextInputLayout tilPwd;
    @InjectView(R.id.btn_regist)
    Button btnRegist;
    private RegistPresenter registPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_regist);
        ButterKnife.inject(this);
        registPresenter = new RegistPresenterImpl(this);

    }

    @OnClick(R.id.btn_regist)
    public void onClick(View view) {
        String username= etUsername.getText().toString();
        String pwd =etPwd.getText().toString();
        if(!StringUtils.CheckUsername(username)){
           tilUsername.setErrorEnabled(true);
           tilUsername.setError("用户名不符合规则");
           return;
        }else {
            tilUsername.setErrorEnabled(false);
        }

        if(!StringUtils.Checkpwd(pwd)){
            tilPwd.setErrorEnabled(true);
            tilPwd.setError("密码不符合规则");
            return;
        }else {
            tilPwd.setErrorEnabled(false);
        }
        registPresenter.registUser(username,pwd);
    }

    @Override
    public void regist(String username, String pwd, boolean isSuccess, String fail) {
            if(isSuccess){
                showToast("注册成功1");
                startActivity(LoginActivity.class,true);
            }else {
                showToast("注册失败:"+fail);
            }
    }
}
