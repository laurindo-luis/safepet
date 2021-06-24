package br.ufma.lsdi.safepetmobile.databaseSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB = "safepet";
    private static int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE tutor (_id INTEGER PRIMARY KEY, name TEXT, " +
                "email TEXT, login TEXT, password TEXT, latitude DOUBLE, longitude DOUBLE);");

        sqLiteDatabase.execSQL("CREATE TABLE pet (_id INTEGER PRIMARY KEY, name TEXT, " +
                "radius_in_meters DOUBLE, id_coleira INTEGER, type INTEGER, monitored INTEGER, " +
                "tutor_id INTEGER REFERENCES tutor(_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
