package com.ywk.popmovies.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ywk.popmovies.BuildConfig;
import com.ywk.popmovies.R;
import com.ywk.popmovies.ui.activity.MoviesDetailActivity;
import com.ywk.popmovies.ui.adapter.MoviesAdapter;
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
 * Created by Administrator on 2016/4/30.
 */
public class MoviesListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    private final String LOG_TAG = MoviesListFragment.class.getSimpleName();

    private GridView gridView;
    private MoviesAdapter moviesAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean isLastRow = false;
    private boolean isLoading = false;
    private boolean isMore = true;
    private boolean isRefresh = false;
    private int mPage = 0;
    private FetchMoviesListTask fetchMoviesListTask;
    private final int PAGE_SIZE = 20;
    private String mSort_order;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movieslist, container, false);
        setupView(view);
        getMoviesListData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String currenSort_order = mSharedPref.getString(getString(R.string.sort_orde), getString(R.string.sort_orde_default));
        if (!currenSort_order.equals(mSort_order)) {
            mSort_order = currenSort_order;
            isRefresh = true;
            getMoviesListData();
        }

    }

    private void setupView(View v) {
        gridView = (GridView) v.findViewById(R.id.gridview_fragment_movieslist);
        moviesAdapter = new MoviesAdapter(getActivity(), new ArrayList<MoviesEntity>());
        gridView.setAdapter(moviesAdapter);
        gridView.setOnScrollListener(onScrollListener);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MoviesEntity moviesEntity = (MoviesEntity) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), MoviesDetailActivity.class);
                intent.putExtra("moviesId",moviesEntity.getId());
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
       /* swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/
        SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSort_order = mSharedPref.getString(getString(R.string.sort_orde), getString(R.string.sort_orde_default));
    }

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (isLastRow && scrollState == SCROLL_STATE_IDLE) {
                if (!isLoading && isMore) {
                    getMoviesListData();
                }
                isLastRow = false;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
                isLastRow = true;
            }
        }
    };

    private void getMoviesListData() {
        fetchMoviesListTask = new FetchMoviesListTask();
        fetchMoviesListTask.execute();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPage = 0;
        getMoviesListData();
    }


    class FetchMoviesListTask extends AsyncTask<Void, Void, List<MoviesEntity>> {

        @Override
        protected List<MoviesEntity> doInBackground(Void... params) {
            isLoading = true;
            mPage++;


            Uri uri = Uri.parse(Const.URL_BASE + mSort_order).buildUpon()
                    .appendQueryParameter(Const.PAGE, String.valueOf(mPage))
                    .appendQueryParameter(Const.APP_KEY, BuildConfig.OPEN_MOVIES_API_KEY)
                    .build();

            Log.d(LOG_TAG, uri.toString());

            BufferedReader reader = null;
            HttpURLConnection urlConnection = null;
            String moviesListStr = null;

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
                moviesListStr = stringBuffer.toString();
                Log.d(LOG_TAG, "moviesListStr : " + moviesListStr);
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
            List<MoviesEntity> moviesEntities = getMoviesDataFromJson(moviesListStr);
            return moviesEntities;
        }


        @Override
        protected void onPostExecute(List<MoviesEntity> moviesEntities) {
            super.onPostExecute(moviesEntities);

            // Before you add data to delete the last item forgery
            if (moviesAdapter.isFooterViewEnable()) {
                moviesAdapter.removeLastItem();
            }

            if (moviesEntities != null) {
                if (isRefresh) {
                    moviesAdapter.clear();
                }
                for (int i = 0; i < moviesEntities.size(); i++) {
                    moviesAdapter.add(moviesEntities.get(i));
                }
                if (moviesEntities.size() < 20) {
                    isMore = false;
                    moviesAdapter.setFootreViewEnable(false);
                    Toast.makeText(getActivity(), R.string.not_more, Toast.LENGTH_LONG).show();
                } else {
                    moviesAdapter.setFootreViewEnable(true);
                    moviesAdapter.add(null);
                }

            } else {
                Toast.makeText(getActivity(), R.string.load_erro, Toast.LENGTH_LONG).show();
            }

            if (isRefresh) {
                isRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
            }
            isLoading = false;
        }

        private List<MoviesEntity> getMoviesDataFromJson(String moviesStr) {
            if (moviesStr == null) {
                return null;
            }
            Gson gson = new Gson();
            List<MoviesEntity> moviesEntities = new ArrayList<MoviesEntity>();
            try {
                JSONObject jsonObject = new JSONObject(moviesStr);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    MoviesEntity moviesEntity = gson.fromJson(jsonArray.getJSONObject(i).toString(), MoviesEntity.class);
                    moviesEntities.add(moviesEntity);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return moviesEntities;
        }
    }

}
