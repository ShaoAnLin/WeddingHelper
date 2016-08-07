package com.wedding.weddinghelper.activities;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.fragements.JoinInfoFragment;
import com.wedding.weddinghelper.fragements.JoinPhotoFragment;
import com.wedding.weddinghelper.fragements.JoinSettingFragment;
import com.wedding.weddinghelper.fragements.JoinSurveyFragment;
import com.wedding.weddinghelper.fragements.OwnInfoFragment;
import com.wedding.weddinghelper.fragements.OwnPhotoFragment;
import com.wedding.weddinghelper.fragements.OwnSettingFragment;

public class OwnMainActivity extends AppCompatActivity
        implements View.OnClickListener {

    final private String tabInfoTag = "tabInfoTag";
    final private String tabPhotoTag = "tabPhotoTag";
    final private String tabSettingTag = "tabSettingTag";

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_main);

        // initiate action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.own_main_action_bar);
        setSupportActionBar(actionBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_white);
            getSupportActionBar().setTitle(getString(R.string.wedding_info));
            getSupportActionBar().setShowHideAnimationEnabled(true);
        }

        // set close icon on click listener
        if (actionBar != null) {
            actionBar.setNavigationOnClickListener(this);
        }

        // set up tab host
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.container);

        // The tab can only show image or text?
        mTabHost.addTab(mTabHost.newTabSpec(tabInfoTag)
                        .setIndicator(
                                "",
                                ResourcesCompat.getDrawable(getResources(), R.drawable.ic_place_24dp, null)),
                OwnInfoFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(tabPhotoTag)
                        .setIndicator(
                                "",
                                ResourcesCompat.getDrawable(getResources(), R.drawable.ic_collections_24dp, null)),
                OwnPhotoFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(tabSettingTag)
                        .setIndicator(
                                "",
                                ResourcesCompat.getDrawable(getResources(), R.drawable.ic_settings_24dp, null)),
                OwnSettingFragment.class, null);


        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String str) {
                if (str.equals(tabInfoTag)){
                    Log.d("Tab", str);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(getString(R.string.wedding_info));
                    }
                }
                else if (str.equals(tabPhotoTag)){
                    Log.d("Tab", str);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(getString(R.string.wedding_photo));
                    }
                }
                else if (str.equals(tabSettingTag)){
                    Log.d("Tab", str);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(getString(R.string.setting));
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
