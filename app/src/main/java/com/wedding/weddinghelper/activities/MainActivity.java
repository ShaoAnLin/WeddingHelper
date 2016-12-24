package com.wedding.weddinghelper.activities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.Util.PhotoUtils;
import com.wedding.weddinghelper.fragements.PhotoFragment;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this,"TlUPVfv4VFf6O5sWppgvE1Koo80oqvhvBB0FePUC", "jObLwhnPEsWGRWkjAhwDVcPv1RUcznytT2X83iet");

        final ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        PackageInfo pInfo;
        String version;
        try{
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        }
        catch(PackageManager.NameNotFoundException ex){
            version = "";
        }
        currentInstallation.put("realAppVersion",version);
        currentInstallation.saveInBackground();

        // initiate action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.main_action_bar);
        setSupportActionBar(actionBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }

        ImageButton ownWeddingBtn = (ImageButton) findViewById(R.id.ownWedding);
        ImageButton joinWeddingBtn = (ImageButton) findViewById(R.id.joinWedding);

        if (ownWeddingBtn != null) {
            ownWeddingBtn.setOnClickListener(this);
        }
        if (joinWeddingBtn != null) {
            joinWeddingBtn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent showIntent;
        switch(v.getId()) {
            case R.id.ownWedding:
                Log.d(getClass().getSimpleName(), "Own Wedding Page Clicked");
                showIntent = new Intent(this, OwnActivity.class);
                break;
            case R.id.joinWedding:
                Log.d(getClass().getSimpleName(), "Join Wedding Page Clicked");
                showIntent = new Intent(this, JoinActivity.class);
                break;
            default:
                showIntent = new Intent(this, OwnActivity.class);
        }
        startActivity(showIntent);
    }

    @Override
    public void onRestart() { super.onRestart();
        Log.d(getClass().getSimpleName(), "onRestart()");
    }
    @Override
    public void onStart() { super.onStart();
        Log.d(getClass().getSimpleName(), "onStart()");
    }
    @Override
    public void onResume() { super.onResume();
        Log.d(getClass().getSimpleName(), "onResume()");
    }
    @Override
    public void onPause() { Log.d(getClass().getSimpleName(), "onPause()");
        super.onPause(); }
    @Override
    public void onStop() {
        Log.d(getClass().getSimpleName(), "onStop()");
        super.onStop(); }
    @Override
    public void onDestroy() { Log.d(getClass().getSimpleName(), "onDestroy()");
        super.onDestroy();
    }
}
