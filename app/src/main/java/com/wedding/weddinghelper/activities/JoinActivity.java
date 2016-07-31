package com.wedding.weddinghelper.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wedding.weddinghelper.R;

public class JoinActivity extends AppCompatActivity
    implements View.OnClickListener {

    final public String NAME = "test";
    final public String PASSWORD = "123";

    private EditText mNameView;
    private EditText mPasswordView;

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

        mNameView = (EditText) findViewById(R.id.join_wedding_name);
        mPasswordView = (EditText) findViewById(R.id.join_wedding_password);

        Button mSignInButton = (Button) findViewById(R.id.join_wedding_sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {

        String name = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if ( !name.equals(NAME) ) {
            mPasswordView.setError(getString(R.string.error_incorrect_name));
            focusView = mNameView;
            cancel = true;
        }
        else {
            if ( !password.equals(PASSWORD) ) {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                focusView = mPasswordView;
                cancel = true;
            }
        }

        if ( cancel ) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Log.d("Login", "Success!");
            Intent showMainIntent = new Intent(this, JoinMainActivity.class);
            showMainIntent.putExtra(JoinMainActivity.PAGE_TYPE_KEY, JoinMainActivity.PageType.INFO.name());
            startActivity(showMainIntent);
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
