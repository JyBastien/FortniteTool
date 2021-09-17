package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.fortnitetool.R;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;

import modele.Partie;
import modele.Score;
import utils.TimestampParser;

public class DbAdapter {
    private SQLiteDatabase db;
    private DataAccess dataAccess;
    private Context context;

    public DbAdapter(Context context) {
        this.context = context;
        this.dataAccess = new DataAccess(context, DataAccess.BD_NOM, null, DataAccess.VERSION);
    }

    public void ouvrirBd() {
        db = dataAccess.getWritableDatabase();
    }

    public void fermerBd() {
        db.close();
    }

    public long ajouterPartie(Partie partie) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_POINT, partie.getPointAmeliorer());
        cv.put(DataAccess.COL_DATE, partie.getTemps().toString());

        return db.insert(DataAccess.TABLE_GAME, null, cv);
    }

    public long ajouterScore(Score score) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_NOM_JOUEUR, score.getJoueur());
        cv.put(DataAccess.COL_SCORE, score.getScore());
        cv.put(DataAccess.COL_DATE, score.getTemps().toString());

        return db.insert(DataAccess.TABLE_SCORE, null, cv);
    }

    public ArrayList<Partie> fetchParties(){
        ArrayList<Partie> parties = new ArrayList<>(0);
        String[] colonnes = {DataAccess.COL_POINT,DataAccess.COL_DATE};
        Cursor cursor = db.query(DataAccess.TABLE_GAME,colonnes,null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String point = cursor.getString(0);
            String date = cursor.getString(1);
            try {
                Timestamp timestamp = TimestampParser.parse(date);
                Partie partie = new Partie(timestamp,point);
                parties.add(partie);
            } catch (ParseException e) {
                Toast.makeText(context, R.string.DateIncorrecte, Toast.LENGTH_SHORT).show();
            }
            cursor.moveToNext();

        }
        cursor.close();
        return parties;
    }

    public ArrayList<Score> fetchScores(){
        ArrayList<Score> scores = new ArrayList<>(0);
        String[] colonnes = {DataAccess.COL_NOM_JOUEUR,DataAccess.COL_SCORE, DataAccess.COL_DATE};
        Cursor cursor = db.query(DataAccess.TABLE_SCORE,colonnes,null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String nomJoueur = cursor.getString(0);
            int intScore = cursor.getInt(1);
            String date = cursor.getString(2);
            try {
                Timestamp timestamp = TimestampParser.parse(date);
                Score score = new Score(timestamp,nomJoueur,intScore);
                scores.add(score);
            } catch (ParseException e) {
                Toast.makeText(context, R.string.DateIncorrecte, Toast.LENGTH_SHORT).show();
            }
            cursor.moveToNext();

        }
        cursor.close();
        return scores;
    }




}
