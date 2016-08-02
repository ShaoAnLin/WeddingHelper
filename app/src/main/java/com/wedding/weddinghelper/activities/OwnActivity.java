package com.wedding.weddinghelper.activities;

import android.content.Intent;
import android.preference.Preference;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wedding.weddinghelper.R;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ParseInstallation;
import com.wedding.weddinghelper.fragements.CreateAccountFragment;

import org.w3c.dom.Text;


public class OwnActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own);

        // initiate action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.own_wedding_action_bar);
        setSupportActionBar(actionBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_white);
            getSupportActionBar().setTitle(getString(R.string.own_wedding));
            getSupportActionBar().setShowHideAnimationEnabled(true);
        }

        // set close icon on click listener
        if (actionBar != null) {
            actionBar.setNavigationOnClickListener(this);
        }

        // set preference fragment
        if (getSupportFragmentManager().findFragmentById(R.id.own_login_fragment) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.own_login_fragment, CreateAccountFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
