package modele;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;

public abstract class Persistable {

    /*Permettra au database d'updater ou d'inserer les valeurs dans les colonnes correspondante
    * de la DB */
    public abstract ContentValues getContentValues(int dataSet);
    /*retourne les colonnes `récupérer lors des fetchs dans la database*/
    public abstract   String[] getColonnes();
}
