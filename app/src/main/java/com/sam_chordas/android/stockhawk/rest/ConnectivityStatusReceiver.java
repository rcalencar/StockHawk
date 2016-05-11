package com.sam_chordas.android.stockhawk.rest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by rodrigo.alencar on 5/10/16.
 */
public class ConnectivityStatusReceiver extends BroadcastReceiver {
    private static final String TAG = "ConnectivityStatus";

    private Listener listener;

    public ConnectivityStatusReceiver() {
    }

    public ConnectivityStatusReceiver(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        Log.d(TAG, String.format("action = %s, connected = %s", action, isConnected));
        listener.updateConnectivityStatus(isConnected);
    }

    public interface Listener {
        void updateConnectivityStatus(boolean connected);
    }
}
