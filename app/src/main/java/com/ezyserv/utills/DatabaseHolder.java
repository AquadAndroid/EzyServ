package com.ezyserv.utills;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ezyserv.adapter.PlaceAutocompleteAdapter;

import java.util.ArrayList;

/**
 * Created by Hector on 12/30/17.
 */

public class DatabaseHolder extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHolder.class.getSimpleName();

    private static final int DATABASE_VESION = 1;
    private static final String DATABASE_NAME = "EsyServ";
    private static final String TABLE_FAVOURITE = "favourite";

    private static final String placeId = "placeId";
    private static final String description = "title";
    private static final String favourite = "favourite";

    public DatabaseHolder(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VESION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE);
        String CREATE_FAVOURITES_TABLE = "CREATE TABLE " + TABLE_FAVOURITE + "("
                + placeId + " TEXT,"
                + description + " TEXT, "
                + favourite + " TEXT )";
        sqLiteDatabase.execSQL(CREATE_FAVOURITES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(TAG, "onUpgrade: ");
    }


    public void addFavourites(PlaceAutocompleteAdapter.PlaceAutocomplete favouritesModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(placeId, String.valueOf(favouritesModel.placeId));
        values.put(description, String.valueOf(favouritesModel.description));
        values.put(favourite, "yes");
        database.insert(TABLE_FAVOURITE, null, values);
    }

    public ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> getAllFavourites() {
        ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> contactList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PlaceAutocompleteAdapter.PlaceAutocomplete dataGetQuoteModel = new PlaceAutocompleteAdapter.PlaceAutocomplete();
                Log.w(TAG, "ID Favourite= " + cursor.getString(0));
                dataGetQuoteModel.placeId = cursor.getString(0);
                dataGetQuoteModel.description = cursor.getString(1);
                dataGetQuoteModel.favourite = cursor.getString(2);
                contactList.add(dataGetQuoteModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    public PlaceAutocompleteAdapter.PlaceAutocomplete getPlaceById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVOURITE, new String[]{placeId,
                        description, favourite}, placeId + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PlaceAutocompleteAdapter.PlaceAutocomplete contact = new PlaceAutocompleteAdapter.PlaceAutocomplete(cursor.getString(0),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

    // Deleting single contact
    public void removeFavourite(String Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVOURITE, placeId + " = ?",
                new String[]{Id});
        db.close();
    }

}
