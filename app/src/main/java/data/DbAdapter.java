package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.fortnitetool.R;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;

import fragment.ConfigFragment;
import modele.Joueur;
import modele.Partie;
import modele.Point;
import modele.Score;
import modele.Stats;
import utils.Persistable;
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

    public long insertPersistable(Persistable entity) {
        ContentValues cv = entity.getContentValues();
        String tableName = entity.getTableName();
        return db.insert(tableName, null, cv);
    }

    public ArrayList<Persistable> fetchAllPersistable(Persistable entity){
        ArrayList<Persistable> entities = new ArrayList<>(0);
        String[] colonnes = entity.getColonnes();
        Cursor cursor = db.query(entity.getTableName(),entity.getColonnes(),null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            try {
                entity = entity.factoryFromCursor(cursor);
                entities.add(entity);
            } catch (ParseException e) {
                Toast.makeText(context, R.string.DateIncorrecte, Toast.LENGTH_SHORT).show();
            }
            cursor.moveToNext();
        }
        cursor.close();
        return entities;
    }

    public ArrayList<Partie> fetchAllPartieParDate(){
        Partie partie = new Partie();
        ArrayList<Partie> parties = new ArrayList<>(0);
        String[] colonnes = partie.getColonnes();
        Cursor cursor = db.query(partie.getTableName(),colonnes,null,null,null,null,"date(" + DataAccess.COL_DATE + ") desc");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            try {
                partie = (Partie) partie.factoryFromCursor(cursor);
                parties.add(partie);
            } catch (ParseException e) {
                Toast.makeText(context, R.string.DateIncorrecte, Toast.LENGTH_SHORT).show();
            }
            cursor.moveToNext();
        }
        cursor.close();
        return parties;
    }


    public Boolean getBoolean(){
        return null;
    }


    public long updateJoueur(Joueur ancienJoueur, Joueur nouveauJoueur) {
        return db.update(ancienJoueur.getTableName(),nouveauJoueur.getContentValues(),DataAccess.COL_NOM_JOUEUR + " = ?",new String[]{ancienJoueur.getNom()});
    }
    public long updatePoint(Point ancienPoint, Point nouveauPoint) {
        return db.update(ancienPoint.getTableName(),nouveauPoint.getContentValues(),  DataAccess.COL_NOM_POINT + " = ?",new String[]{ancienPoint.getNom()});
    }

    public void deleteJoueur(Joueur joueur) {
        db.delete(joueur.getTableName(),DataAccess.COL_NOM_JOUEUR + " = ?",new String[]{joueur.getNom()});
    }

    public void deletePoint(Point point) {
        db.delete(point.getTableName(),DataAccess.COL_NOM_POINT + " = ?",new String[]{point.getNom()});
    }

    public void updateParties(Point ancienPoint, Point nouveauPoint) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_POINT,nouveauPoint.getNom());
        db.update(DataAccess.TABLE_GAME,cv,DataAccess.COL_POINT + " = ?",new String[]{ancienPoint.getNom()});
    }

    public void updateScores(Joueur ancienJoueur, Joueur nouveauJoueur) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_NOM_JOUEUR,nouveauJoueur.getNom());
        db.update(DataAccess.TABLE_SCORE,cv,DataAccess.COL_NOM_JOUEUR + " = ?",new String[]{ancienJoueur.getNom()});
    }

    public void deleteJoueurScores(Joueur joueurAEffacer) {
        db.delete(DataAccess.TABLE_SCORE,DataAccess.COL_NOM_JOUEUR + " = ?", new String[]{joueurAEffacer.getNom()});
    }

    public void deletePartiesPoint(Point pointAEffacer) {
        db.delete(DataAccess.TABLE_GAME,DataAccess.COL_POINT + " = ?",new String[]{pointAEffacer.getNom()});
    }

    public void clearData() {
        db.delete(DataAccess.TABLE_GAME,null,null);
        db.delete(DataAccess.TABLE_SCORE,null,null);
    }

    public void deletePartie(Partie partie) {
        db.delete(DataAccess.TABLE_GAME,DataAccess.COL_DATE + " = ?",new String[]{partie.getTemps().toString()});
    }

    public void deleteScore(Score score) {
        db.delete(DataAccess.TABLE_SCORE,DataAccess.COL_DATE + " = ?",new String[]{score.getTemps().toString()});
    }

}
