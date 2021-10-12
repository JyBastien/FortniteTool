package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.fortnitetool.R;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;

import modele.Score;
import utils.Persistable;
import utils.TimestampParser;

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

    public long insertPersistable(Persistable entity) {
        ContentValues cv = entity.getContentValues();
        String tableName = entity.getTableName();
        return db.insert(tableName, null, cv);
    }

    public ArrayList<Persistable> fetchAllPersistable(Persistable entity){
        ArrayList<Persistable> entities = new ArrayList<>(0);
        String[] colonnes = entity.getColonnes();
        Cursor cursor = db.query(entity.getTableName(),entity.getColonnes(),null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            try {
                entity = entity.factoryFromCursor(cursor);
                entities.add(entity);
            } catch (ParseException e) {
                Toast.makeText(context, R.string.DateIncorrecte, Toast.LENGTH_SHORT).show();
            }
            cursor.moveToNext();

        }
        cursor.close();
        return entities;
    }

    public Boolean getBoolean(){
        return null;
    }







}
