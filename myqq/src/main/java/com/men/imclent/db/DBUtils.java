package com.men.imclent.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBUtils {
    private  static Context context =null;

    public static void initDBUtils(Context context) {
        DBUtils.context=context;
    }

    public static List<String> initContact(String username) {
        List<String>    result = new ArrayList<String>();
        if (context == null) {
            throw new RuntimeException("请初始化application");
        }else {
            ContactOpenHelper openHelper = new ContactOpenHelper(context);
            SQLiteDatabase database = openHelper.getReadableDatabase();
            Cursor cursor = database.query("contact_info", new String[]{"contact"}, "username=?", new String[]{username}, null, null, null);
            while (cursor.moveToNext()){
                String name = cursor.getString(0);
                result.add(name);
            }
            cursor.close();
            openHelper.close();
            return result;
        }

    }

    public static void updateContactFromEmServer (String username,List<String> contacts){
        if (context == null) {
            throw new RuntimeException("请初始化application");
        }else {
            ContactOpenHelper openHelper =new ContactOpenHelper(context);
            SQLiteDatabase database = openHelper.getReadableDatabase();
            database.beginTransaction();
            try {
                database.setTransactionSuccessful();
                database.delete("contact_info","username=?",new String[]{username});
                ContentValues contentValues = new ContentValues();
                contentValues.put("username",username);
                for (String contact : contacts) {
                    contentValues.put("contact",contact);
                    database.insert("contact_info", null, contentValues);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                database.endTransaction();
            }

        }
    }
}
