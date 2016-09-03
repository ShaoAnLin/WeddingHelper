package com.wedding.weddinghelper.activities;

import android.content.res.Configuration;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.fragements.PhotoFragment;
import com.wedding.weddinghelper.fragements.JoinSurveyFragment;
import com.wedding.weddinghelper.fragements.WeddingInfoFragment;


public class JoinMainActivity extends AppCompatActivity
        implements View.OnClickListener {

    final private String tabSurveyTag = "tabSurveyTag";
    final private String tabInfoTag = "tabInfoTag";
    final private String tabPhotoTag = "tabPhotoTag";
    final private String tabSettingTag = "tabSettingTag";

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_main);

        // initiate action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.join_main_action_bar);
        setSupportActionBar(actionBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_white);
            getSupportActionBar().setTitle(getString(R.string.attend_survey));
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
        // String: getResources().getText(R.string.wedding_photo)
        mTabHost.addTab(mTabHost.newTabSpec(tabSurveyTag)
                        .setIndicator(
                                "",
                                ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_box_black_24dp, null)),
                JoinSurveyFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(tabInfoTag)
                        .setIndicator(
                                "",
                                ResourcesCompat.getDrawable(getResources(), R.drawable.ic_place_24dp, null)),
                WeddingInfoFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(tabPhotoTag)
                        .setIndicator(
                                "",
                                ResourcesCompat.getDrawable(getResources(), R.drawable.ic_collections_24dp, null)),
                PhotoFragment.class, null);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String str) {
                if (str.equals(tabSurveyTag)){
                    Log.d("Tab", "Survey");
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(getString(R.string.attend_survey));
                    }
                }
                else if (str.equals(tabInfoTag)){
                    Log.d("Tab", "Info");
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(getString(R.string.wedding_info));
                    }
                }
                else if (str.equals(tabPhotoTag)){
                    Log.d("Tab", "Photo");
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(getString(R.string.wedding_photo));
                        //mTabHost.setVisibility( View.GONE );
                        //mTabHost.setVisibility( View.VISIBLE );
                    }
                }
            }
        });
        Bundle bundle = this.getIntent().getExtras();
        weddingInfoObjectId = bundle.getString("weddingInfoObjectId");
    }

    @Override
    public void onClick(View v){
        finish();
    }
    private String weddingInfoObjectId;
    public String getWeddingInfoObjectId(){
        return weddingInfoObjectId;
    }
}