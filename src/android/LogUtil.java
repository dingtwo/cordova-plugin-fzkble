package com.example.plugin;

import android.util.Log;

/**
 * creat by lucky_code at 2017/10/28
 */
public class LogUtil {
    public static void ble_state(String msg){
        Log.d("ble_state",msg);
    }
    public static void ble_command(String msg){
        Log.d("ble_command",msg);
    }
}
