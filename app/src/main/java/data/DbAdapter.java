package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.fortnitetool.R;

import java.text.ParseException;
import java.util.ArrayList;

import modele.Partie;
import modele.Point;
/*Classe responsable des interactions avec la database*/
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
    /*insère une nouvelle partie dans la BD*/
    public long insertPartie(Partie partie, int dataSet) {
        ContentValues cv = partie.getContentValues(dataSet);
        String tableName = partie.getTableName();
        return db.insert(tableName, null, cv);
    }
    /*récupère la liste des parties du dataSet fournit en paramètre
    * ordonné par date*/
    public ArrayList<Partie> fetchAllPartieParDate(int dataSet) {
        Partie partie = new Partie();
        ArrayList<Partie> parties = new ArrayList<>(0);
        String[] colonnes = partie.getColonnes();
        String[] selArgs = {String.valueOf(dataSet)};
        Cursor cursor = db.query(partie.getTableName(), colonnes, DataAccess.COL_DATASET + " = ?", selArgs, null, null, "datetime(" + DataAccess.COL_DATE + ") desc");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
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
    /*mets à jour le point correspondant au nom du point fourni en parametre comme ancienPoint
    * et lui les valeurs du nouveauPoint fournit en paramètre également, les 2 doivent correspondre
    * également au dataSet fournit en paramètre*/
    public long updatePoint(Point ancienPoint, Point nouveauPoint, int dataSet) {
        return db.update(DataAccess.TABLE_POINT, nouveauPoint.getContentValues(dataSet), DataAccess.COL_NOM_POINT + " = ? AND " + DataAccess.COL_DATASET + " = ?", new String[]{ancienPoint.getNom(),String.valueOf(dataSet)});
    }
    /*efface le point correspondant au dataSet fournit en paramètre et au nom du point fournit
    * en paramètre aussi*/
    public void deletePoint(Point point,int dataSet) {
        db.delete(DataAccess.TABLE_POINT, DataAccess.COL_NOM_POINT + " = ? AND " + DataAccess.COL_DATASET + " = ?", new String[]{point.getNom(),String.valueOf(dataSet)});
    }
    /*recoit en parmètre l'ancien point qui retient le nom original du point a modifier,
    le nouveauPoint qui contient les nouvelles valeurs et le dataSet des points dans la BD*/
    public void updateParties(Point ancienPoint, Point nouveauPoint,int dataSet) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_POINT, nouveauPoint.getNom());
        db.update(DataAccess.TABLE_PARTIE, cv, DataAccess.COL_POINT + " = ? AND " + DataAccess.COL_DATASET + " = ?", new String[]{ancienPoint.getNom(),String.valueOf(dataSet)});
    }
    /*recoit un pointAeffacer et un entier dataset en parametre qui permettra deffacer
     *toutes les parties qui répondent à ces critères*/
    public void deletePartiesPoint(Point pointAEffacer,int dataSet) {
        db.delete(DataAccess.TABLE_PARTIE, DataAccess.COL_POINT + " = ? AND " + DataAccess.COL_DATASET + " = ?", new String[]{pointAEffacer.getNom(),String.valueOf(dataSet)});
    }
    /*efface toutes les données correspondant au dataset fournit en paramètre dans la table parties*/
    public void clearData(int dataSet) {
        db.delete(DataAccess.TABLE_PARTIE, DataAccess.COL_DATASET + " = ?", new String[]{String.valueOf(dataSet)});
    }
    /*efface de la table partie l'Enregistrement qui correspond au temps inscrit dans la partie fournie en paramètre et au dataset également*/
    public void deletePartie(Partie partie, int dataSet) {
        db.delete(DataAccess.TABLE_PARTIE, DataAccess.COL_DATE + " = ? AND " + DataAccess.COL_DATASET + " = ?", new String[]{partie.getTemps().toString(),String.valueOf(dataSet)});
    }
    /*lit dans la base de données la couleur de graphique que l'utilisateur a choisi*/
    public int fetchCouleurGraphique() {
        String[] colonnes = {DataAccess.COL_NOM, DataAccess.COL_VALEUR};
        String[] selArgs = {DataAccess.PREFERENCES_COULEUR};
        Cursor cursor = db.query(DataAccess.TABLE_PREFERENCES, colonnes, DataAccess.COL_NOM + " = ?", selArgs, null, null, null);
        cursor.moveToFirst();
        int color = 0;
        if (!cursor.isAfterLast()) {
            color = cursor.getInt(1);
        }
        cursor.close();
        return color;
    }
    /*mets a jour dans la DB la couleur graphique choisie par l'utilisateur*/
    public void updateCouleurGraphique(int valeur) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_VALEUR, valeur);
        String[] whereArgs = {DataAccess.PREFERENCES_COULEUR};
        db.update(DataAccess.TABLE_PREFERENCES, cv, DataAccess.COL_NOM + " = ?", whereArgs);
    }
    /*récupère le nO du dernier dataSet sélectionné par l'utilisateur*/
    public int fetchDataSetActuel() {
        String[] colonnes = {DataAccess.COL_NOM, DataAccess.COL_VALEUR};
        String[] selArgs = {DataAccess.PREFERENCES_DATASET};
        Cursor cursor = db.query(DataAccess.TABLE_PREFERENCES, colonnes, DataAccess.COL_NOM + " = ?", selArgs, null, null, null);
        cursor.moveToFirst();
        int color = 0;
        if (!cursor.isAfterLast()) {
            color = cursor.getInt(1);
        }
        cursor.close();
        return color;
    }
    /*mets a jour dans la DB le numéro du dataSet sélectionné par l'utilisateur*/
    public void updateDataSetActuel(int i) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_VALEUR, i);
        String[] whereArgs = {DataAccess.PREFERENCES_DATASET};
        db.update(DataAccess.TABLE_PREFERENCES, cv, DataAccess.COL_NOM + " = ?", whereArgs);
    }
    /*Récupère la liste de nom des dataSets dans la BD*/
    public ArrayList<String> fetchNomsDataSets() {
        ArrayList<String> noms = new ArrayList<>(4);
        String[] colonnes = {DataAccess.COL_ID, DataAccess.COL_NOM};
        Cursor cursor = db.query(DataAccess.TABLE_DATASET, colonnes, null, null, null, null, DataAccess.COL_ID);
        cursor.moveToNext();
        while (!cursor.isAfterLast()) {
            noms.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return noms;
    }
    /*enregistre un nouveau point fourni en paramètre*/
    public void insertPoint(Point point, int dataSet) {
        ContentValues cv = point.getContentValues(dataSet);
        db.insert(DataAccess.TABLE_POINT, null, cv);
    }
    /*retourne vrai si la valeur de la préférence firstrun est a 1
    * faux dans les autres cas*/
    public boolean firstRun() {
        String[] colonnes = {DataAccess.COL_VALEUR};
        String[] selectionAgrs = {DataAccess.PREFERENCES_FIRST_RUN};
        Cursor cursor = db.query(DataAccess.TABLE_PREFERENCES, colonnes, DataAccess.COL_NOM + " = ?", selectionAgrs, null, null, null);
        cursor.moveToNext();
        boolean firstRun = cursor.getInt(0) == 1;
        cursor.close();
        return firstRun;
    }
    /*mets a jour la valeur de la preferences firstRun
    * pour la valeur 0 soit ce n'Est pas le first run*/
    public void updateFirstRun() {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_VALEUR,0);
        String[] whereArgs = {DataAccess.PREFERENCES_FIRST_RUN};
        db.update(DataAccess.TABLE_PREFERENCES,cv,DataAccess.COL_NOM + " = ?", whereArgs);
    }
    /*récupère la lsite des points de la DB correspondants aux dataSet fournit en parametre*/
    public ArrayList<Point> fetchAllPoints(int dataSet) {
        ArrayList<Point> points = new ArrayList<>(0);
        Point point = new Point();
        String[] selArgs = {String.valueOf(dataSet)};
        Cursor cursor = db.query(DataAccess.TABLE_POINT, point.getColonnes(), DataAccess.COL_DATASET + " = ?", selArgs, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                point = Point.factoryFromCursor(cursor);
                points.add(point);
            } catch (ParseException e) {
                Toast.makeText(context, R.string.DateIncorrecte, Toast.LENGTH_SHORT).show();
            }
            cursor.moveToNext();
        }
        cursor.close();
        return points;
    }
    /*mets a jour le nom du dataset correspondant au id fourni en parametre avec le nouveau
    * nom fourni également*/
    public void updateNomDataSet(String nouveauNom, int dataSetActuel) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_NOM,nouveauNom);
        String[] whereArgs = {String.valueOf(dataSetActuel)};
        db.update(DataAccess.TABLE_DATASET,cv,DataAccess.COL_ID + " = ?",whereArgs);
    }
}
