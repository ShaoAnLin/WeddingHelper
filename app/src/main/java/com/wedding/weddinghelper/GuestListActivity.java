package com.wedding.weddinghelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Neal on 2016/8/20.
 */
public class GuestListActivity extends AppCompatActivity {
    public  int attendMarryCount = 0, attendMarryFriendCount = 0, attendMarryMeatCount = 0, attendMarryVegetableCount = 0;
    public  int attendEngageCount = 0, attendEngageFriendCount = 0, attendEngageMeatCount = 0, attendEngageVegetableCount = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_list);

        ParseQuery query = new ParseQuery("Information");
        query.whereEqualTo("objectId","XA6hDoxtXo");
        query.orderByAscending("AttendingWilling");
        query.orderByAscending("Session");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List <ParseObject>list, ParseException e) {
                for (ParseObject formData:list) {
                    if (formData.getInt("AttendWilling") == 0 && formData.getInt("Session") ==1){
                        attendMarryCount++;
                        attendMarryFriendCount = attendMarryFriendCount+formData.getInt("PeopleNumber") - 1;
                        attendMarryMeatCount = attendMarryMeatCount+formData.getInt("MeatNumber");
                        attendMarryVegetableCount = attendMarryVegetableCount+formData.getInt("VagetableNumber");
                    }
                    else if (formData.getInt("AttendWilling") == 0 && formData.getInt("Session") ==0){
                        attendEngageCount++;
                        attendEngageFriendCount = attendEngageFriendCount+formData.getInt("PeopleNumber") - 1;
                        attendEngageMeatCount = attendEngageMeatCount+formData.getInt("MeatNumber");
                        attendEngageVegetableCount = attendEngageVegetableCount+formData.getInt("VagetableNumber");
                    }
                }
            }
        });
    }

}
