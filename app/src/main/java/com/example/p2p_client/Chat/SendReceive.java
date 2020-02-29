package com.example.p2p_client.Chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

//thread to send and receive texte
public class SendReceive extends Thread{

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private cli_serClass cli_ser;

    //constructor
    public SendReceive(Socket skt, cli_serClass cli_ser){
        this.cli_ser = cli_ser;
        this.socket = skt;
        try {
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //methode run
    @Override
    public void run(){
        byte[] buffer = new byte[1024];
        int bytes;

        while(socket!=null){
            try {
                bytes = inputStream.read(buffer);
                if(bytes > 0){
                    //send message to target
                    handler.getInstance(this).handler.obtainMessage(handler.MESSAGE_READ,bytes,-1,buffer).sendToTarget();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //write into the outputStream
    public void write(byte[] bytes){
        try {
            outputStream.write(bytes);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //allow to receive texte
    public void receive(String texte){
        this.cli_ser.receive(texte);
    }
}

