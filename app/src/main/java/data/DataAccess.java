package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import androidx.annotation.Nullable;

import activity.MainActivity;

public class DataAccess extends SQLiteOpenHelper {
    public final static String BD_NOM = "FortniteTool11";

    public final static String TABLE_DATASET = "DataSet";
    //  col - id , col - nom

    public final static String TABLE_PREFERENCES = "Preferences";
    public final static String COL_NOM = "Nom";
    public final static String COL_VALEUR = "Valeur";
    public final static String PREFERENCES_COULEUR = "couleur";
    public final static String PREFERENCES_DATASET = "dataset";
    public static final String PREFERENCES_FIRST_RUN = "firstRun";

    public final static String TABLE_GAME = "Game";
    public final static String COL_ID = "_id";
    public final static String COL_POINT = "Point";
    public final static String COL_DATE = "Date";
    //col - dataset

    public final static String TABLE_POINT = "Point";
    public final static String COL_NOM_POINT = "Nom";
    public static final String COL_DATASET = "DataSet";


    public final static String DATASET_DDL = "create table " + TABLE_DATASET + "(" +
            COL_ID + " INTEGER primary key, " +
            COL_NOM + " TEXT)";

    public final static String PREFERENCE_DDL = "create table " + TABLE_PREFERENCES + "(" +
            COL_NOM + " TEXT primary key, " +
            COL_VALEUR + " INTEGER)";

    public final static String GAME_DDl = "create table " + TABLE_GAME + "(" +
            COL_ID + " INTEGER primary key autoincrement, " +
            COL_POINT + " TEXT, " +
            COL_DATE + " TEXT, " +
            COL_DATASET + " INTEGER)";

    public final static String POINT_DDl = "create table " + TABLE_POINT + "(" +
            COL_NOM_POINT + " TEXT, " +
            COL_DATASET + " INTEGER, " +
            "PRIMARY KEY(" +  COL_NOM_POINT + "," + COL_DATASET + "))";




    public final static int VERSION = 10;

    public DataAccess(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(GAME_DDl);
        sqLiteDatabase.execSQL(POINT_DDl);
        sqLiteDatabase.execSQL(PREFERENCE_DDL);
        sqLiteDatabase.execSQL("insert into " + TABLE_PREFERENCES + " values('" + PREFERENCES_COULEUR + "'," + Color.CYAN + ")");
        sqLiteDatabase.execSQL("insert into " + TABLE_PREFERENCES + " values('" + PREFERENCES_DATASET + "'," + 0 + ")");
        sqLiteDatabase.execSQL("insert into " + TABLE_PREFERENCES + " VALUES('" + PREFERENCES_FIRST_RUN + "',1)");
        //creer la table dataset avec le id et le nom
        sqLiteDatabase.execSQL(DATASET_DDL);
        int idDataSet = 0;
        for (String nomData : MainActivity.NOMS_DATASET_INITIAUX) {
            sqLiteDatabase.execSQL("insert into " + TABLE_DATASET + " values(" + idDataSet + ",'" + nomData + "')");
            for (String nomPoint : MainActivity.POINTS_INITIAUX) {
                //line of error
                sqLiteDatabase.execSQL("insert into " + TABLE_POINT + " values('" + nomPoint.replaceAll("'","''") + "'," + idDataSet + ")");
            }
            idDataSet++;
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //tout inclure dans le on create avant la line of error

    }
}