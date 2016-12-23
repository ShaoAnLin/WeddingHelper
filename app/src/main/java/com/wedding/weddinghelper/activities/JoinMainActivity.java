package com.wedding.weddinghelper.activities;

import android.graphics.Rect;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TabHost;

import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.fragements.PhotoFragment;
import com.wedding.weddinghelper.fragements.JoinSurveyFragment;
import com.wedding.weddinghelper.fragements.WeddingInfoFragment;


public class JoinMainActivity extends AppCompatActivity
        implements View.OnClickListener {

    final private String tabSurveyTag = "tabSurveyTag";
    final private String tabInfoTag = "tabInfoTag";
    final private String tabPhotoTag = "tabPhotoTag";

    private FragmentTabHost mTabHost;
    private String weddingInfoObjectId;

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
        tabSetup();

        Bundle bundle = this.getIntent().getExtras();
        weddingInfoObjectId = bundle.getString("weddingInfoObjectId");

        // check if keyboard is hidden or shown
        final View activityRootView = findViewById(R.id.container);
        activityRootView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        activityRootView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = activityRootView.getRootView().getHeight();

                        int keypadHeight = screenHeight - r.bottom;
                        if (keypadHeight > screenHeight * 0.15) {
                            // keyboard is opened
                            mTabHost.getTabWidget().getChildAt(0).setVisibility(View.GONE);
                            mTabHost.getTabWidget().getChildAt(1).setVisibility(View.GONE);
                            mTabHost.getTabWidget().getChildAt(2).setVisibility(View.GONE);
                        } else {
                            // keyboard is closed
                            mTabHost.getTabWidget().getChildAt(0).setVisibility(View.VISIBLE);
                            mTabHost.getTabWidget().getChildAt(1).setVisibility(View.VISIBLE);
                            mTabHost.getTabWidget().getChildAt(2).setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    public void tabSetup() {
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
                if (str.equals(tabSurveyTag)) {
                    Log.d("Tab", "Survey");
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(getString(R.string.attend_survey));
                    }
                } else if (str.equals(tabInfoTag)) {
                    Log.d("Tab", "Info");
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(getString(R.string.wedding_info));
                    }
                } else if (str.equals(tabPhotoTag)) {
                    Log.d("Tab", "Photo");
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(getString(R.string.wedding_photo));
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    public String getWeddingInfoObjectId() {
        return weddingInfoObjectId;
    }
}