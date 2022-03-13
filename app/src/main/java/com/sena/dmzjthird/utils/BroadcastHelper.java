package com.sena.dmzjthird.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.BatteryManager;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * FileName: BroadcastHelper
 * Author: JiaoCan
 * Date: 2022/3/11 9:03
 */

public class BroadcastHelper {

    /**
     * 不能使用本地广播注册
     * 无法接收到系统广播
     */
    public static BroadcastReceiver getBatteryAndNetworkBroadcast(Context context, TextView tvBattery, TextView tvNetwork) {

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case Intent.ACTION_BATTERY_CHANGED:
                        int currentBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                        int maxBattery = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                        if (tvBattery != null) {
                            tvBattery.setText((currentBattery * 100) / maxBattery + "% 电量");
                        }
                        break;
                    case "android.net.conn.CONNECTIVITY_CHANGE":
                        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkCapabilities info = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                        if (tvNetwork == null) return ;
                        if (null != info) {
                            if (info.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                tvNetwork.setText("WIFI");
                            } else if (info.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                tvNetwork.setText("数据网络");
                            }
                        } else {
                            tvNetwork.setText("无连接");
                        }
                        break;

                    default:
                        break;
                }

            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        context.registerReceiver(receiver, filter);

        return receiver;
    }

    public static void unregisterBroadcast(Context context, BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
    }

}
