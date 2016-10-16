package com.wedding.weddinghelper.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.wedding.weddinghelper.Adapter.FullScreenImageAdapter;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.fragements.PhotoFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class FullScreenViewActivity extends Activity{
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        viewPager = (ViewPager) findViewById(R.id.pager);

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        ArrayList<String> photoList = new ArrayList(Arrays.asList(PhotoFragment.miniPhotoUrls));
        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, photoList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }
}
