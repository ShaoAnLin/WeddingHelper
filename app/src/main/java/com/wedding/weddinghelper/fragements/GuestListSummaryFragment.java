package com.wedding.weddinghelper.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wedding.weddinghelper.R;

import org.w3c.dom.Text;

import java.util.List;

public class GuestListSummaryFragment extends Fragment {

    public static GuestListSummaryFragment newInstance() {
        return new GuestListSummaryFragment();
    }

    public int attendMarryCount = 0, attendMarryFriendCount = 0, attendMarryMeatCount = 0, attendMarryVegetableCount = 0;
    public int attendEngageCount = 0, attendEngageFriendCount = 0, attendEngageMeatCount = 0, attendEngageVegetableCount = 0;

    public TextView totalForm, attendMarryNumber, attendMarryFriendNumber, totalMarryNumber, attendMarryMeat, attendMarryVegetable;
    public TextView attendEngageNumber, attendEngageFriendNumber, totalEngageNumber, attendEngageMeat, attendEngageVegetable, notAttendNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_list_summary, container, false);

        totalForm = (TextView)view.findViewById(R.id.total_Form);
        attendMarryNumber = (TextView)view.findViewById(R.id.attend_marry_number);
        attendMarryFriendNumber = (TextView)view.findViewById(R.id.attend_marry_friend_number);
        totalMarryNumber = (TextView)view.findViewById(R.id.total_marry_number);
        attendMarryMeat = (TextView)view.findViewById(R.id.attend_marry_meat);
        attendMarryVegetable = (TextView)view.findViewById(R.id.attend_marry_vegetable);
        attendEngageNumber = (TextView)view.findViewById(R.id.attend_engage_number);
        attendEngageFriendNumber = (TextView)view.findViewById(R.id.attend_engage_friend_number);
        totalEngageNumber = (TextView)view.findViewById(R.id.total_engage_number);
        attendEngageMeat = (TextView)view.findViewById(R.id.attend_engage_meat);
        attendEngageVegetable = (TextView)view.findViewById(R.id.attend_engage_vegetable);
        notAttendNumber = (TextView)view.findViewById(R.id.not_attend_number);
        // Parse data
        ParseQuery query = new ParseQuery("AttendantList");
        query.whereEqualTo("weddingObjectId", "XA6hDoxtXo");
        query.orderByAscending("AttendingWilling");
        query.orderByAscending("Session");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject formData : list) {
                    if (formData.getNumber("AttendingWilling").intValue() == 0 && formData.getNumber("Session").intValue() == 1) {
                        attendMarryCount++;
                        attendMarryFriendCount = attendMarryFriendCount + formData.getInt("PeopleNumber") - 1;
                        attendMarryMeatCount = attendMarryMeatCount + formData.getInt("MeatNumber");
                        attendMarryVegetableCount = attendMarryVegetableCount + formData.getInt("VagetableNumber");
                    }
                    else if (formData.getNumber("AttendingWilling").intValue() == 0 && formData.getNumber("Session").intValue() == 0 ) {
                        attendEngageCount++;
                        attendEngageFriendCount = attendEngageFriendCount + formData.getInt("PeopleNumber") - 1;
                        attendEngageMeatCount = attendEngageMeatCount + formData.getInt("MeatNumber");
                        attendEngageVegetableCount = attendEngageVegetableCount + formData.getInt("VagetableNumber");
                    }
                }
                totalForm.setText("總填單數："+list.size());
                attendMarryNumber.setText("出席結婚場次人數："+attendMarryCount);
                attendMarryFriendNumber.setText("攜友人數："+attendMarryFriendCount);
                totalMarryNumber.setText("總人數："+(attendMarryCount+attendMarryFriendCount));
                attendMarryMeat.setText("葷食人數："+attendMarryMeatCount);
                attendMarryVegetable.setText("素食人數："+attendMarryVegetableCount);

                attendEngageNumber.setText("出席訂婚場次人數："+attendEngageCount);
                attendEngageFriendNumber.setText("攜友人數："+attendEngageFriendCount);
                totalEngageNumber.setText("總人數："+(attendEngageCount+attendEngageFriendCount));
                attendEngageMeat.setText("葷食人數："+attendEngageMeatCount);
                attendEngageVegetable.setText("素食人數："+attendEngageVegetableCount);
                notAttendNumber.setText("無法出席人數："+(list.size()-attendMarryCount-attendEngageCount));
            }
        });

        return view;
    }
}
