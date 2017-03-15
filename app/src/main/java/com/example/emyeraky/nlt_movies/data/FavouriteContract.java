package com.example.emyeraky.nlt_movies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Emy Eraky on 3/11/2017.
 */
public class FavouriteContract {

    public static final String CONTENT_AUTHORITY = "com.example.emyeraky.nlt_movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITE = "favourite";

    public static final class FavouriteEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";
        public static final String ID="_id";
        public static final String MID="id";
        public static final String POSTER_PATH = "poster_path" ;
        public static final String TIME = "time" ;
        public static final String ORIGINAL_TITLE ="original_title" ;
        public static final String POPULARITY = "popularity" ;
        public static final String DATE = "date";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;


        public static Uri buildDetailsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}