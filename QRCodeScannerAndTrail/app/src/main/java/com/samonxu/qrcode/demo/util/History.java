package com.samonxu.qrcode.demo.util;

import android.content.SharedPreferences;

/**
 * Created by yinlili on 2017/5/19.
 */

public class History {
    public String name;
    public String time;
    public String time1;
    public String number;
    public String macAddress;

    public History()
    {
    }

    public History(String number,String name,String macAddress, String time, String time1)
    {
        this.number=number;
        this.name = name;
        this.macAddress=macAddress;
        this.time = time;
        this.time1 = time1;
    }
}
