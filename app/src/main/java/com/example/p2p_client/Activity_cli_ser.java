package com.example.p2p_client;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.TextView;

import com.example.p2p_client.ecouteurs.EcouteurConnectionChanged;
import com.example.p2p_client.ecouteurs.EcouteurInfoConnection;
import com.example.p2p_client.jeu.jeu;

import java.net.InetAddress;

public abstract class Activity_cli_ser extends Activity {

    public static final String TAG = "ACTIVITY CLIENT OU SERVER: ";

    protected WifiP2pManager manager;
    protected WifiP2pManager.Channel channel;

    protected InetAddress adresseServeur;
    protected TextView textAdresse;

    protected EcouteurConnectionChanged ecouteurConnectionChanged;
    protected EcouteurInfoConnection ecouteurInfoConnection;

    // This is the callback for the started sub-activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case jeu.DECONNEXION:
                this.quitter();
                break;
        }
    }

    //quitte la partie
    public void quitter(){
        this.deconnexion();
        setResult(10);
        super.finish();
    }

    //se deconnecte du groupê
    public void deconnexion(){
        Log.i(TAG, "deconexion");

        if (manager != null && channel != null) {
            manager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {

                public void onGroupInfoAvailable(WifiP2pGroup group) {
                    if (group != null && manager != null && channel != null) {
                        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {

                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "removeGroup onSuccess -");
                            }

                            @Override
                            public void onFailure(int reason) {
                                Log.d(TAG, "removeGroup onFailure -" + reason);
                            }
                        });
                    }
                }
            });
        }
    }



    public WifiP2pManager getManager(){
        return this.manager;
    }

    public WifiP2pManager.Channel getChannel(){
        return  this.channel;
    }

    public EcouteurInfoConnection getEcouteurInfoConnection(){
        return this.ecouteurInfoConnection;
    }

    public InetAddress getAdresseServeur() {
        return adresseServeur;
    }

    public void setAdresseServeur(InetAddress adresseServeur) {
        this.adresseServeur = adresseServeur;

        this.textAdresse.setText("adresse : " + this.adresseServeur.getHostAddress());
    }
}
