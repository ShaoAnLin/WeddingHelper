package com.wedding.weddinghelper.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.wedding.weddinghelper.JoinWeddingFragment;
import com.wedding.weddinghelper.R;

public class JoinActivity extends AppCompatActivity
    implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // initiate action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.join_wedding_action_bar);
        setSupportActionBar(actionBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_white);
            getSupportActionBar().setTitle(getString(R.string.join_wedding));
            getSupportActionBar().setShowHideAnimationEnabled(true);
        }

        // set close icon on click listener
        if (actionBar != null) {
            actionBar.setNavigationOnClickListener(this);
        }

        if (getSupportFragmentManager().findFragmentById(R.id.join_wedding_fragment) == null) {
            //Log.d("MeetingDetailActivity", "Event ID: " + eventId);
            int int1 = 1139;
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.join_wedding_fragment, JoinWeddingFragment.newInstance(int1))
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
