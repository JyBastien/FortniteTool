package utils;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.util.ArrayList;

public interface Persistable {
    ContentValues getContentValues();

    String getTableName();

    String[] getColonnes();

    Persistable factoryFromCursor(Cursor cursor) throws ParseException;

    String toString();

    static ArrayList<String> toArrayString(ArrayList<Persistable> persistables){
        ArrayList<String> arrayListString= new ArrayList<>(0);
        for (Persistable persistable : persistables){
            arrayListString.add(persistable.toString());
        }
        return arrayListString;
    }

}
