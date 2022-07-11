package modele;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.util.ArrayList;

import data.DataAccess;
/*Classe qui représente le nom d'uin point à améliorer*/
public class Point extends Persistable{
    private String nom;

    public Point() {
    }

    public Point(String nom) {
        this.nom = nom;
    }

    /*Transforme une liste de points en liste de String prête à être affichée dans un listView*/
    public static ArrayList<String> toArrayString(ArrayList<Point> points){
        ArrayList<String> arrayListString= new ArrayList<>(0);
        for (Point point : points){
            arrayListString.add(point.toString());
        }
        return arrayListString;
    }
    public ContentValues getContentValues(int dataSet) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_NOM_POINT,this.nom);
        cv.put(DataAccess.COL_DATASET,dataSet);
        return cv;
    }

    public String[] getColonnes() {
        String[] colonnes = {DataAccess.COL_NOM_POINT,DataAccess.COL_DATASET};
        return colonnes;
    }

    /*construit un Point à partir du cursor récupéré dans la DB fournit en
     * paramètre*/
    public static Point factoryFromCursor(Cursor cursor) throws ParseException {
        String nom = cursor.getString(0);
        Point point = new Point(nom);
        return point;
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
}
