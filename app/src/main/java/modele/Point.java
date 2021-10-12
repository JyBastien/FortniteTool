package modele;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;

import data.DataAccess;
import utils.Persistable;

public class Point implements Persistable {
    private String nom;

    public Point() {
    }

    public Point(String nom) {
        this.nom = nom;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_NOM_POINT,this.nom);
        return cv;
    }

    @Override
    public String getTableName() {
        return DataAccess.TABLE_POINT;
    }

    @Override
    public String[] getColonnes() {
        String[] colonnes = {DataAccess.COL_NOM_POINT};
        return colonnes;
    }

    @Override
    public Persistable factoryFromCursor(Cursor cursor) throws ParseException {
        String nom = cursor.getString(0);
        Point point = new Point(nom);
        return point;
    }

    @Override
    public String toString() {
        return nom;
    }
}
