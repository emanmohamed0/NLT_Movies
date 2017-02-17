package com.example.emyeraky.nlt_movies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    MovieData[] movieData;
    static Context context;

    @Override
    protected void onStart() {
        super.onStart();
        if(isNetworkConnected()){
            FetchImage fetchImage = new FetchImage();
            fetchImage.execute("popular?");
        }
        else
            Toast.makeText(MainActivity.this, "Not connected Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getBaseContext();
        gridView = (GridView) findViewById(R.id.show_image);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                       Intent ii = new Intent(MainActivity.this, DetailActivity.class);
                        DetailActivity.movieData = movieData[i];
                        startActivity(ii);

            }
        });

        if(isNetworkConnected()){
            FetchImage fetchImage = new FetchImage();
            fetchImage.execute("popular?");
        }
        else
            Toast.makeText(MainActivity.this, "Not connected Internet", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.high_rate&&isNetworkConnected()){
            FetchImage fetchImage = new FetchImage();
            fetchImage.execute("top_rated?");
        }
        else if (id==R.id.popular&&isNetworkConnected()){
            FetchImage fetchImage = new FetchImage();
            fetchImage.execute("popular?");
        }

        return super.onOptionsItemSelected(item);
    }

    // use to tell connection network
    static  boolean isNetworkConnected(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
        JSONArray jsonArray =jsonObject.getJSONArray("results");
        movieData= new MovieData[jsonArray.length()];
        for(int i = 0; i < jsonArray.length(); i++) {
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

