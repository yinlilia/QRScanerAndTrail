package com.samonxu.qrcode.demo.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinlili on 2017/5/19.
 */

public class DBManager {
    private DataHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context)
    {
        //Log.d(AppConstants.LOG_TAG, "DBManager --> Constructor");
        helper = new DataHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add persons
     *
     * @param histories
     */
    public void add(List<History> histories)
    {
        //Log.d(AppConstants.LOG_TAG, "DBManager --> add");
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try
        {
            for (History history : histories)
            {
                /*db.execSQL("INSERT INTO " + DataHelper.TABLE_NAME
                        + "VALUES(?,?,?)", new Object[] { history.name,
                        history.macAddress, history.time });*/
                db.execSQL("INSERT INTO " + DataHelper.TABLE_NAME
                        + " VALUES(null, ?, ?, ?, ?)", new Object[] { history.name,history.macAddress,
                        history.time, history.time1 });
                // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
                // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
                // 使用占位符有效区分了这种情况
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        }
        finally
        {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * update person's age
     *
     * @param person
     */
    public void updateAge(History person)
    {
        //Log.d(AppConstants.LOG_TAG, "DBManager --> updateAge");
        ContentValues cv = new ContentValues();
        cv.put("age", person.macAddress);
        db.update(DataHelper.TABLE_NAME, cv, "name = ?",
                new String[] { person.name });
    }

    /**
     * delete old person
     *
     * @param history
     */
    public void deleteOldPerson(History history)
    {
        //Log.d(AppConstants.LOG_TAG, "DBManager --> deleteOldPerson");
        /*db.delete(DataHelper.TABLE_NAME, "age >= ?",
                new String[] { String.valueOf(history.macAddress) });*/
        db.execSQL("delete from PersonTable");
    }

    /**
     * query all persons, return list
     *
     * @return List<Person>
     */
    public List<History> query()
    {
        //Log.d(AppConstants.LOG_TAG, "DBManager --> query");
        ArrayList<History> histories = new ArrayList<History>();
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
            History history = new History();
            history.number = c.getString(c.getColumnIndex("_id"));
            history.name = c.getString(c.getColumnIndex("name"));
            history.macAddress=c.getString(c.getColumnIndex("macAddress"));
            history.time = c.getString(c.getColumnIndex("age"));
            history.time1 = c.getString(c.getColumnIndex("info"));
            histories.add(history);
        }
        c.close();
        return histories;
    }

    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor()
    {
        //Log.d(AppConstants.LOG_TAG, "DBManager --> queryTheCursor");
        Cursor c = db.rawQuery("SELECT * FROM " + DataHelper.TABLE_NAME,
                null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB()
    {
        //Log.d(AppConstants.LOG_TAG, "DBManager --> closeDB");
        // 释放数据库资源
        db.close();
    }

}
