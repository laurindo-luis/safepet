package br.ufma.lsdi.safepetmobile.ServicesSQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.ufma.lsdi.safepetmobile.databaseSQLite.DatabaseHelper;
import br.ufma.lsdi.safepetmobile.domain.Pet;
import br.ufma.lsdi.safepetmobile.domain.SafePlace;
import br.ufma.lsdi.safepetmobile.domain.Tutor;

public class PetServiceSQLite {

    public static long save (DatabaseHelper databaseHelper, Pet pet) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        return sqLiteDatabase.insert("pet", null, setContentValues(pet));
    }

    public static void update (DatabaseHelper databaseHelper, Pet pet) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.update("pet", setContentValues(pet), "_id = ?", new String[] {String.valueOf(pet.getId())});
    }

    public static List<Pet> getPets(DatabaseHelper databaseHelper) {
        List<Pet> pets = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM pet", null);
        cursor.moveToFirst();

        for (int i = 0 ; i < cursor.getCount(); i++) {
            pets.add(setPet(cursor));
            cursor.moveToNext();
        }

        return pets;
    }

    private static ContentValues setContentValues(Pet pet) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", pet.getId());
        contentValues.put("name", pet.getName());
        contentValues.put("type", pet.getType());
        contentValues.put("radius_in_meters", pet.getRadiusInMeters());
        contentValues.put("id_coleira", pet.getIdColeira());
        contentValues.put("tutor_id", pet.getTutorId());

        int monitored = pet.isMonitored() ? 1 : 0;
        contentValues.put("monitored", monitored);


        return contentValues;
    }

    private static Pet setPet(Cursor cursor) {
        int indexId = cursor.getColumnIndex("_id");
        int indexName = cursor.getColumnIndex("name");
        int indexRadiusInMeters = cursor.getColumnIndex("radius_in_meters");
        int indexType = cursor.getColumnIndex("type");
        int indexIdColeira = cursor.getColumnIndex("id_coleira");
        int indexTutorId = cursor.getColumnIndex("tutor_id");
        int intexMonitored = cursor.getColumnIndex("monitored");

        boolean isMonitored = cursor.getInt(intexMonitored) == 1;

        return new Pet(cursor.getLong(indexId), cursor.getString(indexName),
                cursor.getDouble(indexRadiusInMeters), cursor.getInt(indexType),
                cursor.getInt(indexIdColeira), cursor.getLong(indexTutorId),
                isMonitored);
    }
}
