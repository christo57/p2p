package com.example.p2p_client.Chat;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

//singleton handler
public class handler {

    public static final int MESSAGE_READ = 1;
    private static handler instance = null;
    public Handler handler;
    public static SendReceive sendReceive;

    //get instance
    public static handler getInstance(SendReceive sr){
        if (instance == null){
            instance = new handler();
        }
        sendReceive = sr;
        return instance;
    }

    //handler
    private handler(){
        this.handler =  new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MESSAGE_READ:
                        byte[] readBuff = (byte[])msg.obj;
                        String tempMsg = new String(readBuff,0,msg.arg1);
                        sendReceive.receive(tempMsg);
                        break;
                }
                return false;
            }
        });
    }
}
