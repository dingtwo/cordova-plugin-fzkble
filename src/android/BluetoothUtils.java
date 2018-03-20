package com.example.plugin;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.mysirui.vehicle.SRBleSDK;
import com.mysirui.vehicle.dataModel.BleData;
import com.mysirui.vehicle.dataModel.StatusItem;
import com.mysirui.vehicle.framework.IStatusListener;
import com.mysirui.vehicle.framework.MsgResult;

import java.util.List;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by zxh on 2017/11/20.
 * 蓝牙封装类
 */

public class BluetoothUtils {
    private static BluetoothUtils bluetoothUtils;


    private static Context mContext;
    //蓝牙封装类
    public static SRBleSDK srBleSDK;

    public static BluetoothUtils getInstance(Context context, Activity mActivity) {
        mContext = context;
        if (bluetoothUtils == null) {
            bluetoothUtils = new BluetoothUtils();
        }
        //绑定activity，初始化蓝牙sdk
        srBleSDK = SRBleSDK.with(mActivity).setReleaseImmediately(true);
        return bluetoothUtils;
    }

    /**
     * 执行连接操作
     *
     * @param mac
     * @param idStr
     * @param keyStr
     */
    public void connectBle(String mac, String idStr, String keyStr) {
        BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
        boolean isOpen = blueadapter.isEnabled();
        if (!isOpen) {
            blueadapter.enable();
            return;
        }

        if (TextUtils.isEmpty(mac) || TextUtils.isEmpty(idStr) || TextUtils.isEmpty(keyStr)) {
            Toast.makeText(mContext, "请填写mac，id，key", Toast.LENGTH_LONG).show();
            return;
        }
        LogUtil.ble_state("mac:" + mac + "  id:" + idStr + "  key:" + keyStr);
        /**连接蓝牙，并添加所有监听*/
        //认证成功回调，表示连接成功，并且认证成功
        srBleSDK.
                onLogin(new Action0() {
                    @Override
                    public void call() {
                        LogUtil.ble_state("认证成功");
                    }
                })
                //连接成功回调
                .onConnect(new Action0() {
                    @Override
                    public void call() {
                        LogUtil.ble_state("连接成功");
                    }
                })
                //连接中回调
                .onConnecting(new Action0() {
                    @Override
                    public void call() {
                        LogUtil.ble_state("正在连接中");
                    }
                })
                //断开连接回调
                .onDisconn(new Action0() {
                    @Override
                    public void call() {
                        LogUtil.ble_state("连接失败或断开");
                    }
                })
                //蓝牙数据回调
                .onStatusChange(new IStatusListener() {
                    @Override
                    public void onStatusChange(List<StatusItem> list) {
                        if (list == null || list.size() == 0) {
                            LogUtil.ble_state("蓝牙返回数据为空");
                            return;
                        }
                        for (int i = 0; i < list.size(); i++) {
                            LogUtil.ble_state("蓝牙数据：key=" + list.get(i).getStatusKey() + "  value=" + list.get(i).getValue() + "  state=" + list.get(i).getStatus());
                        }
                    }
                }).start(mac, idStr, keyStr);
    }


    /**
     * 断开蓝牙操作
     */
    public void stopBluetooth() {
        srBleSDK.stop();
    }

    /**
     * 锁车门
     */
    public String btlock() {
        final String[] strClose = new String[1];
        //发送上锁指令
        srBleSDK.command_lock().subscribe(new Action1<MsgResult<BleData>>() {
            @Override
            public void call(MsgResult<BleData> bleDataMsgResult) {
                if (bleDataMsgResult.isSucc()) {
                    strClose[0] = "1";
                    LogUtil.ble_command("command_lock指令发送成功");
                    Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                } else {
                    strClose[0] = "2";
                    Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
                    LogUtil.ble_command("command_lock" + "发送失败，失败原因：" + bleDataMsgResult.getMsg());
                }
            }
        });
        return strClose[0];
    }

    /**
     * 解锁车门
     */
    public String btunlock() {
        final String[] str = new String[1];

        //发送解锁指令
        srBleSDK.command_unlock().subscribe(new Action1<MsgResult<BleData>>() {


            @Override
            public void call(MsgResult<BleData> bleDataMsgResult) {
                if (bleDataMsgResult.isSucc()) {
                    str[0] = "1";
                    Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                    //表示指令发送成功
                    LogUtil.ble_command("command_unlock指令发送成功");
                } else {
                    str[0] = "2";
                    Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
                    LogUtil.ble_command("command_unlock" + "发送失败，失败原因：" + bleDataMsgResult.getMsg());
                }
            }
        });
        return str[0];
    }
}
