package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataAccess extends SQLiteOpenHelper {
    public final static String BD_NOM = "FortniteTool";

    public final static String TABLE_GAME = "Game";
    public final static String COL_ID = "_id";
    public final static String COL_POINT = "Point";
    public final static String COL_DATE = "Date";

    public final static String TABLE_SCORE = "Score";
    public final static String COL_NOM_JOUEUR = "Point";
    public final static String COL_SCORE = "Score";

    public final static String GAME_DDl = "create table " + TABLE_GAME + "(" +
            COL_ID + " INTEGER primary key autoincrement, " +
            COL_POINT + " TEXT, " +
            COL_DATE + " TEXT)";

    public final static String SCORE_DDl = "create table " + TABLE_SCORE + "(" +
            COL_ID + " INTEGER primary key autoincrement, " +
            COL_NOM_JOUEUR + " TEXT, " +
            COL_SCORE + " INTEGER, " +
            COL_DATE + " TEXT)";

    public final static int VERSION = 1;

    public DataAccess(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(GAME_DDl);
        sqLiteDatabase.execSQL(SCORE_DDl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}