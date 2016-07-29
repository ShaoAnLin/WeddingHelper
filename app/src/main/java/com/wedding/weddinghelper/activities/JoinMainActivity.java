package com.wedding.weddinghelper.activities;

import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.wedding.weddinghelper.fragements.JoinInfoFragment;

import com.wedding.weddinghelper.R;

public class JoinMainActivity extends AppCompatActivity
        implements OnMenuTabClickListener, View.OnClickListener {
    public static final String PAGE_TYPE_KEY = "whrPageTypeKey";

    private BottomBar mBottomBar;
    public enum PageType {
        INFO(0), PHOTO(1), SETTING(2);

        public final int position;
        private PageType(int position) {
            this.position = position;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_main);

        // initiate action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.join_info_action_bar);
        setSupportActionBar(actionBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_white);
            getSupportActionBar().setTitle(getString(R.string.join_info));
            getSupportActionBar().setShowHideAnimationEnabled(true);
        }

        // set close icon on click listener
        if (actionBar != null) {
            actionBar.setNavigationOnClickListener(this);
        }

        // initiate bottom bar
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.useOnlyStatusBarTopOffset();
        mBottomBar.useFixedMode();
        mBottomBar.useDarkTheme();
        mBottomBar.setActiveTabColor(ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.setItemsFromMenu(R.menu.bottom_bar_menu, this);

        // set tab's background color
        mBottomBar.mapColorForTab(PageType.INFO.position, ContextCompat.getColor(this, R.color.colorPrimary));
        mBottomBar.mapColorForTab(PageType.PHOTO.position, ContextCompat.getColor(this, R.color.colorPrimary));
        mBottomBar.mapColorForTab(PageType.SETTING.position, ContextCompat.getColor(this, R.color.colorPrimary));

        // setup the default tab
        // Note: the default position has to be set before setting background colors
        //       for some reason, otherwise the color won't be changed in the first time
        if (getSupportFragmentManager().findFragmentById(R.id.join_main_fragment) == null) {
            int position = PageType.INFO.position;
            if (getIntent() != null && getIntent().getExtras() != null &&
                    getIntent().getExtras().getString(PAGE_TYPE_KEY) != null) {
                PageType type = null;
                try {
                    type = PageType.valueOf(getIntent().getExtras().getString(PAGE_TYPE_KEY));
                } catch (Exception e) {}

                switch (type) {
                    case INFO:
                    case PHOTO:
                    case SETTING:
                        position = type.position;
                        break;
                    default:
                        position = PageType.INFO.position;
                }
            }
            mBottomBar.setDefaultTabPosition(position);
        }
    }

    @Override
    public void onMenuTabReSelected(int menuItemId) {
        // do nothing at the moment
    }

    @Override
    public void onMenuTabSelected(@IdRes int menuItemId) {
        /*switch (menuItemId) {
            case R.id.bottomBarInFo:
                replaceCurrentFragment(JoinInfoFragment.newInstance()
                        .setModel(introModelFragment.getModel()));
                setActionBarTitle(R.string.intro_page_title);
                break;
            default:
        }*/
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
