package com.wedding.weddinghelper.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.fragements.JoinInfoFragment;

public class ManageActivity extends AppCompatActivity
        implements View.OnClickListener, ListView.OnItemClickListener {

    final private String[] wedding_list = {"Test wedding 1","Test wedding 2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        // initiate action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.manage_action_bar);
        setSupportActionBar(actionBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_white);
            getSupportActionBar().setTitle(getString(R.string.wedding_manage));
            getSupportActionBar().setShowHideAnimationEnabled(true);
        }

        // set close icon on click listener
        if (actionBar != null) {
            actionBar.setNavigationOnClickListener(this);
        }

        ListView weddingList = (ListView)findViewById(R.id.wedding_list_view);
        ArrayAdapter listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, wedding_list);
        weddingList.setAdapter(listAdapter);
        weddingList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), "你選擇的是" + wedding_list[position], Toast.LENGTH_SHORT).show();
        Log.d("Manage", wedding_list[position]);
        startActivity(new Intent(this, OwnMainActivity.class));
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_button:
                Log.d(getClass().getSimpleName(), "Create wedding");
                startActivity(new Intent(this, CreateWeddingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
