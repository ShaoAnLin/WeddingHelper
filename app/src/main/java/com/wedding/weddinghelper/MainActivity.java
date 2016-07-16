package com.wedding.weddinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PAGE_TYPE_KEY = "PageTypeKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ImageView coupleImage = (ImageView) findViewById(R.id.couple);
        ImageButton createWeddingBtn = (ImageButton) findViewById(R.id.createWedding);
        ImageButton joinWeddingBtn = (ImageButton) findViewById(R.id.joinWedding);

        if (createWeddingBtn != null) {
            createWeddingBtn.setOnClickListener(this);
        }
        if (joinWeddingBtn != null) {
            joinWeddingBtn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent showIntent;
        switch(v.getId()) {
            case R.id.createWedding:
                Log.d(getClass().getSimpleName(), "Create Wedding Page Clicked");
                showIntent = new Intent(this, LoginActivity.class);
                break;
            case R.id.joinWedding:
                Log.d(getClass().getSimpleName(), "Join Wedding Page Clicked");
                showIntent = new Intent(this, JoinActivity.class);
                break;
            default:
                showIntent = new Intent(this, LoginActivity.class);
        }
        //showIntent.putExtra(MainActivity.PAGE_TYPE_KEY, pageType);
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
        super.onDestroy(); }
}
