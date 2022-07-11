package modele;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.fortnitetool.R;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import activity.MainActivity;
import data.DataAccess;
import utils.TimestampParser;
/*Classe qui récupère les informations d'une partie soit
* la date ou elle s'est déroulée et le nom du point qui est
* à améliorer dans cette partie*/
public class Partie extends Persistable{
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
        return DataAccess.TABLE_PARTIE;
    }

    public String[] getColonnes() {
        String[] colonnes = {DataAccess.COL_POINT,DataAccess.COL_DATE};
        return colonnes;
    }
    /*construit une partie a partir du cursor récupérer dans la DB fournit en
    * paramètre*/
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
    /*Transforme une liste de partie en liste de String prête à être affichée dans le listView*/
    public static ArrayList<String> partiesToStringEnLigne(ArrayList<Partie> parties, MainActivity activity) {
        ArrayList<String> partiesString = new ArrayList(0);
        for (Partie partie : parties) {
            partiesString.add(new Date(partie.getTemps().getTime()).toString().substring(4, 19) + " " + activity.getResources().getString(R.string.point) + ": " + partie.getPointAmeliorer());
        }
        return partiesString;
    }
}
