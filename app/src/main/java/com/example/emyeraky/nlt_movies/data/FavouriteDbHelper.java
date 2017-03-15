package com.example.emyeraky.nlt_movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.emyeraky.nlt_movies.data.FavouriteContract.*;


/**
 * Created by Emy Eraky on 3/11/2017.
 */
public class FavouriteDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "movie.db";
    static final int DATABASE_VERSION = 2;

    public FavouriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create TABLE
        final String CREATE_TABLE="CREATE TABLE "+FavouriteContract.FavouriteEntry.TABLE_NAME+
                "("+FavouriteContract.FavouriteEntry.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                FavouriteContract.FavouriteEntry.MID+" TEXT,"+FavouriteContract.FavouriteEntry.ORIGINAL_TITLE+
                " TEXT,"+FavouriteContract.FavouriteEntry.TIME+" TEXT,"+FavouriteContract.FavouriteEntry.DATE+
                " TEXT,"+FavouriteContract.FavouriteEntry.POSTER_PATH+" TEXT,"+FavouriteContract.FavouriteEntry.POPULARITY+" TEXT);";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
