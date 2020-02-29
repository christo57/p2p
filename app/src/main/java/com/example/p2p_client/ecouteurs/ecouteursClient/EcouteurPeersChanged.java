package com.example.p2p_client.ecouteurs.ecouteursClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.example.p2p_client.ActivityClient;

public class EcouteurPeersChanged extends BroadcastReceiver {

    private ActivityClient activity;

    public EcouteurPeersChanged(ActivityClient activity){
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.i(ActivityClient.TAG, "received action " + action);

        if (action.equals(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)) {
            this.activity.getManager().requestPeers(activity.getChannel(), activity.getMonPeerListener());
        }
    }
}
