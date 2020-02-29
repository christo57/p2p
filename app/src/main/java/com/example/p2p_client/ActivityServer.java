package com.example.p2p_client;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;

import com.example.p2p_client.ecouteurs.EcouteurConnectionChanged;
import com.example.p2p_client.ecouteurs.EcouteurInfoConnection;

import java.net.InetAddress;

public class ActivityServer extends Activity_cli_ser {

    public static final String TAG = "ACTIVITY SERVER : ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        Log.i(TAG, "started");

        this.manager=(WifiP2pManager)this.getSystemService(Context.WIFI_P2P_SERVICE);
        this.channel = this.manager.initialize(this,this.getMainLooper(), null);

        this.ecouteurConnectionChanged = new EcouteurConnectionChanged(this);
        this.ecouteurInfoConnection = new EcouteurInfoConnection(this);

        this.manager.createGroup(this.channel, null);

        this.textAdresse = findViewById(R.id.textAdresse);
    }

    protected  void onResume(){
        super.onResume();
        IntentFilter filtre = new IntentFilter();
        filtre.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        this.registerReceiver(this.ecouteurConnectionChanged, filtre);
    }

    protected void onPause(){
        super.onPause();
        this.unregisterReceiver(this.ecouteurConnectionChanged);
    }
}
