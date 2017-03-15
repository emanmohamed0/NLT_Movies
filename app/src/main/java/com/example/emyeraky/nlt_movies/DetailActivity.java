package com.example.emyeraky.nlt_movies;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emyeraky.nlt_movies.data.FavouriteContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    static TextView title;
    static TextView date;
    static TextView time;
    static TextView vote;
    static Button bnt_favorit;
    static String poster_url;
    static ImageView posterImage;
    static String flag;
    static String key;
    static String[] Keys;
    static String posterurl;
    static ArrayList<String> poster_List;
    static MovieData movieData;
    static Context context;
    static ListView listView_overView;
    static ListView listView_trailer;
    DBController dbController;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        listView_trailer = (ListView) findViewById(R.id.list_item);
        listView_overView = (ListView) findViewById(R.id.overview);
        bnt_favorit = (Button) findViewById(R.id.favorite);
        title = (TextView) findViewById(R.id.original_title);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        context = getBaseContext();
        vote = (TextView) findViewById(R.id.vote);
        posterImage = (ImageView) findViewById(R.id.poster_path);
        poster_List = new ArrayList<>();

        displayvideo();
        listView_trailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(poster_List.get(i)));
                startActivity(intent);
            }
        });
        favorite();
    }

    private void favorite() {
        if(movieData ==null){

        }
        else {
            if (MainActivity.isNetworkConnected()) {
                // notify user you are online
                FetchVideo video = new FetchVideo();
                video.execute(movieData.getID(), "/videos?");
                flag = "trailer";
            } else {
                Toast.makeText(context, "No Internet connected!!", Toast.LENGTH_SHORT).show();
            }

            title.setText(movieData.getOriginal_title());
            vote.setText(movieData.getVote_average());

            String baseUrl = "http://image.tmdb.org/t/p/w185";
            poster_url = baseUrl + movieData.getPoster_path();
            Picasso.with(context).load(poster_url).into(posterImage);
        }
        bnt_favorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();

                String t = title.getText().toString();
                String v = vote.getText().toString();
                String d = date.getText().toString();
                String tt = time.getText().toString();

                values.put(FavouriteContract.FavouriteEntry.MID,movieData.getID());
                values.put(FavouriteContract.FavouriteEntry.ORIGINAL_TITLE,t);
                values.put(FavouriteContract.FavouriteEntry.TIME,tt);
                values.put(FavouriteContract.FavouriteEntry.DATE,d);
                values.put(FavouriteContract.FavouriteEntry.POSTER_PATH,poster_url);
                values.put(FavouriteContract.FavouriteEntry.POPULARITY,v);

                Uri uri = getBaseContext().getContentResolver().insert(FavouriteContract.FavouriteEntry.CONTENT_URI, values);

            }


        });
    }


    void displayvideo() {
        if (movieData == null) {

        } else {
            if (MainActivity.isNetworkConnected()) {
                FetchVideo video = new FetchVideo();
                video.execute(movieData.getID(), "/videos?");
                flag = "trailer";
            } else {
                Toast.makeText(context, "No Internet connected!!", Toast.LENGTH_SHORT).show();
            }

            title.setText(movieData.getOriginal_title());
            vote.setText(movieData.getVote_average());
            date.setText(movieData.getRelease_date());

            String baseUrl = "http://image.tmdb.org/t/p/w185";
            posterurl = baseUrl + movieData.getPoster_path();
            Picasso.with(context).load(posterurl).into(posterImage);
        }
        /*bnt_favorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = title.getText().toString();
                String r = vote.getText().toString();
                String d = date.getText().toString();
                String tt = time.getText().toString();


//                if(movieData.getID()== FavoriteContract.FavoriteEntry._ID) {
                ContentValues values = new ContentValues();
                values.put(FavoriteContract.FavoriteEntry.MID, movieData.getID());
                values.put(FavoriteContract.FavoriteEntry.ORIGINAL_TITLE, t);
                values.put(FavoriteContract.FavoriteEntry.TIME, tt);
                values.put(FavoriteContract.FavoriteEntry.DATE, d);
                values.put(FavoriteContract.FavoriteEntry.POSTER_PATH, posterurl);
                values.put(FavoriteContract.FavoriteEntry.POPULARITY, r);
                Uri uri = getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI, values);
//                }
//            else
//
//            {
//                Toast.makeText(getBaseContext(), "Unfavorite!", Toast.LENGTH_SHORT).show();
//            }
//               // dbController = new DBController(context);
//
//                Uri newUri;
//9
//                ContentValues values = new ContentValues();
//
//                values.put(FavoriteContract.FavoriteEntry.MID,movieData.getID());
//                values.put(FavoriteContract.FavoriteEntry.ORIGINAL_TITLE,t);
//                values.put(FavoriteContract.FavoriteEntry.TIME,tt);
//                values.put(FavoriteContract.FavoriteEntry.DATE,d);
//                values.put(FavoriteContract.FavoriteEntry.POSTER_PATH,posterurl);
//                values.put(FavoriteContract.FavoriteEntry.POPULARITY,r);
//
//               newUri= getContentResolver().insert(
//                        FavoriteContract.FavoriteEntry.CONTENT_URI,
//                        values
//                );

//                if(newUri == DBHelper.myUrl){
//                    Toast.makeText(context,"Already insert",Toast.LENGTH_SHORT).show();
//
//                }
//                else {
//                    Toast.makeText(context,"Can't insert",Toast.LENGTH_SHORT).show();
//
//                }
//                long id = dbController.insert_db(movieData.getID(),t,posterurl,r,d,tt);
//                if (id >= 0){
//
//                    Toast.makeText(context,"Already insert",Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(context,"Can't insert",Toast.LENGTH_SHORT).show();
//                }
            }
        });*/
    }

    private static String[] getVediosKeyFromJson(String imageJsonStr) throws JSONException {

        if (flag.equals("trailer")) {
            key = "key";
        } else if (flag.equals("reviews")) {
            key = "content";
        }
        JSONObject imageJson = new JSONObject(imageJsonStr);
        JSONArray videoArray = imageJson.getJSONArray("results");

        Keys = new String[videoArray.length()];
        for (int i = 0; i < videoArray.length(); i++) {
            JSONObject videos = videoArray.getJSONObject(i);
            String keys_poster = videos.getString(key);
            Keys[i] = keys_poster;
        }

        return Keys;
    }

    public static class FetchVideo extends AsyncTask<String, ProgressDialog, String[]> {
        private String LOG_TAG = FetchVideo.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {

                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String videoJsonStr = null;

            try {
                final String FORECAST_BASE_URL =
                        "http://api.themoviedb.org/3/movie/" + params[0] + params[1];
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, "72659fcbe6b80e24ac36ddb5bbdbe316")
                        .build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                videoJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getVediosKeyFromJson(videoJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] keys_Poster) {

            if (keys_Poster == null) {
                return;
            }
            if (flag.equals("trailer")) {
                String[] trailerText = new String[keys_Poster.length];

                if (keys_Poster != null) {
                    for (int i = 0; i < keys_Poster.length; i++) {
                        poster_List.add("http://www.youtube.com/watch?v=" + keys_Poster[i]);
                        trailerText[i] = "Trailer " + i;
                    }

                    TrailerAdapter listTrailer = new TrailerAdapter(context, trailerText);
                    listView_trailer.setAdapter(listTrailer);

                }
                FetchVideo video = new FetchVideo();
                video.execute(movieData.getID(), "/reviews?");
                flag = "reviews";
            } else if (flag.equals("reviews")) {
                if (keys_Poster != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            context,
                            android.R.layout.simple_list_item_1,
                            keys_Poster);

                    listView_overView.setAdapter(adapter);
                }
            }
        }
    }
}