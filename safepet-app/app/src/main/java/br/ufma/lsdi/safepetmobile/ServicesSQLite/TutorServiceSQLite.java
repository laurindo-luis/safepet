package br.ufma.lsdi.safepetmobile.ServicesSQLite;

import android.app.job.JobInfo;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.ufma.lsdi.safepetmobile.databaseSQLite.DatabaseHelper;
import br.ufma.lsdi.safepetmobile.domain.SafePlace;
import br.ufma.lsdi.safepetmobile.domain.Tutor;

public class TutorServiceSQLite {

    public static long save (DatabaseHelper databaseHelper, Tutor tutor) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        return sqLiteDatabase.insert("tutor", null, setContentValues(tutor));
    }

    public static boolean login (DatabaseHelper databaseHelper, String userName, String password) {
        Tutor tutor = getTutor(databaseHelper, userName);
        if(tutor != null)
            if(tutor.getPassword().equals(password))
                return true;

        return false;
    }

    public static Tutor getTutor (DatabaseHelper databaseHelper) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tutor", null);

        Tutor tutor = null;
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            tutor = setTutor(cursor);
        }
        return tutor;
    }

    public static Tutor getTutor(DatabaseHelper databaseHelper, String userName) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tutor WHERE login = ?", new String[] {userName});

        Tutor tutor = null;
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            tutor = setTutor(cursor);
        }
        return tutor;
    }

    public static Tutor getTutor(DatabaseHelper databaseHelper, long id) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tutor WHERE _id = ?", new String[] {String.valueOf(id)});

        Tutor tutor = null;
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            tutor = setTutor(cursor);
        }
        return tutor;
    }

    private static ContentValues setContentValues(Tutor tutor) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", tutor.getId());
        contentValues.put("name", tutor.getName());
        contentValues.put("email", tutor.getEmail());
        contentValues.put("login", tutor.getLogin());
        contentValues.put("password", tutor.getPassword());
        contentValues.put("latitude", tutor.getSafePlace().getLatitude());
        contentValues.put("longitude", tutor.getSafePlace().getLongitude());
        return contentValues;
    }

    private static Tutor setTutor(Cursor cursor) {
        int indexId = cursor.getColumnIndex("_id");
        int indexName = cursor.getColumnIndex("name");
        int indexLogin = cursor.getColumnIndex("login");
        int indexEmail = cursor.getColumnIndex("email");
        int indexPassword = cursor.getColumnIndex("password");
        int indexLatitude = cursor.getColumnIndex("latitude");
        int indexLongitude = cursor.getColumnIndex("longitude");

        SafePlace safePlace = new SafePlace(cursor.getDouble(indexLatitude),
                cursor.getDouble(indexLongitude));

        return new Tutor(cursor.getLong(indexId), cursor.getString(indexName),
                cursor.getString(indexEmail), cursor.getString(indexLogin),
                cursor.getString(indexPassword), safePlace);
    }
}
