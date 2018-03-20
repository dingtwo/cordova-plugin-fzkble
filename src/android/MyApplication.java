package com.example.plugin;

import android.app.Application;

import com.mysirui.vehicle.SRBleSDK;
/**
 * creat by lucky_code at 2017/10/28
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //蓝牙sdk初始化
        SRBleSDK.iniWithApplication(this);
    }
}
