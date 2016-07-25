package com.wedding.weddinghelper.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.wedding.weddinghelper.JoinWeddingFragment;
import com.wedding.weddinghelper.R;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        if (getSupportFragmentManager().findFragmentById(R.id.join_wedding_fragment) == null) {
            //Log.d("MeetingDetailActivity", "Event ID: " + eventId);
            int int1 = 1139;
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.join_wedding_fragment, JoinWeddingFragment.newInstance(int1))
                    .commit();
        }
    }
}
