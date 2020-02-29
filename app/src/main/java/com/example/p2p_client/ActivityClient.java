package com.example.p2p_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.p2p_client.ecouteurs.EcouteurConnectionChanged;
import com.example.p2p_client.ecouteurs.EcouteurInfoConnection;
import com.example.p2p_client.ecouteurs.ecouteursClient.EcouteurPeersChanged;
import com.example.p2p_client.ecouteurs.ecouteursClient.MonPeerListener;
import com.example.p2p_client.jeu.jeu;

import java.net.InetAddress;
import java.util.Collection;

public class ActivityClient extends Activity_cli_ser {

    public static final String TAG = "ACTIVITY CLIENT : ";

    private EcouteurPeersChanged ecouteurPeersChanged;
    private MonPeerListener monPeerListener;

    private String nomServeur;

    private Collection<WifiP2pDevice> listeAppareilsAProximite;
    private String[] nomsAppareilsAProximite;


    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        this.manager = (WifiP2pManager) this.getSystemService(Context.WIFI_P2P_SERVICE);
        this.channel = this.manager.initialize(this, this.getMainLooper(), null);

        this.ecouteurPeersChanged = new EcouteurPeersChanged(this);
        this.monPeerListener = new MonPeerListener(this);
        this.ecouteurConnectionChanged = new EcouteurConnectionChanged(this);
        this.ecouteurInfoConnection = new EcouteurInfoConnection(this);

        this.textAdresse = findViewById(R.id.textConnected);
    }

    public void onResume(){
        super.onResume();

        IntentFilter filtre = new IntentFilter();
        filtre.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        this.registerReceiver(this.ecouteurPeersChanged, filtre);

        filtre = new IntentFilter();
        filtre.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        this.registerReceiver(this.ecouteurConnectionChanged, filtre);

        this.manager.discoverPeers(this.channel, null);
    }

    public void onPause(){
        super.onPause();

        unregisterReceiver(this.ecouteurPeersChanged);
        unregisterReceiver(this.ecouteurConnectionChanged);
    }

    public void updateAppareils(Collection<WifiP2pDevice> deviceList){

        //set parameters
        this.listeAppareilsAProximite = deviceList;
        this.nomsAppareilsAProximite = this.filtreNoms(this.listeAppareilsAProximite);

        //set list view
        this.listView = (ListView) findViewById(R.id.ListView);

        ArrayAdapter<String> noms = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , this.nomsAppareilsAProximite);

        this.listView.setAdapter(noms);

        //add listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //on recupere l'id du site cliqué
                String nom = (String)parent.getItemAtPosition(position);
                Log.i(ActivityClient.TAG, nom);

                WifiP2pDevice appareil = null;

                for(WifiP2pDevice device : listeAppareilsAProximite) {
                    Log.i(ActivityClient.TAG, device.deviceName);
                    if (device.deviceName.equals(nom)) {
                        appareil = device;
                        Log.i(ActivityClient.TAG, device.deviceName + " = " + nom);
                        break;
                    }
                }
                if(appareil != null){
                    WifiP2pConfig config = new WifiP2pConfig();
                    config.wps.setup = WpsInfo.PBC;
                    config.deviceAddress = appareil.deviceAddress;
                    nomServeur = appareil.deviceName;

                    manager.connect(channel, config, null);
                }
            }
        });
    }

    public String[] filtreNoms(Collection<WifiP2pDevice> listeAppareilsAProximite){
        String[] t = new String[listeAppareilsAProximite.size()];

        int i = -1;

        for(WifiP2pDevice d: listeAppareilsAProximite){
            t[++i] = d.deviceName;
        }

        return t;
    }


    public WifiP2pManager getManager() {
        return manager;
    }

    public WifiP2pManager.Channel getChannel() {
        return channel;
    }

    public EcouteurPeersChanged getEcouteurPeersChanged() {
        return ecouteurPeersChanged;
    }

    public MonPeerListener getMonPeerListener() {
        return monPeerListener;
    }

    public EcouteurConnectionChanged getEcouteurConnectionChanged() {
        return ecouteurConnectionChanged;
    }

    public EcouteurInfoConnection getEcouteurInfoConnection() {
        return ecouteurInfoConnection;
    }

    public Collection<WifiP2pDevice> getListeAppareilsAProximite() {
        return listeAppareilsAProximite;
    }

    public String[] getNomsAppareilsAProximite() {
        return nomsAppareilsAProximite;
    }

    public void setListeAppareilsAProximite(Collection<WifiP2pDevice> listeAppareilsAProximite) {
        this.listeAppareilsAProximite = listeAppareilsAProximite;
    }

    public void setNomsAppareilsAProximite(String[] nomsAppareilsAProximite) {
        this.nomsAppareilsAProximite = nomsAppareilsAProximite;
    }

    public InetAddress getAdresseServeur(){
        return this.adresseServeur;
    }

    public void setAdresseServeur(InetAddress adresseServeur){
        this.adresseServeur = adresseServeur;

        this.textAdresse.setText(this.textAdresse.getText() + " " + this.nomServeur + " : " + this.adresseServeur.getHostAddress());
    }

    public String getNomServeur(){
        return this.nomServeur;
    }
}
