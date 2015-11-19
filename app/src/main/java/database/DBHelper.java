package database;

/**
 * Created by durley on 18/11/15.
 */

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String LUGARES_TABLE_NAME = "lugares";
    public static final String LUGARES_COLUMN_ID = "id";
    public static final String LUGARES_COLUMN_NOMBRE = "nombre";
    public static final String LUGARES_COLUMN_URL = "url";
    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table lugares " +
                        "(id integer primary key, nombre text,url text,ip text,serial text,usuario text,contra text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS lugares");
        onCreate(db);
    }

    public boolean insertLugar  (String nombre, String url)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre", nombre);
        contentValues.put("url", url);
        db.insert("lugares", null, contentValues);
        return true;
    }

    public int getLugar(){
        SQLiteDatabase db = this.getReadableDatabase();
        int id_lugar=0;
        Cursor res =  db.rawQuery("select * from lugares", null);

        res.moveToFirst();

        id_lugar=res.getInt(res.getColumnIndex("id"));

        res.close();

        return id_lugar;

    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from lugares where id=" + id + "", null);
        return res;
    }

    public int checkForTables(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(*) FROM lugares", null);

        if(res != null){

            res.moveToFirst();

            int count = res.getInt(0);

            if(!res.isClosed()) {
                res.close();
            }

            if(count == 1){
                return 1;
            }else if(count > 1){
                return count;
            }

        }

        return 0;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, LUGARES_TABLE_NAME);
        return numRows;
    }

    public boolean updateLugar (Integer id, String nombre, String url)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre", nombre);
        contentValues.put("url", url);
        db.update("lugares", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteLugar (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("lugares",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllLugares()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from lugares", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String Index=res.getString(res.getColumnIndex(LUGARES_COLUMN_ID));
            String Name=res.getString(res.getColumnIndex(LUGARES_COLUMN_NOMBRE));
            array_list.add(Index+") " +Name);
            res.moveToNext();
        }

        res.close();

        return array_list;
    }


}