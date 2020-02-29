package com.example.p2p_client.ecouteurs.ecouteursClient;


import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;


import com.example.p2p_client.ActivityClient;


public class MonPeerListener implements WifiP2pManager.PeerListListener {

    private ActivityClient activity;

    public MonPeerListener(ActivityClient activity){
        this.activity = activity;
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        Log.i(ActivityClient.TAG, "appel mathode onpeersavailable");

        this.activity.updateAppareils(peers.getDeviceList());
   }
}
