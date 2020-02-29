package com.example.p2p_client.ecouteurs;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.example.p2p_client.ActivityClient;
import com.example.p2p_client.ActivityServer;
import com.example.p2p_client.Activity_cli_ser;

import java.net.InetAddress;

public class EcouteurInfoConnection implements WifiP2pManager.ConnectionInfoListener {

    private Activity_cli_ser activity;
    private boolean isFirstConnexion;

    public EcouteurInfoConnection(Activity_cli_ser activity) {
        this.activity = activity;
        this.isFirstConnexion = true;
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        //for server
        if (info.groupFormed && info.isGroupOwner && !isFirstConnexion) {
            Log.i(ActivityServer.TAG, "group formed et is group owner : adresse : " + activity.getAdresseServeur().toString());
            final Intent jeuServer = new Intent(activity, com.example.p2p_client.jeu.jeuServer.class);
            activity.startActivityForResult(jeuServer, 10);
        }

        //for client
        if (info.groupFormed && !info.isGroupOwner) {
            InetAddress adresseServeur = info.groupOwnerAddress;
            this.activity.setAdresseServeur(adresseServeur);
            final Intent jeuClient = new Intent(activity, com.example.p2p_client.jeu.jeuClient.class);
            jeuClient.putExtra("adresse", activity.getAdresseServeur());
            jeuClient.putExtra("nom", ((ActivityClient) activity).getNomServeur());
            activity.startActivityForResult(jeuClient, 10);
        }

        //first connexion for server
        if (isFirstConnexion) {
            isFirstConnexion = false;
            InetAddress adresseServeur = info.groupOwnerAddress;
            this.activity.setAdresseServeur(adresseServeur);
        }
    }

}
