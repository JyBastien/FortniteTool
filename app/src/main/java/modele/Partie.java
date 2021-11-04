package modele;

import android.content.ContentValues;
import android.database.Cursor;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import data.DataAccess;
import utils.TimestampParser;

public class Partie{
    private Timestamp temps;
    private String pointAmeliorer;

    public Partie(Timestamp temps, String pointAmeliorer) {
        this.temps = temps;
        this.pointAmeliorer = pointAmeliorer;
    }

    public Partie() {

    }

    public Timestamp getTemps() {
        return temps;
    }

    public void setTemps(Timestamp temps) {
        this.temps = temps;
    }

    public String getPointAmeliorer() {
        return pointAmeliorer;
    }

    public void setPointAmeliorer(String pointAmeliorer) {
        this.pointAmeliorer = pointAmeliorer;
    }


    public ContentValues getContentValues(int dataSet) {
        ContentValues cv = new ContentValues();
        cv.put(DataAccess.COL_POINT, this.getPointAmeliorer());
        cv.put(DataAccess.COL_DATE, this.getTemps().toString());
        cv.put(DataAccess.COL_DATASET,dataSet );
        return cv;
    }

    public String getTableName() {
        return DataAccess.TABLE_GAME;
    }

    public String[] getColonnes() {
        String[] colonnes = {DataAccess.COL_POINT,DataAccess.COL_DATE};
        return colonnes;
    }

    public Partie factoryFromCursor(Cursor cursor) throws ParseException {
        String point = cursor.getString(0);
        String date = cursor.getString(1);
        Timestamp timestamp = TimestampParser.parse(date);
        Partie partie = new Partie(timestamp,point);
        return partie;
    }

    @Override
    public String toString() {
        return pointAmeliorer;
    }

    public LocalDate getDate() {
        return Instant.ofEpochMilli(temps.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
