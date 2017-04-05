package com.ywk.popmovies.ui.activity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.ywk.popmovies.BuildConfig;
import com.ywk.popmovies.R;
import com.ywk.popmovies.ui.entity.MoviesDetailEntity;
import com.ywk.popmovies.ui.entity.MoviesEntity;
import com.ywk.popmovies.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */
public class MoviesDetailActivity extends BaseActivity {

    private int moviesId;
    private static final String LOG_TAG = MoviesDetailActivity.class.getSimpleName();

    private TextView tv_movies_year, tv_movies_time, tv_movies_grade, tv_movies_title,tv_movies_explain;
    private ImageView iv_thumbnail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_moviesdetail);
        setupView();


        moviesId = getIntent().getIntExtra("moviesId", 0);
        new FechMoviesDetailTask().execute();
    }


    public void setupView() {

        tv_movies_year = (TextView) findViewById(R.id.tv_movies_year);
        tv_movies_time = (TextView) findViewById(R.id.tv_movies_time);
        tv_movies_grade = (TextView) findViewById(R.id.tv_movies_grade);
        tv_movies_title = (TextView) findViewById(R.id.tv_movies_title);
        tv_movies_explain = (TextView) findViewById(R.id.tv_movies_explain);
        iv_thumbnail = (ImageView) findViewById(R.id.iv_thumbnail);


    }


    class FechMoviesDetailTask extends AsyncTask<Void, Void, MoviesDetailEntity> {

        @Override
        protected MoviesDetailEntity doInBackground(Void... voids) {


            Uri uri = Uri.parse(Const.URL_BASE + moviesId).buildUpon()
                    .appendQueryParameter(Const.APP_KEY, BuildConfig.OPEN_MOVIES_API_KEY)
                    .build();

            Log.d(LOG_TAG, uri.toString());

            BufferedReader reader = null;
            HttpURLConnection urlConnection = null;
            String moviesStr = null;

            try {
                URL url = new URL(uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                StringBuffer stringBuffer = new StringBuffer();
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                if (stringBuffer.length() == 0) {
                    return null;
                }
                moviesStr = stringBuffer.toString();
                Log.d(LOG_TAG, "moviesListStr : " + moviesStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            Gson gson = new Gson();
            MoviesDetailEntity moviesDetailEntity = null;
            try {
                if (moviesStr!=null){
                    JSONObject jsonObject = new JSONObject(moviesStr);
                    moviesDetailEntity = gson.fromJson(jsonObject.toString(), MoviesDetailEntity.class);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return moviesDetailEntity;
        }


        @Override
        protected void onPostExecute(MoviesDetailEntity moviesDetailEntity) {
            super.onPostExecute(moviesDetailEntity);
if (moviesDetailEntity!=null){
    tv_movies_year.setText(moviesDetailEntity.getRelease_date());
    tv_movies_time.setText(moviesDetailEntity.getRuntime() + "min");
    tv_movies_grade.setText(moviesDetailEntity.getVote_average() + "/10");
    tv_movies_title.setText(moviesDetailEntity.getTitle());
    tv_movies_explain.setText(moviesDetailEntity.getOverview());
    Picasso.with(MoviesDetailActivity.this).load(Const.URL_BASE_IMG + moviesDetailEntity.getPoster_path()).into(iv_thumbnail);

}
           }
    }
}
