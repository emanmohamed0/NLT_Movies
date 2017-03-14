package com.example.emyeraky.nlt_movies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Emy Eraky on 3/9/2017.
 */

public class DBHelper extends ContentProvider{
        //database_declare
    private static final String DATABASE_NAME="MovieApp";
    private static final int DATABASE_VERSION=2;

    //tables
    public static final String TABLE_NAME="MovieData";

    //columns
    public static final String ID="_id";
    public static final String MID="id";
    public static final String POSTER_PATH = "poster_path" ;
    public static final String TIME = "time" ;
    public static final String ORIGINAL_TITLE ="original_title" ;
    public static final String POPULARITY = "popularity" ;
    public static final String DATE = "date";

    Moviebase moviebase;

    private class Moviebase extends SQLiteOpenHelper {
        public Moviebase(Context context){
            super(context, DATABASE_NAME, null , DATABASE_VERSION);
        }
        //create TABLE
    String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+
            "("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            MID+" TEXT,"+ORIGINAL_TITLE+" TEXT,"+TIME+" TEXT,"+DATE+" TEXT,"+POSTER_PATH+" TEXT,"+POPULARITY+" TEXT);";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
    }

    final static int movies=1;
    final static int movies_id=2;
    public final static  String providerName="com.example.emyeraky.nlt_movies.DBHelper";
    public final static  String URL="content://"+providerName+"/"+"movies";

    public final static Uri myUrl = Uri.parse(URL);
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(providerName, "movies", movies);
        sURIMatcher.addURI(providerName,  "movies/#", movies_id);
    }
    @Override
    public boolean onCreate() {
        moviebase=new Moviebase(getContext());

        if(moviebase==null)
            return false;
        else
            return true;
    }

    SQLiteDatabase database;
    public void open() throws SQLiteException {
        try {
            database = moviebase.getWritableDatabase();
        }
        catch (SQLException ex){
            database = moviebase.getReadableDatabase();
        }

    }
    public void close_db(){
         moviebase.close();
    }
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        open();
        SQLiteQueryBuilder qp=new SQLiteQueryBuilder();
        qp.setTables(Provide.TABLE_NAME);
        SQLiteDatabase dp=moviebase.getWritableDatabase();
        Cursor c=qp.query(dp,strings,null,null,null,null,null);
        c.setNotificationUri(getContext().getContentResolver(),uri);
        close_db();
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        moviebase = new Moviebase(getContext());
        open();
        SQLiteDatabase db=moviebase.getWritableDatabase();
        long row= db.insert(Provide.TABLE_NAME, null, contentValues);

        if(row>0){
            Toast.makeText(DetailActivity.context, "success to insert row into ", Toast.LENGTH_SHORT).show();
          //  ("success to insert row into " + uri);
        }else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }

        Uri _uri;

        _uri= ContentUris.withAppendedId(myUrl, row);
        getContext().getContentResolver().notifyChange(_uri,null);


        return _uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db=moviebase.getWritableDatabase();
        int row=db.delete(Provide.TABLE_NAME,s,null);
        getContext().getContentResolver().notifyChange(uri, null);
        return row;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
//public class DBHelper extends SQLiteOpenHelper {
//    //database_declare
//    private static final String DATABASE_NAME="MovieApp";
//    private static final int DATABASE_VERSION=2;
//
//    //tables
//    public static final String TABLE_NAME="MovieData";
//
//    //columns
//    public static final String ID="_id";
//    public static final String MID="id";
//    public static final String POSTER_PATH = "poster_path" ;
//    public static final String TIME = "time" ;
//    public static final String ORIGINAL_TITLE ="original_title" ;
//    public static final String POPULARITY = "popularity" ;
//    public static final String DATE = "date";
//
//    //create TABLE
//    String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+
//            "("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
//            MID+" TEXT,"+ORIGINAL_TITLE+" TEXT,"+TIME+" TEXT,"+DATE+" TEXT,"+POSTER_PATH+" TEXT,"+POPULARITY+" TEXT);";
//
//    public DBHelper(Context context){
//        super(context, DATABASE_NAME, null , DATABASE_VERSION);
//    }
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(CREATE_TABLE);
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(sqLiteDatabase);
//
//    }
//}
