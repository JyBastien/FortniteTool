package modele;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;

import data.DataAccess;
import utils.Persistable;

public class Joueur implements Persistable {
    private String nom;

    public Joueur() {
    }

    public Joueur(String nom) {
        this.nom = nom;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_NOM_JOUEUR,this.nom);
        return cv;
    }

    @Override
    public String getTableName() {
        return DataAccess.TABLE_JOUEUR;
    }

    @Override
    public String[] getColonnes() {
        String[] colonnes = {DataAccess.COL_NOM_JOUEUR};
        return colonnes;
    }

    @Override
    public Persistable factoryFromCursor(Cursor cursor) throws ParseException {
        String nom = cursor.getString(0);
        Joueur joueur = new Joueur(nom);
        return joueur;
    }

    @Override
    public String toString() {
        return nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    //INSERT DAVED

    /*        if (joueurs.size() == 0) {
            reponse = "";
            while(reponse.trim().equals("")) {
                chargerPremierElement("Vous devez entrez un Joueur aussi");
            }
            Joueur joueur = new Joueur(reponse);
            dbAdapter.insertPersistable(joueur);
            joueurs.add(joueur);
        }*/
}
