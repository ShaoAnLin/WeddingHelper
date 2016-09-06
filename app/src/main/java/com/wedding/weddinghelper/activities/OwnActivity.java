package com.wedding.weddinghelper.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.Util.CacheManager;
import com.wedding.weddinghelper.fragements.CreateAccountFragment;
import com.wedding.weddinghelper.fragements.LoginAccountFragment;


public class OwnActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener {

    final private String createAccountFragmentTag = "createAccountFragment";
    final private String loginAccountFragmentTag = "loginAccountFragment";

    private Switch mHaveAccountSwitch;

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
            getSupportActionBar().setTitle(getString(R.string.create_account));
            getSupportActionBar().setShowHideAnimationEnabled(true);
        }

        // set close icon on click listener
        if (actionBar != null) {
            actionBar.setNavigationOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    finish();
                }
            });
        }

        // get switch
        mHaveAccountSwitch = (Switch) findViewById(R.id.have_account_switch);
        if (mHaveAccountSwitch != null) {
            mHaveAccountSwitch.setChecked(false);
            mHaveAccountSwitch.setText(getString(R.string.no_account));
            mHaveAccountSwitch.setOnCheckedChangeListener(this);
        }

        // set preference fragment
        if (getSupportFragmentManager().findFragmentById(R.id.own_login_fragment) == null) {
            String haveAccountCache = CacheManager.readString(getApplicationContext(), CacheManager.HAVE_ACCOUNT_KEY);
            if (haveAccountCache != null){
                if (haveAccountCache.equals("true")) {
                    mHaveAccountSwitch.setChecked(true);
                    replaceLoginFormFragment(LoginAccountFragment.newInstance(), loginAccountFragmentTag);
                }
                else{
                    mHaveAccountSwitch.setChecked(false);
                    replaceLoginFormFragment(CreateAccountFragment.newInstance(), createAccountFragmentTag);
                }
            }
            else{
                mHaveAccountSwitch.setChecked(false);
                replaceLoginFormFragment(CreateAccountFragment.newInstance(), createAccountFragmentTag);
            }
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            mHaveAccountSwitch.setText(getString(R.string.have_account));
            getSupportActionBar().setTitle(getString(R.string.login_account));
        }
        else{
            mHaveAccountSwitch.setText(getString(R.string.no_account));
            getSupportActionBar().setTitle(getString(R.string.create_account));
        }

        CacheManager.writeString(getApplicationContext(), CacheManager.HAVE_ACCOUNT_KEY, Boolean.toString(isChecked));

        Fragment loginFormFragment = getSupportFragmentManager().findFragmentById(R.id.own_login_fragment);
        if (loginFormFragment instanceof CreateAccountFragment && isChecked) {
            Log.d("Create -> Login", "!!!");

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getString(R.string.login_account));
            }

            this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.own_login_fragment, LoginAccountFragment.newInstance(), loginAccountFragmentTag)
                    .commit();
        }
        else if (loginFormFragment instanceof LoginAccountFragment && !isChecked){
            Log.d("Login -> Create", "!!!");

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getString(R.string.create_account));
            }

            this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.own_login_fragment, CreateAccountFragment.newInstance(), createAccountFragmentTag)
                    .commit();
        }
        else{
            Log.d("Switch", "Error!");
        }
    }

    private void replaceLoginFormFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.own_login_fragment, fragment, tag)
                .commit();
    }

    public void login(){
        Log.d(getClass().getSimpleName(), "login");
        startActivity(new Intent(this, ManageActivity.class));
    }
}
