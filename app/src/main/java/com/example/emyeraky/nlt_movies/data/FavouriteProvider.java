package com.example.emyeraky.nlt_movies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Emy Eraky on 3/11/2017.
 */
public class FavouriteProvider extends ContentProvider {

    FavouriteDbHelper favouriteDbHelper;
    public final UriMatcher uriMatcher= buildUriMatcher();

    static final int MOVIE_DETAILS = 100;
    static final int MOVIEDETAILS_WITH_ID = 101;

    static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavouriteContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, FavouriteContract.PATH_FAVOURITE, MOVIE_DETAILS);
        uriMatcher.addURI(authority, FavouriteContract.PATH_FAVOURITE + "/*", MOVIEDETAILS_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        favouriteDbHelper = new FavouriteDbHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIE_DETAILS:
                return FavouriteContract.FavouriteEntry.CONTENT_TYPE;
            case MOVIEDETAILS_WITH_ID:
                return FavouriteContract.FavouriteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = favouriteDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);

        switch (match) {
            case MOVIE_DETAILS:
                cursor = db.query(
                        FavouriteContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIEDETAILS_WITH_ID:
                long _id = ContentUris.parseId(uri);
                cursor = db.query(
                        FavouriteContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        FavouriteContract.FavouriteEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = favouriteDbHelper.getWritableDatabase();
        int match  = uriMatcher.match(uri);
        Uri returnedUri;

        switch (match){
            case MOVIE_DETAILS:
                long _id = db.insert(FavouriteContract.FavouriteEntry.TABLE_NAME, null, values);
                if(_id != -1){
                    returnedUri = FavouriteContract.FavouriteEntry.buildDetailsUri(_id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnedUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = favouriteDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowdelete;
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIE_DETAILS:
                rowdelete = db.delete(FavouriteContract.FavouriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowdelete != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowdelete;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
