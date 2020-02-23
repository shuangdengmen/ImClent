package com.men.imclent.application;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.men.imclent.MainActivity;
import com.men.imclent.R;
import com.men.imclent.db.DBUtils;
import com.men.imclent.event.ContactChangeEvent;
import com.men.imclent.event.ExitEvent;
import com.men.imclent.utils.ThreadUtils;
import com.men.imclent.view.ChatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

public class IMApplication extends Application {
    public static IMApplication appContext;
    private int foregroundSound;
    private int backgroundSound;
    private SoundPool soundPool;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
//环信
        initEMClint();

        initEMReceive();
//bmob应用
        Bmob.initialize(this, "e31e22da54e8ef0a984930f768a941dc");
//数据库
        DBUtils.initDBUtils(this);
        initSoundPool();
    }

    private void initSoundPool() {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        foregroundSound = soundPool.load(getApplicationContext(), R.raw.duan, 1);
        backgroundSound = soundPool.load(getApplicationContext(), R.raw.yulu, 1);

    }

    private void initEMClint() {


        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);

        appContext = this;
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(appContext.getPackageName())) {

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
//初始化
        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
//注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

    }

    private void initEMReceive() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String username) {
                EventBus.getDefault().post(new ContactChangeEvent(username, true));
            }

            @Override
            public void onContactDeleted(String username) {
                EventBus.getDefault().post(new ContactChangeEvent(username, false));
            }

            @Override
            public void onContactInvited(String username, String s1) {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(username);
//                    EMClient.getInstance().contactManager().declineInvitation(username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFriendRequestAccepted(String s) {

            }

            @Override
            public void onFriendRequestDeclined(String s) {

            }
        });

        //监听消息
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                EventBus.getDefault().post(list);

                if (isBackgroundState()) {
                    soundPool.play(backgroundSound, 1, 1, 0, 0, 1);
                    sendNoti(list);
                } else {
                    soundPool.play(foregroundSound, 1, 1, 0, 0, 1);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });
    }

    private void sendNoti(List<EMMessage> list) {
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.message);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.default_avatar ));
        EMMessage emMessage = list.get(0);
        EMTextMessageBody body = (EMTextMessageBody) emMessage.getBody();
        builder.setContentTitle(emMessage.getUserName());
        builder.setContentText(body.getMessage());
        //TODO 增加这个内容
        builder.setContentInfo("cececececece");

        Intent chatIntent = new Intent(getApplicationContext(),ChatActivity.class);
        chatIntent.putExtra("contact", emMessage.getUserName());
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        Intent[] intents = new Intent[]{mainIntent,chatIntent};
        PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 1, intents, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,notification);

    }

    private boolean isBackgroundState() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(50);
        ActivityManager.RunningTaskInfo runningTaskInfo = tasks.get(0);
        ComponentName componentName = runningTaskInfo.topActivity;
        if (componentName.getPackageName().equals(getPackageName())) {
            //处于前台
            Log.d("isBackgroundState", "isBackgroundState" + "true");
            return false;
        } else {
            Log.d("isBackgroundState", "isBackgroundState" + "false");
            return true;
        }


    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
                e.printStackTrace();
            }
        }
        return processName;
    }


    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            ThreadUtils.runOnMainUI(new Runnable() {

                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        Log.d("MyConnectionListener", "MyConnectionListener.....");
                        // 显示帐号在其他设备登录
                        EventBus.getDefault().post(new ExitEvent(EMError.USER_LOGIN_ANOTHER_DEVICE));
                    } else {
                        if (NetUtils.hasNetwork(getApplicationContext())) {

                            //连接不到聊天服务器
                        }else {
                            //当前网络不可用，请检查网络设置
                        }
                    }
                }
            });
        }
    }
}
