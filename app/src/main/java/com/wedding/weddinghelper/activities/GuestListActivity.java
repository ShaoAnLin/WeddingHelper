package com.wedding.weddinghelper.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.fragements.GuestDetailFragment;
import com.wedding.weddinghelper.fragements.GuestListDetailFragment;
import com.wedding.weddinghelper.fragements.GuestListSummaryFragment;

import java.util.List;

public class GuestListActivity extends AppCompatActivity
        implements View.OnClickListener {

    public static final String PAGE_TYPE_KEY = "PageTypeKey";
    public static final int GUEST_LIST_SUMMARY_VAL = 0;
    public static final int GUEST_LIST_DETAIL_VAL = 1;
    public int attendMarryCount = 0, attendMarryFriendCount = 0, attendMarryMeatCount = 0, attendMarryVegetableCount = 0;
    public int attendEngageCount = 0, attendEngageFriendCount = 0, attendEngageMeatCount = 0, attendEngageVegetableCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_list);

        // initiate action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.guest_list_action_bar);
        setSupportActionBar(actionBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_white);
            getSupportActionBar().setTitle(getString(R.string.guest_list_summary));
            getSupportActionBar().setShowHideAnimationEnabled(true);
        }

        // set close icon on click listener
        if (actionBar != null) {
            actionBar.setNavigationOnClickListener(this);
        }

        // set fragment
        if (getSupportFragmentManager().findFragmentById(R.id.guest_fragment) == null) {
            int type = getIntent().getIntExtra(PAGE_TYPE_KEY, 0);

            switch (type) {
                case GUEST_LIST_SUMMARY_VAL:
                    getSupportActionBar().setTitle(getString(R.string.guest_list_summary));
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.guest_fragment, GuestListSummaryFragment.newInstance())
                            .commit();
                    break;
                case GUEST_LIST_DETAIL_VAL:
                    getSupportActionBar().setTitle(getString(R.string.guest_list_detail));
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.guest_fragment, GuestListDetailFragment.newInstance())
                            .commit();
                    break;
                default:
            }
        }

        // Parse data
        ParseQuery query = new ParseQuery("Information");
        query.whereEqualTo("objectId", "XA6hDoxtXo");
        query.orderByAscending("AttendingWilling");
        query.orderByAscending("Session");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject formData : list) {
                    if (formData.getInt("AttendWilling") == 0 && formData.getInt("Session") == 1) {
                        attendMarryCount++;
                        attendMarryFriendCount = attendMarryFriendCount + formData.getInt("PeopleNumber") - 1;
                        attendMarryMeatCount = attendMarryMeatCount + formData.getInt("MeatNumber");
                        attendMarryVegetableCount = attendMarryVegetableCount + formData.getInt("VagetableNumber");
                    } else if (formData.getInt("AttendWilling") == 0 && formData.getInt("Session") == 0) {
                        attendEngageCount++;
                        attendEngageFriendCount = attendEngageFriendCount + formData.getInt("PeopleNumber") - 1;
                        attendEngageMeatCount = attendEngageMeatCount + formData.getInt("MeatNumber");
                        attendEngageVegetableCount = attendEngageVegetableCount + formData.getInt("VagetableNumber");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Fragment guestFragment = getSupportFragmentManager().findFragmentById(R.id.guest_fragment);
        if (guestFragment instanceof GuestDetailFragment) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getString(R.string.guest_list_detail));
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.guest_fragment, GuestListDetailFragment.newInstance())
                    .commit();
        }
        else{
            finish();
        }
    }

    public void listItemClicked(){
        //startActivity(new Intent(this, GuestDetailActivity.class));
        getSupportActionBar().setTitle(getString(R.string.guest_detail));
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.guest_fragment, GuestDetailFragment.newInstance())
                .commit();
    }
}
