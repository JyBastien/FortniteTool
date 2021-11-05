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

    public long insertPartie(Partie partie, int dataSet) {
        ContentValues cv = partie.getContentValues(dataSet);
        String tableName = partie.getTableName();
        return db.insert(tableName, null, cv);
    }

    public ArrayList<Partie> fetchAllPersistable(Partie partie, int dataSet) {
        ArrayList<Partie> parties = new ArrayList<>(0);
        String[] colonnes = partie.getColonnes();
        String[] selArgs = {String.valueOf(dataSet)};
        Cursor cursor = db.query(partie.getTableName(), partie.getColonnes(), DataAccess.COL_DATASET + " = ?", selArgs, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                partie = partie.factoryFromCursor(cursor);
                parties.add(partie);
            } catch (ParseException e) {
                Toast.makeText(context, R.string.DateIncorrecte, Toast.LENGTH_SHORT).show();
            }
            cursor.moveToNext();
        }
        cursor.close();
        return parties;
    }

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


    public long updatePoint(Point ancienPoint, Point nouveauPoint, int dataSet) {
        return db.update(DataAccess.TABLE_POINT, nouveauPoint.getContentValues(dataSet), DataAccess.COL_NOM_POINT + " = ? AND " + DataAccess.COL_DATASET + " = ?", new String[]{ancienPoint.getNom(),String.valueOf(dataSet)});
    }

    public void deletePoint(Point point,int dataSet) {
        db.delete(DataAccess.TABLE_POINT, DataAccess.COL_NOM_POINT + " = ? AND " + DataAccess.COL_DATASET + " = ?", new String[]{point.getNom(),String.valueOf(dataSet)});
    }

    public void updateParties(Point ancienPoint, Point nouveauPoint,int dataSet) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_POINT, nouveauPoint.getNom());
        db.update(DataAccess.TABLE_GAME, cv, DataAccess.COL_POINT + " = ? AND " + DataAccess.COL_DATASET + " = ?", new String[]{ancienPoint.getNom(),String.valueOf(dataSet)});
    }

    public void deletePartiesPoint(Point pointAEffacer,int dataSet) {
        db.delete(DataAccess.TABLE_GAME, DataAccess.COL_POINT + " = ? AND " + DataAccess.COL_DATASET + " = ?", new String[]{pointAEffacer.getNom(),String.valueOf(dataSet)});
    }

    public void clearData(int dataSet) {
        db.delete(DataAccess.TABLE_GAME, DataAccess.COL_DATASET + " = ?", new String[]{String.valueOf(dataSet)});
    }

    public void deletePartie(Partie partie, int dataSet) {
        db.delete(DataAccess.TABLE_GAME, DataAccess.COL_DATE + " = ? AND " + DataAccess.COL_DATASET + " = ?", new String[]{partie.getTemps().toString(),String.valueOf(dataSet)});
    }

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

    public void updateCouleurGraphique(int valeur) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_VALEUR, valeur);
        String[] whereArgs = {DataAccess.PREFERENCES_COULEUR};
        db.update(DataAccess.TABLE_PREFERENCES, cv, DataAccess.COL_NOM + " = ?", whereArgs);
    }

    public int fetchDataSetActuelId() {
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

    public void updateDataSetActuel(int i) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_VALEUR, i);
        String[] whereArgs = {DataAccess.PREFERENCES_DATASET};
        db.update(DataAccess.TABLE_PREFERENCES, cv, DataAccess.COL_NOM + " = ?", whereArgs);
    }

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

    public void insertPoint(Point point, int dataSet) {
        ContentValues cv = point.getContentValues(dataSet);
        db.insert(DataAccess.TABLE_POINT, null, cv);
    }

    public boolean firstRun() {
        String[] colonnes = {DataAccess.COL_VALEUR};
        String[] selectionAgrs = {DataAccess.PREFERENCES_FIRST_RUN};
        Cursor cursor = db.query(DataAccess.TABLE_PREFERENCES, colonnes, DataAccess.COL_NOM + " = ?", selectionAgrs, null, null, null);
        cursor.moveToNext();
        boolean firstRun = cursor.getInt(0) == 1;
        cursor.close();
        return firstRun;
    }

    public void updateFirstRun() {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_VALEUR,0);
        String[] whereArgs = {DataAccess.PREFERENCES_FIRST_RUN};
        db.update(DataAccess.TABLE_PREFERENCES,cv,DataAccess.COL_NOM + " = ?", whereArgs);
    }

    public ArrayList<Point> fetchAllPoints(int dataSet) {
        ArrayList<Point> points = new ArrayList<>(0);
        Point point;
        String[] selArgs = {String.valueOf(dataSet)};
        Cursor cursor = db.query(DataAccess.TABLE_POINT, Point.getColonnes(), DataAccess.COL_DATASET + " = ?", selArgs, null, null, null);
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

    public void updateNomDataSet(String nouveauNom, int dataSetActuel) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_NOM,nouveauNom);
        String[] whereArgs = {String.valueOf(dataSetActuel)};
        db.update(DataAccess.TABLE_DATASET,cv,DataAccess.COL_ID + " = ?",whereArgs);
    }
}
