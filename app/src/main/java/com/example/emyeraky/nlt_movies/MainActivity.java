package com.example.emyeraky.nlt_movies;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.emyeraky.nlt_movies.data.FavouriteContract;

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

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    MovieData[] movieData;
    static Context context;
    ArrayList<MovieData> movieDataList;
    static String stateaction = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getBaseContext();
        gridView = (GridView) findViewById(R.id.show_image);

        //check Internet connected
        if (isNetworkConnected()) {
            // notify user you are online
            if(stateaction.equals("popular")){
                FetchImage fetchImage = new FetchImage();
                fetchImage.execute("popular?");
                stateaction = "popular";
            }
            else if(stateaction.equals("top_rated")){
                FetchImage fetchImage = new FetchImage();
                fetchImage.execute("top_rated?");
                stateaction = "top_rated";
            }
            else if(stateaction.equals("favorite")){
                favoritmovie();
                stateaction = "favorite";
            }else {
                FetchImage fetchImage = new FetchImage();
                fetchImage.execute("popular?");
                stateaction = "popular";
            }

        } else {
            Toast.makeText(getBaseContext(), "No Internet connected!!", Toast.LENGTH_SHORT).show();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent ii = new Intent(MainActivity.this, DetailActivity.class);
                DetailActivity.movieData = movieData[i];
                startActivity(ii);

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        //check Internet connected
        if (isNetworkConnected()) {
            // notify user you are online
            if (stateaction.equals("popular")) {
                FetchImage fetchImage = new FetchImage();
                fetchImage.execute("popular?");
                stateaction = "popular";
                Toast.makeText(MainActivity.this, "popular", Toast.LENGTH_SHORT).show();
            } else if (stateaction.equals("top_rated")) {
                FetchImage fetchImage = new FetchImage();
                fetchImage.execute("top_rated?");
                stateaction = "top_rated";
                Toast.makeText(MainActivity.this, "top_rated", Toast.LENGTH_SHORT).show();
            } else if (stateaction.equals("favorite")) {
                favoritmovie();
                stateaction = "favorite";
                Toast.makeText(MainActivity.this, "favorite", Toast.LENGTH_SHORT).show();
            } else {
                FetchImage fetchImage = new FetchImage();
                fetchImage.execute("popular?");
                stateaction = "popular";
                Toast.makeText(MainActivity.this, "popular", Toast.LENGTH_SHORT).show();
            }
        } else {

            Toast.makeText(getBaseContext(), "No Internet connected!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popular & isNetworkConnected()) {
            FetchImage fetchImage = new FetchImage();
            fetchImage.execute("popular?");
            stateaction = "popular";
            Toast.makeText(MainActivity.this, "popularOption", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.high_rate && isNetworkConnected()) {
            FetchImage fetchImage = new FetchImage();
            fetchImage.execute("top_rated?");
            stateaction = "top_rated";
            Toast.makeText(MainActivity.this, "top_ratedOption", Toast.LENGTH_SHORT).show();
        }

        //return favorite from DB
        else {
            if (isNetworkConnected()) {
                favoritmovie();
                stateaction = "favorite";
                Toast.makeText(MainActivity.this, "favoriteOption", Toast.LENGTH_SHORT).show();
            } else {
                favoritmovie();
                Toast.makeText(MainActivity.this, "No Internet Connected", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    // use to tell connection network
    static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void favoritmovie() {
        movieDataList = new ArrayList<>();
        MovieData movie;

        String [] column ={DBHelper.MID,DBHelper.ORIGINAL_TITLE,DBHelper.TIME,DBHelper.DATE,DBHelper.POSTER_PATH,DBHelper.POPULARITY};


        try {
            Cursor cursor = getContentResolver().query(
                    FavouriteContract.FavouriteEntry.CONTENT_URI,   // The content URI of the words table
                    column,                        // The columns to return for each row
                    null,                   // Selection criteria
                    null,                     // Selection criteria
                    null);
//            Cursor cursor = dbController.get_dataselect();
            if (cursor.moveToFirst()) {
                do {
                    movie = new MovieData();
                    movie.setID(cursor.getString(0));
                    movie.setOriginal_title(cursor.getString(1));
                    movie.setTime(cursor.getString(2));
                    movie.setRelease_date(cursor.getString(3));
                    movie.setPoster_path(cursor.getString(4));
                    movie.setVote_average(cursor.getString(5));

                    movieDataList.add(movie);
                } while (cursor.moveToNext());
                String[] poster = new String[movieDataList.size()];

                for (int i = 0; i < poster.length; i++) {
                    poster[i] = movieDataList.get(i).getPoster_path();
                }
                //set image in grid to display
                AdapterImage adapterImage = new AdapterImage(getBaseContext(), poster);
                gridView.setAdapter(adapterImage);
                movieData = new MovieData[movieDataList.size()];
                for (int i = 0; i < movieDataList.size(); i++) {
                    movieData[i] = movieDataList.get(i);
                }
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //     actionLand(flag, position);
                        Intent ii = new Intent(MainActivity.this, DetailActivity.class);
                        DetailActivity.movieData = movieData[i];
                        startActivity(ii);
                    }
                });

            }
            }catch (Exception e) {
            e.printStackTrace();
        }

    }


    //get data from Json
    private MovieData[] getImageDataFromJson(String imageJsonStr) throws JSONException {

        final String id = "id";
        final String poster_path = "poster_path";
        final String original_title = "original_title";
        final String vote_average = "vote_average";
        final String overview = "overview";
        final String release_date = "release_date";

        JSONObject jsonObject = new JSONObject(imageJsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        movieData = new MovieData[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject image = jsonArray.getJSONObject(i);

            movieData[i] = new MovieData();
            movieData[i].setID(image.getString(id));
            movieData[i].setPoster_path(image.getString(poster_path));
            movieData[i].setOriginal_title(image.getString(original_title));
            movieData[i].setOverview(image.getString(overview));
            movieData[i].setRelease_date(image.getString(release_date));
            movieData[i].setVote_average(image.getString(vote_average));
        }

        return movieData;
    }

    public class FetchImage extends AsyncTask<String, ProgressDialog, MovieData[]> {
        private String LOG_TAG = FetchImage.class.getSimpleName();

        @Override
        protected MovieData[] doInBackground(String... params) {

            if (params.length == 0) {

                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String imageJsonStr = null;

            try {
                final String FORECAST_BASE_URL =
                        "https://api.themoviedb.org/3/movie/" + params[0];
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
                imageJsonStr = buffer.toString();
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
                return getImageDataFromJson(imageJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieData[] movieDatas) {
            String[] posters = new String[movieDatas.length];

            if (movieDatas != null) {
                for (int i = 0; i < movieDatas.length; i++) {

                    posters[i] = movieDatas[i].getPoster_path();
                }

                AdapterImage adapterImage = new AdapterImage(getBaseContext(), posters);
                gridView.setAdapter(adapterImage);
            }
        }
    }

}

