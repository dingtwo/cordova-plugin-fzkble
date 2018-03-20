package com.example.plugin;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.mysirui.vehicle.dataModel.BleData;
import com.mysirui.vehicle.framework.MsgResult;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rx.functions.Action1;

public class Hello extends CordovaPlugin {


    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//适配6.0以上版本
//            requestPermissions(new String[]{P_LOCATION_COARSE}, new OnPermissionResult() {
//                @Override
//                public void onPermissionResult(boolean isPermit) {
//                    if (isPermit) {
//                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                        // 通过GPS卫星定位，定位精度高（适用于在室外和空旷的地方定位, 速度慢）
//                        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//                        if (!gps) {
//                            Toast.makeText(cordova.getActivity(), "蓝牙连接需要打开GPS", Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//
//                    }
//                }
//            });
//        }
    }

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("greet")) {

            String name = data.getString(0);
            String message = "Hello, " + name;
            callbackContext.success(message);

            return true;

        } else if (action.equals("connect")){
            Log.d("a", "开始亮亮亮亮亮");
            JSONObject obj = data.getJSONObject(0);
            String bluetooth_mac = obj.getString("mac");
            String bluetooth_key = obj.getString("key");
            String bluetooth_id = obj.getString("bluetoothID");

            BluetoothUtils.getInstance(cordova.getContext(), cordova.getActivity()).connectBle(bluetooth_mac, bluetooth_id, bluetooth_key);
            callbackContext.success("连接成功!!!!");
            return true;

        }else if (action.equals("openDoor")){
            BluetoothUtils.srBleSDK.command_unlock().subscribe(new Action1<MsgResult<BleData>>() {


                @Override
                public void call(MsgResult<BleData> bleDataMsgResult) {
                    if (bleDataMsgResult.isSucc()) {
                        //表示指令发送成功
                        LogUtil.ble_command("command_unlock指令发送成功");
                    } else {
                        //发送开门指令

                        LogUtil.ble_command("command_unlock" + "发送失败，失败原因：" + bleDataMsgResult.getMsg());
                    }
                }
            });
            Log.d("dd", "开门");
            return true;
        }else if (action.equals("closeDoor")) {
            BluetoothUtils.srBleSDK.command_lock().subscribe(new Action1<MsgResult<BleData>>() {


                @Override
                public void call(MsgResult<BleData> bleDataMsgResult) {
                    if (bleDataMsgResult.isSucc()) {
                        //表示指令发送成功
                        LogUtil.ble_command("command_unlock指令发送成功");
                        callbackContext.success("锁门成功!!!!");
                    } else {
                        //发送开门指令

                        LogUtil.ble_command("command_unlock" + "发送失败，失败原因：" + bleDataMsgResult.getMsg());
                        callbackContext.success("锁门失败!!!!");
                    }
                }
            });
            Log.d("vv", "关门");
            return true;
        }else if (action.equals("callCar")) {
            BluetoothUtils.srBleSDK.command_call().subscribe(new Action1<MsgResult<BleData>>() {


                @Override
                public void call(MsgResult<BleData> bleDataMsgResult) {
                    if (bleDataMsgResult.isSucc()) {
                        //表示指令发送成功
                        LogUtil.ble_command("command_unlock指令发送成功");
                        callbackContext.success("鸣笛成功!!!!");
                    } else {
                        //发送开门指令

                        LogUtil.ble_command("command_unlock" + "发送失败，失败原因：" + bleDataMsgResult.getMsg());
                        callbackContext.success("鸣笛失败!!!!");
                    }
                }
            });
            Log.d("dd", "鸣笛");
            return true;
        } else {
            return false;
        }
    }
}
