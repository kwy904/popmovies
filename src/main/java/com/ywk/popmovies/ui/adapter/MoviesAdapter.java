package com.ywk.popmovies.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ywk.popmovies.R;
import com.ywk.popmovies.ui.entity.MoviesEntity;
import com.ywk.popmovies.utils.Const;
import com.ywk.popmovies.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/1.
 */
public class MoviesAdapter extends ArrayAdapter<MoviesEntity> {

    private final String LOG_TAG = MoviesAdapter.class.getSimpleName();

    private Context mContext;
    private List<MoviesEntity> items;

    private boolean footerViewEnable = false;
    private LinearLayout footerView;

    public MoviesAdapter(Context context, List<MoviesEntity> moviesEntities) {
        super(context, 0, moviesEntities);

        this.mContext = context;
        if (moviesEntities == null) {
            moviesEntities = new ArrayList<MoviesEntity>();
        }
        this.items = moviesEntities;
    }

    public void removeLastItem() {
        items.remove(items.size() - 1);
        notifyDataSetChanged();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == items.size() - 1 && footerViewEnable) {
            if (footerView == null) {
                footerView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.listview_loading_view, null);
                GridView.LayoutParams pl = new GridView.LayoutParams(
                        DensityUtil.getScreenWidth(getContext()),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                footerView.setLayoutParams(pl);
            }
            return footerView;
        }
        ViewHolder viewHolder;
        if (convertView == null || (convertView != null && convertView == footerView)) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.imageview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemview = (ImageView) convertView
                    .findViewById(R.id.imageview);
            convertView.setTag(viewHolder);
           /* ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getContext(), 180)));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            convertView = imageView;*/
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MoviesEntity moviesEntity = items.get(position);
        Picasso.with(getContext()).load(Const.URL_BASE_IMG + moviesEntity.getPoster_path()).into(viewHolder.itemview);
        return convertView;
    }

    public static class ViewHolder {

        public ImageView itemview;

    }

    public void setFootreViewEnable(boolean enable) {
        footerViewEnable = enable;
    }

    public boolean isFooterViewEnable() {
        return footerViewEnable;
    }
}
