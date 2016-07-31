package com.wedding.weddinghelper.activities;

import android.content.Intent;
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

        Button loginButton = (Button) findViewById(R.id.own_wedding_login_button);
        if (loginButton != null) {
            loginButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("Neal","v.getId() = "+v.getId());
        switch (v.getId()){
            case R.id.own_wedding_login_button:
                EditText userNameEditText = (EditText) findViewById(R.id.user_name);
                final EditText userPasswordEditText = (EditText) findViewById(R.id.user_password);
                ParseUser.logInInBackground(userNameEditText.getText().toString(), userPasswordEditText.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            //Go to admin UI
                            Log.d("Neal","Login success");
                        } else {
                            //Show a tip to tell user the failed reason.
                            Log.d("Neal","Login failed with exception"  + e);
                        }
                    }
                });
                break;

            //WRONG WAY
            case R.id.own_wedding_action_bar:
                finish();
                break;
        }

    }

}
