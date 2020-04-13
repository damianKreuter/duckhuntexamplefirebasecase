package com.eshakorps.duck_hunt_example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

//import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    EditText nickText;
    Button botonStart;
    String nickSeleccionado;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.login);

        nickText = findViewById(R.id.editText);
        botonStart = findViewById(R.id.botonComenzar);

        db = FirebaseFirestore.getInstance();

        botonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickSeleccionado = nickText.getText().toString();


                Typeface tipoFuente = Typeface.createFromAsset(getAssets(), "pixel.ttf");
                nickText.setTypeface(tipoFuente);
                botonStart.setTypeface(tipoFuente);

                if(nickSeleccionado.isEmpty()){
                    nickText.setError("El nombre de user es obligatorio");
                } else if (nickSeleccionado.length() < 3) {
                    nickText.setError("Debe tener al menos 3 caracteres");
                } else {
                    initGameAndStart();
                }
            }
        });
    }

    private void startGame(String documentId){
        nickText.setText("");
        Intent i = new Intent(Login.this, GameActivity.class);
        i.putExtra(Constantes.EXTRA_NICK, nickSeleccionado);
        i.putExtra(Constantes.EXTRA_ID, documentId);
        startActivity(i);
    }

    private void addNewNickAndStartGame(){
        User newUser = new User(nickSeleccionado, 0);

        db.collection("user")
                .add(newUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        startGame(documentReference.getId());
                    }
                });
    }

    private void initGameAndStart() {
        db.collection("user").whereEqualTo("nick",nickSeleccionado)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()>0){
                            nickText.setError("El nick no está disponible");
                        } else {
                            addNewNickAndStartGame();
                        }
                    }
                });
    }


}
