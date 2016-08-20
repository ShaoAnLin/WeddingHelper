package com.wedding.weddinghelper.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.fragements.GuestListFragment;

public class GuestListActivity extends AppCompatActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_list);

        // initiate action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.guest_list_action_bar);
        setSupportActionBar(actionBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_white);
            getSupportActionBar().setTitle(getString(R.string.guest_list));
            getSupportActionBar().setShowHideAnimationEnabled(true);
        }

        // set close icon on click listener
        if (actionBar != null) {
            actionBar.setNavigationOnClickListener(this);
        }

        // set preference fragment
        if (getSupportFragmentManager().findFragmentById(R.id.guest_fragment) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.guest_fragment, GuestListFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
