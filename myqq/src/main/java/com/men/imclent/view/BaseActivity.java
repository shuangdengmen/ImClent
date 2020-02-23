package com.men.imclent.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hyphenate.util.DateUtils;
import com.men.imclent.R;
import com.men.imclent.event.ExitEvent;
import com.men.imclent.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(new MyExitBroadcastReceiver() ,new IntentFilter("com.men.exit"));
    }

    protected  void startActivity(Class clazz,boolean isFinish){
        if(isFinish){

            Intent intent = new Intent(getApplicationContext(),clazz);
            startActivity(intent);
            overridePendingTransition(R.anim.toright,R.anim.toleft);
            finish();
        }else {
            Intent intent = new Intent(getApplicationContext(), clazz);
            startActivity(intent);
            overridePendingTransition(R.anim.toright,R.anim.toleft);
        }
    }

    public void showToast(String result){
        ToastUtils.showToast(getApplicationContext(),result);
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetExitEvent(ExitEvent event) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
        dialog.setTitle("你的账户与" + DateUtils.getTimestampStr() + "时在异地登录，请重新登录");
        dialog.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
                Intent intent = new Intent("com.men.exit");
                manager.sendBroadcast(intent);
                startActivity(LoginActivity.class, false);
            }
        });
    }

    class MyExitBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.men.exit")) {
                finish();
            }
        }
    }

}
