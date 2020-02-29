package com.example.p2p_client;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.p2p_client.Chat.handler;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MAIN ACTIVITY : ";
    MainActivity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Log.i(TAG, "started");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        handler.getInstance(null);

        this.self = this;

        //gestion bouton server
        Button server = findViewById(R.id.buttonServer);
        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent activityServer = new Intent(self, ActivityServer.class);
                startActivityForResult(activityServer, 10);
            }
        });

        //gestion bouton client
        Button client = findViewById(R.id.buttonClient);
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent activityClient = new Intent(self, ActivityClient.class);
                startActivityForResult(activityClient, 10);
            }
        });

        Button quitter = findViewById(R.id.buttonQuitter);
        quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

}
