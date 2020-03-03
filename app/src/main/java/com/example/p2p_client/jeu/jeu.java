package com.example.p2p_client.jeu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.p2p_client.Activity_cli_ser;
import com.example.p2p_client.MainActivity;
import com.example.p2p_client.R;


public abstract class jeu extends Activity {

    public static final int SCOREPOURGAGNER = 3;

    public static final int DECONNEXION = 1;

    /*
    //pour developper
    String texteInitial = "texte : ";
    TextView texte;
    TextView texteToSend;
    Button btnSend;
    TextView textType;
    */

    TextView textScore;

    //buttons to play
    Button boutonPierre;
    Button boutonFeuille;
    Button boutonCiseaux;

    //button choice
    Button boutonChoixAdversaire;
    Button boutonChoix;

    //text score
    TextView scoreVous;
    TextView scoreAdversaire;

    //choice
    protected int choix;
    protected int choixAdversaire;

    protected Boolean restart;
    protected Boolean restartAdversaire;

    protected AlertDialog.Builder alert;
    protected AlertDialog showed;

    public void initialisation(){
        choix = -1;
        choixAdversaire = -1;

        restart = null;
        restartAdversaire = null;

        alert = null;
        showed = null;

        scoreVous.setText("0");
        scoreAdversaire.setText("0");
    }

    //on create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jeu_final);

        /*
        textType = findViewById(R.id.textType);
        texte = findViewById(R.id.textMessage);
        texteToSend = findViewById(R.id.texteToSend);

        btnSend = findViewById(R.id.buttonSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = texteToSend.getText().toString();
                send("autre:" + msg);
            }
        });
        */

        textScore = findViewById(R.id.textGeneral);
        textScore.setText("Score : le premier a " + SCOREPOURGAGNER + " gagne");

        boutonPierre = findViewById(R.id.buttonPierre);
        boutonPierre.setBackgroundColor(Color.WHITE);
        boutonPierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boutonChoix = boutonPierre;
                jouer(0);
            }
        });

        boutonFeuille = findViewById(R.id.buttonFeuille);
        boutonFeuille.setBackgroundColor(Color.WHITE);
        boutonFeuille.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boutonChoix = boutonFeuille;
                jouer(1);
            }
        });

        boutonCiseaux = findViewById(R.id.buttonCiseaux);
        boutonCiseaux.setBackgroundColor(Color.WHITE);
        boutonCiseaux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boutonChoix = boutonCiseaux;
                jouer(2);
            }
        });

        this.boutonChoixAdversaire = findViewById(R.id.buttonChoixAdversaire);
        boutonChoixAdversaire.setBackgroundColor(Color.WHITE);
        this.scoreVous = findViewById(R.id.textScoreVous);
        this.scoreAdversaire = findViewById(R.id.textScoreAdversaire);

        initialisation();
    }

    public void jouer(int choix) {
        this.choix = choix;
        boutonChoix.setBackgroundColor(Color.YELLOW);
        boutonPierre.setEnabled(false);
        boutonFeuille.setEnabled(false);
        boutonCiseaux.setEnabled(false);
    }

    public void receive(String texte) {
        Log.i(MainActivity.TAG, texte);
        //this.texte.setText(texteInitial + texte);
    }

    public void afficherAdversaireJouer() {
        boutonChoixAdversaire.setBackgroundColor(Color.CYAN);
        boutonChoixAdversaire.setText("CHOIX FAIT");
    }

    public void afficherResultat(int res) {

        //set button choix adversaire texte
        String choixAdversaire = "";
        switch (this.choixAdversaire) {
            case 0:
                choixAdversaire = "PIERRE";
                break;
            case 1:
                choixAdversaire = "FEUILLE";
                break;
            case 2:
                choixAdversaire = "CISEAUX";
                break;
        }

        boutonChoixAdversaire.setText(choixAdversaire);

        switch (res) {

            //egalite
            case 0:
                boutonChoixAdversaire.setBackgroundColor(Color.GRAY);
                boutonChoix.setBackgroundColor(Color.GRAY);
                break;

            //gagner
            case 1:
                boutonChoixAdversaire.setBackgroundColor(Color.RED);
                boutonChoix.setBackgroundColor(Color.GREEN);

                this.scoreVous.setText(String.valueOf(getScore() + 1));
                break;

            //perdu
            case -1:
                boutonChoixAdversaire.setBackgroundColor(Color.GREEN);
                boutonChoix.setBackgroundColor(Color.RED);

                this.scoreAdversaire.setText(String.valueOf(getScoreAdversaire() + 1));
                break;
        }
    }

    //remet tout comme dans l'etat initial
    public void reset() {
        this.choix = -1;
        this.choixAdversaire = -1;

        this.boutonChoixAdversaire.setText("CHOIX");

        this.boutonChoixAdversaire.setBackgroundColor(Color.WHITE);
        this.boutonChoix.setBackgroundColor(Color.WHITE);

        boutonPierre.setEnabled(true);
        boutonFeuille.setEnabled(true);
        boutonCiseaux.setEnabled(true);
    }

    //get score
    public int getScore() {
        return Integer.parseInt(this.scoreVous.getText().toString());
    }

    //get score adversaire
    public int getScoreAdversaire() {
        return Integer.parseInt(this.scoreAdversaire.getText().toString());
    }

    //affiche modal vainqueur ou perdant et quitte
    public void afficherFin() {
        this.alert = new AlertDialog.Builder(this);

        if(this.getScore() >= SCOREPOURGAGNER) alert.setTitle("Vous avez gagner");
        else alert.setTitle("Vous avez perdu");

        alert.setMessage("Voulez-vous recommencez ?");

        alert.setPositiveButton("oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restart = true;
                sendRecommencer("recommencer");
            }
        });

        alert.setNegativeButton("non",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        restart = false;
                        send("quitter");
                        finActivite();
                    }
                });

        this.showed = alert.create();
        this.showed.setCanceledOnTouchOutside(false);
        showed.show();
    }

    public void adversaireRecommence(){
        showed.dismiss();

        alert.setMessage("Voulez-vous recommencer ? \n Votre adversaire veut recommencer");
        this.showed = alert.create();
        this.showed.setCanceledOnTouchOutside(false);
        showed.show();
    }

    public void adversaireQuit(){
        showed.dismiss();

        this.alert = new AlertDialog.Builder(this);

        alert.setTitle("Votre adversaire à quitter la partie");

        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finActivite();
            }
        });

        this.showed = alert.create();
        this.showed.setCanceledOnTouchOutside(false);
        showed.show();
    }

    public void attendreRecommencerAdversaire(){
        this.alert = new AlertDialog.Builder(this);

        alert.setTitle("En attente de votre adversaire ...");

        alert.setPositiveButton("quitter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finActivite();
            }
        });

        this.showed = alert.create();
        this.showed.setCanceledOnTouchOutside(false);
        showed.show();
    }

    public void finActivite(){
        send("quitter");
        setResult(DECONNEXION);
        super.finish();
    }

    public void restart(){
        showed.dismiss();
        //redemmar le jeu
        Log.i(Activity_cli_ser.TAG, "restart game");
        initialisation();
    }

    public abstract void send(String msg);

    public abstract void sendRecommencer(String restart);
}
