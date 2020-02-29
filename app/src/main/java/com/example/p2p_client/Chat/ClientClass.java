package com.example.p2p_client.Chat;

import com.example.p2p_client.jeu.jeuClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;


//thread client
public class ClientClass extends cli_serClass {

    private Socket socket;
    private String hostAdd;
    private SendReceive sendReceive;
    private jeuClient jeuClient;

    //constructor
    public ClientClass(jeuClient jeuClient, InetAddress hostAdress) {
        this.jeuClient = jeuClient;
        this.hostAdd = hostAdress.getHostAddress();
        this.socket = new Socket();
    }

    //run
    @Override
    public void run() {
        try {
            this.socket.connect(new InetSocketAddress(hostAdd, 8888), 500);
            this.sendReceive = new SendReceive(this.socket,this);
            this.sendReceive.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //allox to receive string
    public void receive(String texte){
        this.jeuClient.receive(texte);
    }

    //-------------- getter and setter ------------------------

    public SendReceive getSendReceive(){
        return this.sendReceive;
    }
}

