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

        final String SQL_CREATE_Favourite_TABLE = "CREATE TABLE " + FavouriteEntry.TABLE_NAME + " (" +
                FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavouriteEntry.COLUMN_MOVIENAME + " TEXT NOT NULL," +
                FavouriteEntry.COLUMN_RATE + " Text NOT NULL," +
                FavouriteEntry.COLUMN_DATE + " TEXT DEFAULT SYSDATE," +
                FavouriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                FavouriteEntry.COLUMN_IMAGE + " TEXT NOT NULL" +
                " )";

        db.execSQL(SQL_CREATE_Favourite_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
