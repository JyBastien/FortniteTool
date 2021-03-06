package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import androidx.annotation.Nullable;

import com.example.fortnitetool.R;

import activity.MainActivity;
/*Classe qui retient les informations de la database*/
public class DataAccess extends SQLiteOpenHelper {
    public final static String BD_NOM = "FortniteTool13";

    /*Table qui retiendra les noms personnalisés des datasets*/
    public final static String TABLE_DATASET = "DataSet";
    //  col - id , col - nom

    /*table qui retiendra les preferences de l'utilisateur soit
    * la couleur du graphique, le dataset selectionné et si c'est la première fois
    * que l'application est exécutée*/
    public final static String TABLE_PREFERENCES = "Preferences";
    public final static String COL_NOM = "Nom";
    public final static String COL_VALEUR = "Valeur";
    public final static String PREFERENCES_COULEUR = "couleur";
    public final static String PREFERENCES_DATASET = "dataset";
    public static final String PREFERENCES_FIRST_RUN = "firstRun";

    /*Table qui retient le dataset, le nom et la date des parties*/
    public final static String TABLE_PARTIE = "Partie";
    public final static String COL_ID = "_id";
    public final static String COL_POINT = "Point";
    public final static String COL_DATE = "Date";
    //col - dataset

    /*table qui retien les noms de points et le dataset correspondant*/
    public final static String TABLE_POINT = "Point";
    public final static String COL_NOM_POINT = "Nom";
    public static final String COL_DATASET = "DataSet";

    /*scripts de création*/
    public final static String DATASET_DDL = "create table " + TABLE_DATASET + "(" +
            COL_ID + " INTEGER primary key, " +
            COL_NOM + " TEXT)";

    public final static String PREFERENCE_DDL = "create table " + TABLE_PREFERENCES + "(" +
            COL_NOM + " TEXT primary key, " +
            COL_VALEUR + " INTEGER)";

    public final static String GAME_DDl = "create table " + TABLE_PARTIE + "(" +
            COL_ID + " INTEGER primary key autoincrement, " +
            COL_POINT + " TEXT, " +
            COL_DATE + " TEXT, " +
            COL_DATASET + " INTEGER)";

    public final static String POINT_DDl = "create table " + TABLE_POINT + "(" +
            COL_NOM_POINT + " TEXT, " +
            COL_DATASET + " INTEGER, " +
            "PRIMARY KEY(" +  COL_NOM_POINT + "," + COL_DATASET + "))";
    public final static int VERSION = 11;
    private Context context;

    public DataAccess(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        creerTables(sqLiteDatabase);
        insertPeferences(sqLiteDatabase);
        insertPointsInitiaux(sqLiteDatabase);
    }
    /*Insère les points initiaux de chaque dataSet*/
    private void insertPointsInitiaux(SQLiteDatabase sqLiteDatabase) {
        String[] POINTS_INITIAUX = {context.getResources().getString(R.string.complexite),context.getResources().getString(R.string.fournisseur),context.getResources().getString(R.string.imprevu)};
        int idDataSet = 0;
        for (String nomData : MainActivity.NOMS_DATASET_INITIAUX) {
            sqLiteDatabase.execSQL("insert into " + TABLE_DATASET + " values(" + idDataSet + ",'" + nomData + "')");
            for (String nomPoint : POINTS_INITIAUX) {
                sqLiteDatabase.execSQL("insert into " + TABLE_POINT + " values('" + nomPoint.replaceAll("'","''") + "'," + idDataSet + ")");
            }
            idDataSet++;
        }
    }
    /*exécute les scripts de création de tables*/
    private void creerTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(GAME_DDl);
        sqLiteDatabase.execSQL(POINT_DDl);
        sqLiteDatabase.execSQL(PREFERENCE_DDL);
        sqLiteDatabase.execSQL(DATASET_DDL);
    }

    /*insertion des valeurs de bases des preferences*/
    private void insertPeferences(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("insert into " + TABLE_PREFERENCES + " values('" + PREFERENCES_COULEUR + "'," + Color.CYAN + ")");
        sqLiteDatabase.execSQL("insert into " + TABLE_PREFERENCES + " values('" + PREFERENCES_DATASET + "'," + 0 + ")");
        sqLiteDatabase.execSQL("insert into " + TABLE_PREFERENCES + " values('" + PREFERENCES_FIRST_RUN + "',1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}