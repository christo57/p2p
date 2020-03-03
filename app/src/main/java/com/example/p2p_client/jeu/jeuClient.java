package com.example.p2p_client.jeu;

import android.os.Bundle;
import android.widget.TextView;

import com.example.p2p_client.Chat.ClientClass;
import com.example.p2p_client.R;

import java.net.InetAddress;

public class jeuClient extends jeu {

    private String nomServeur;
    private InetAddress adresseServeur;
    private ClientClass clientClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //init
        this.nomServeur = getIntent().getExtras().getString("nom");
        this.adresseServeur = (InetAddress) getIntent().getExtras().get("adresse");

        /*
        textType.setText(textType.getText() + " Client : connected to " + this.nomServeur + " : " + this.adresseServeur.getHostAddress());
         */

        this.clientClass = new ClientClass(this, this.adresseServeur);
        this.clientClass.start();
    }

    //allow to play
    public void jouer(int choix) {
        super.jouer(choix);
        this.send("choix:" + String.valueOf(choix));
    }

    public void receive(String texte) {
        super.receive(texte);

        //separation de la categorie et de la valeur
        String textes[] = texte.split(":");

        //si un mot
        if (textes.length == 1) {
            //replay ou end
            super.receive(texte);

            switch (texte){
                //si replay, recommence une manche
                case "replay":
                    this.reset();
                    break;

                //si end, alors fin
                case "end":
                    this.reset();
                    this.afficherFin();
                    break;

                case "restart":
                    restart();
                    break;

                case "quitter":
                    adversaireQuit();
                    break;

                case "recommencer":
                    this.restartAdversaire = true;
                    this.adversaireRecommence();
                    break;
            }

        }
        //sinon
        else {
            String categorie = textes[0];
            String valeur = textes[1];

            super.receive(categorie + " : " + valeur);

            switch (categorie) {
                //si choix, stockage et affichage
                case "choix":
                    this.choixAdversaire = Integer.parseInt(valeur);
                    this.afficherAdversaireJouer();
                    break;

                 //si res, affichage
                case "res":
                    int res = Integer.parseInt(valeur);
                    this.afficherResultat(res);
                    break;
            }
        }
    }

    //show result
    public void afficherResultat(int res) {
        //resultat = inverse du resultat adverse
        res = -res;
        super.afficherResultat(res);
    }


    //send message
    @Override
    public void send(String msg) {
        this.clientClass.getSendReceive().write(msg.getBytes());
    }

    @Override
    public void sendRecommencer(String restart){
        send(restart);
        if(restartAdversaire == null) {
            this.attendreRecommencerAdversaire();
        }
    }
}
