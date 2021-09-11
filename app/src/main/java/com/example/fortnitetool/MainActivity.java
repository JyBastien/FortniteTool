package com.example.fortnitetool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    private Spinner raison;
    private Spinner joueur;

    //rendu a customiser les item des spinners comme le prof avait faoit pour son list view


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidgets();
    }

    private void setWidgets() {
        raison = findViewById(R.id.cmbRaisons);
        joueur = findViewById(R.id.cmbJoueur);

        //1. Data
        String[] dataRaison = {"BadStorm", "Unity", "OutSkilled","Casual", "BadDrop","2v1", "Position"};
        String[] dataJoueur = {"CptSemiColon", "SpecktR"};

        //2. Adapter
        ArrayAdapter<String> adapteurRaison = new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item, dataRaison);
        ArrayAdapter<String> adapteurJoueur = new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item, dataJoueur);



        //3. Ler l'adapter avec lsiting

        raison.setAdapter(adapteurRaison);
        joueur.setAdapter(adapteurJoueur);
    }
}