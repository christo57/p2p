package com.example.p2p_client.jeu;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.example.p2p_client.ActivityServer;
import com.example.p2p_client.Chat.ServerClass;
import com.example.p2p_client.R;

public class jeuServer extends jeu {

    private ServerClass serverClass;

    //on create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        textType.setText(textType.getText() + " Server");
         */

        this.serverClass = new ServerClass(this);
        this.serverClass.start();
    }

    //allow to play
    public void jouer(int choix) {
        super.jouer(choix);
        this.send("choix:" + String.valueOf(choix));

        if (choixAdversaire != -1) {
            finManche();
        }
    }

    //allow to receive a message
    public void receive(String texte) {
        String textes[] = texte.split(":");

        if (textes.length == 1) {
            switch (texte) {
                case "quitter":
                    adversaireQuit();
                    break;

                case "recommencer":
                    this.restartAdversaire = true;
                    if (restart == null) this.adversaireRecommence();
                    else restart();
                    break;
            }
        } else if (textes.length == 2) {
            String categorie = textes[0];
            String valeur = textes[1];

            super.receive(categorie + " : " + valeur);

            switch (categorie) {
                //si choix, stockage et affichage
                case "choix":
                    this.choixAdversaire = Integer.parseInt(valeur);
                    if (choix == -1) this.afficherAdversaireJouer();
                    else {
                        finManche();
                    }
                    break;
            }
        }
    }

    //gestion fin de manche
    public void finManche() {
        //get resultat de la manche
        int res = this.calculerResultat();

        //envoi reponse au client
        this.send("res:" + String.valueOf(res));

        //affiche le resultat
        this.afficherResultat(res);

        //Attend avant de recommencer une nouvelle manche
        attendre();
    }

    //defini le resultat de la manche en fonction des choix des joueurs
    public int calculerResultat() {
        //si meme choix, egalite
        if (this.choix == this.choixAdversaire) return 0;
        else {
            if (choix == 0) {
                if (choixAdversaire == 1) return -1;
                else return 1;
            } else if (choix == 1) {
                if (choixAdversaire == 0) return 1;
                else return -1;
            } else {
                if (choixAdversaire == 0) return -1;
                else return 1;
            }
        }
    }

    //permet d'attendre 4 secondes avant de reveiller
    public void attendre() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //si fin de la partie
                if (getScore() >= SCOREPOURGAGNER || getScoreAdversaire() >= SCOREPOURGAGNER) {
                    //envoi message end
                    send("end");

                    reset();
                    afficherFin();
                }
                //sinon
                else {
                    //on recommence une autre manche
                    reset();
                    send("replay");
                }
            }
        }, 4000);
    }

    public void restart() {
        send("restart");
        super.restart();
    }

    //send message
    @Override
    public void send(String msg) {
        this.serverClass.getSendReceive().write(msg.getBytes());
    }

    @Override
    public void sendRecommencer(String restart) {
        if (restartAdversaire == null) {
            send(restart);
            this.attendreRecommencerAdversaire();
        } else this.restart();
    }
}
