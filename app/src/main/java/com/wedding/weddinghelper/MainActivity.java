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
        Intent showIntent = new Intent(this, MainActivity.class);
        /*String pageType;
        switch(v.getId()) {
            case R.id.createWedding:
                Log.d("Home", "Create Wedding Page Clicked");
                pageType = getString(R.string.login);
                break;
            case R.id.joinWedding:
                Log.d("Home", "Join Wedding Page Clicked");
                pageType = getString(R.string.join);
                break;
            default:
                pageType = getString(R.string.login);
        }
        //showMainIntent.setClass(MainActivity.this, LoginActivity.class);
        //startActivityForResult(showMainIntent, 0);
        */

        //showIntent.putExtra(MainActivity.PAGE_TYPE_KEY, pageType);
        showIntent.putExtra(MainActivity.PAGE_TYPE_KEY, getString(R.string.login));
        startActivity(showIntent);
    }
}
