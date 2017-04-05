package com.ywk.popmovies.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2016/4/30.
 */
public class BaseActivity extends AppCompatActivity{


    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    }
}
