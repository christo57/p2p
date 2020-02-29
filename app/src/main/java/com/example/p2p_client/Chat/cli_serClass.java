package com.example.p2p_client.Chat;

public abstract class cli_serClass extends Thread{

    //allow to receive string
    public abstract void receive(String texte);
}
