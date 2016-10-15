package com.wedding.weddinghelper.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.wedding.weddinghelper.Adapter.FullScreenImageAdapter;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.Util.Utils;
import com.wedding.weddinghelper.fragements.PhotoFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class FullScreenViewActivity extends Activity{

    private Utils utils;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        viewPager = (ViewPager) findViewById(R.id.pager);
        utils = new Utils(getApplicationContext());

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        //adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, utils.getFilePaths());
        ArrayList<String> photoList = new ArrayList(Arrays.asList(PhotoFragment.miniPhotoUrls));
        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, photoList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }
}
