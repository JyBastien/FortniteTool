package modele;

import android.content.ContentValues;
import android.database.Cursor;

import java.sql.Timestamp;
import java.text.ParseException;

import data.DataAccess;
import utils.Persistable;
import utils.TimestampParser;

public class Score implements Persistable {
    private Timestamp temps;
    private String nomJoueur;
    private int score;

    public Score(Timestamp temps, String joueur, int score) {
        this.temps = temps;
        this.nomJoueur = joueur;
        this.score = score;
    }

    public Score() {

    }

    public Timestamp getTemps() {
        return temps;
    }

    public void setTemps(Timestamp temps) {
        this.temps = temps;
    }

    public String getNomJoueur() {
        return nomJoueur;
    }

    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_NOM_JOUEUR, this.getNomJoueur());
        cv.put(DataAccess.COL_SCORE, this.getScore());
        cv.put(DataAccess.COL_DATE, this.getTemps().toString());
        return cv;
    }

    @Override
    public String getTableName() {
        return DataAccess.TABLE_SCORE;
    }

    @Override
    public String[] getColonnes() {
        String[] colonnes = {DataAccess.COL_NOM_JOUEUR,DataAccess.COL_SCORE, DataAccess.COL_DATE};
        return colonnes;
    }

    @Override
    public Persistable factoryFromCursor(Cursor cursor) throws ParseException {
        String nomJoueur = cursor.getString(0);
        int intScore = cursor.getInt(1);
        String date = cursor.getString(2);
        Timestamp timestamp = TimestampParser.parse(date);
        Score score = new Score(timestamp,nomJoueur,intScore);
        return score;
    }

    @Override
    public String toString() {
        return nomJoueur;
    }
}
