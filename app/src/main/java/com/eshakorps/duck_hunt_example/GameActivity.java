package com.eshakorps.duck_hunt_example;

import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    ImageView imagenPato;
    TextView txtContador, txtNombreUser, txtTiempo;


    int anchoPantalla, altoPantalla;
    Random aleatorio;
    Boolean gameOver;

    String idUser;
    FirebaseFirestore db;
    String nombeNick;
    int contador;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        contador = 0;

        db = FirebaseFirestore.getInstance();

        gameOver = false;

        txtContador = findViewById(R.id.txt_patos_cazados);
        txtNombreUser = findViewById(R.id.txt_nombre_jugador);
        txtTiempo = findViewById(R.id.txt_contador);
        imagenPato = findViewById(R.id.imagenPato);

        Typeface tipoFuente = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        txtNombreUser.setTypeface(tipoFuente);
        txtTiempo.setTypeface(tipoFuente);
        txtContador.setTypeface(tipoFuente);


        Bundle extra = getIntent().getExtras();
        nombeNick = extra.getString(Constantes.EXTRA_NICK);
        idUser = extra.getString(Constantes.EXTRA_ID);

        txtNombreUser.setText(nombeNick);

        eventos();
        initPantalla();
        initCuentaAtras();
    }


    private void saveDuckHuntedToDB() {
        db.collection("user")
                .document(idUser)
                .update("duck", contador);
    }


    private void initPantalla(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        altoPantalla = size.y;
        anchoPantalla = size.x;

        aleatorio = new Random();
    }

    private void eventos() {
        imagenPato.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                contador++;
                txtContador.setText(String.valueOf(contador));
                imagenPato.setImageResource(R.drawable.duck_clicked);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imagenPato.setImageResource(R.drawable.duck);
                        moverPato();
                    }
                }, 500);


            }
        });
    }

    private void initCuentaAtras() {
        new CountDownTimer(10000, 1000){
            public void onTick(long milliseconds){
                long segundosRestantes = milliseconds / 1000;
                txtTiempo.setText(segundosRestantes+"s");
            }

            public void onFinish() {
                txtTiempo.setText("0");
                gameOver = true;
                mostrarGameOver();
            }
        }.start();
    }

    protected void mostrarGameOver(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        saveDuckHuntedToDB();
        builder.setMessage("Has cazado esta cantidad de patos: "+ txtContador.getText().toString())
                .setTitle("GAME OVER")
                .setPositiveButton("Reiniciar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        volverAJugar();
                    }
                })
                .setNegativeButton("Terminar juego", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        mostrarRanking();
                    }
                })
        .setCancelable(false);

        AlertDialog dialogo = builder.create();
        dialogo.show();
    }

    private void mostrarRanking(){
        Intent i = new Intent(GameActivity.this, RankingActivity.class);
        startActivity(i);
    }

    private void volverAJugar() {
        gameOver = false;

        txtContador.setText("0");
        moverPato();
        contador = 0;
        initCuentaAtras();
    }

    private void moverPato() {
        int min = 0;
        int maxX= anchoPantalla - imagenPato.getWidth();
        int maxY = altoPantalla - imagenPato.getHeight();

        int randomX = aleatorio.nextInt(maxX - min + 1);
        int randomY = aleatorio.nextInt(maxY - min + 1);

        imagenPato.setX(randomX);
        imagenPato.setY(randomY);
    }
}
