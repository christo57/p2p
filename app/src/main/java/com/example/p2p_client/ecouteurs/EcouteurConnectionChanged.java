package com.example.p2p_client.ecouteurs;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.example.p2p_client.ActivityServer;
import com.example.p2p_client.Activity_cli_ser;

public class EcouteurConnectionChanged extends android.content.BroadcastReceiver {

    private Activity_cli_ser activity;

    public EcouteurConnectionChanged(Activity_cli_ser activity){
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        Log.i(ActivityServer.TAG, "received action" + action);

        if(action.equals(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)){
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if(networkInfo.isConnected()){
                this.activity.getManager().requestConnectionInfo(this.activity.getChannel(), this.activity.getEcouteurInfoConnection());
            }
        }
    }
}
