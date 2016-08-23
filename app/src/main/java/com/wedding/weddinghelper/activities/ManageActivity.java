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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.wedding.weddinghelper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageActivity extends AppCompatActivity
        implements View.OnClickListener, ListView.OnItemClickListener {

    List<HashMap<String , String>> list = new ArrayList<>(); //儲存每場婚宴的婚宴名稱及結婚日期。
    String[] wedding_name;
    String[] wedding_date;
    ListView weddingListView;
    List <ParseObject> weddingInformation;

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
        weddingListView = (ListView)findViewById(R.id.wedding_list_view);
        ParseQuery query = ParseQuery.getQuery("Information");
        query.whereEqualTo("managerAccount",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List <ParseObject>weddingList, ParseException e) {
                Log.d("Neal", "Weddinglist count = " + weddingList.size() + "current user = "+ParseUser.getCurrentUser().getUsername());
                weddingInformation = weddingList;
                wedding_name = new String[weddingList.size()];
                wedding_date = new String[weddingList.size()];
                for (int i = 0 ; i< weddingList.size() ; i++){
                    ParseObject theWeddingInformation = weddingList.get(i);
                    wedding_name[i] = theWeddingInformation.getString("weddingAccount");
                    if (theWeddingInformation.getBoolean("onlyOneSession")){
                        wedding_date[i] = theWeddingInformation.getString("engageDate");
                    }
                    else {
                        wedding_date[i] = theWeddingInformation.getString("marryDate");
                    }

                }


                for(int i = 0 ; i < wedding_name.length ; i++){
                    HashMap<String , String> hashMap = new HashMap<>();
                    hashMap.put("wedding_name" , wedding_name[i]);
                    hashMap.put("wedding_date" , wedding_date[i]);
                    list.add(hashMap);
                }
                ListAdapter listAdapter = new SimpleAdapter(getApplicationContext(),list,R.layout.custom_list_view_style, new String[] {"wedding_name","wedding_date"}, new int[]{android.R.id.text2, android.R.id.text1});
                weddingListView.setAdapter(listAdapter);

            }
        });
        weddingListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), "你選擇的是" + wedding_list[position], Toast.LENGTH_SHORT).show();
        Log.d("Manage", wedding_name[position]);
        Intent intent = new Intent();
        intent.setClass(this, OwnMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("weddingInfoObjectId", weddingInformation.get(position).getObjectId());

        intent.putExtras(bundle);
        startActivity(intent);
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
