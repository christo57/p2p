package com.example.p2p_client.Chat;

import com.example.p2p_client.jeu.jeuServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//thread server
public class ServerClass extends cli_serClass {

    private Socket socket;
    private ServerSocket serverSocket;
    private SendReceive sendReceive;
    private jeuServer jeuServer;

    //constructor
    public ServerClass(jeuServer jeuServer){
        this.jeuServer = jeuServer;
    }

    //run
    @Override
    public void run(){
        try{
            this.serverSocket = new ServerSocket(8888);
            this.socket = serverSocket.accept();
            this.sendReceive = new SendReceive(this.socket,this);
            this.sendReceive.start();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //allow to receive string
    public void receive(String texte){
        this.jeuServer.receive(texte);
    }

    //----------- getter and setter --------------

    public SendReceive getSendReceive(){
        return this.sendReceive;
    }
}
