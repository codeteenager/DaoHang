package com.shuaijie.jiang.daohang.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkReceiver extends BroadcastReceiver {
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo netInfo;

    public NetworkReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION) || action.equals("android.intent.action.isNetworkConnected")) {
            mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = mConnectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isAvailable()) {
                //网络连接
                send(context, "innet");
            } else {
                //网络断开
                send(context, "outnet");
            }
        }
    }

    private void send(Context context, String mode) {
        Intent intent = new Intent();
        intent.putExtra("msg", mode);
        intent.setAction("android.intent.action.network");// action与接收器相同
        context.sendBroadcast(intent);
    }
}
