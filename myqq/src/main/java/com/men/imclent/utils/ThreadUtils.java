package com.men.imclent.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadUtils {

    private static Handler handler =new Handler(Looper.getMainLooper());
    private static Executor executor = Executors.newSingleThreadExecutor();

    public static void runOnNOUI(Runnable r){
        executor.execute(r);
    }

    public static void runOnMainUI(Runnable r){
        handler.post(r);
    }
}
