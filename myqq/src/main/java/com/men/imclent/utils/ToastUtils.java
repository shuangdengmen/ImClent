package com.men.imclent.utils;

import android.content.Context;
import android.widget.Toast;

import com.men.imclent.application.IMApplication;

public class ToastUtils {
    private static Toast toast;
   public static void showToast(Context context,String result){
       if (toast==null){
           toast = Toast.makeText(context,result,Toast.LENGTH_SHORT);
       }else {
           toast.setText(result);
       }
       toast.show();
   }
}
