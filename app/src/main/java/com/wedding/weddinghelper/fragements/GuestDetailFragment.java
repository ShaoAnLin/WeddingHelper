package com.wedding.weddinghelper.fragements;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.activities.GuestListActivity;

import org.w3c.dom.Text;

public class GuestDetailFragment extends Fragment {

    public static GuestDetailFragment newInstance() {
        Log.d("Neal", "GuestDetailFragment~!!!");
        return new GuestDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Neal", "onCreate~!!!");
    }

    TextView name, friendOf,  phoneNumber, attend, attendPeople, foodPreference, address, message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_detail, container, false);
        Log.d("Neal", "onCreateView~!!!");
        name = (TextView)view.findViewById(R.id.name_textview);
        friendOf = (TextView)view.findViewById(R.id.friend_of_textview);
        phoneNumber = (TextView)view.findViewById(R.id.phone_number_textview);
        attend = (TextView)view.findViewById(R.id.attend_textview);
        attendPeople = (TextView)view.findViewById(R.id.attend_people_textview);
        foodPreference = (TextView)view.findViewById(R.id.food_preference_textview);
        address = (TextView)view.findViewById(R.id.address_textview);
        message = (TextView)view.findViewById(R.id.message_textview);


        GuestListActivity mMainActivity = (GuestListActivity) getActivity();
        ParseObject theGuestDetail = mMainActivity.getGuestDetail();

        name.setText(theGuestDetail.getString("Name"));
        phoneNumber.setText(theGuestDetail.getString("Phone"));
        if (theGuestDetail.getNumber("Relation").intValue() == 0){
            friendOf.setText("新郎的親朋好友");
        }
        else if (theGuestDetail.getNumber("Relation").intValue() == 1){
            friendOf.setText("新娘的親朋好友");
        }
        if (theGuestDetail.getNumber("AttendingWilling").intValue() == 0){
            if (theGuestDetail.getNumber("Session").intValue() == 0){
                attend.setText("參加訂婚宴");
            }
            else {
                attend.setText("參加結婚宴");
            }

            attendPeople.setText(theGuestDetail.getNumber("PeopleNumber").intValue()+"人出席");
            foodPreference.setText(theGuestDetail.getNumber("MeatNumber").intValue()+"人葷食，"+theGuestDetail.getNumber("VagetableNumber")+"人素食");
            address.setText(theGuestDetail.getString("AddressRegion")+theGuestDetail.getString("AddressDetail"));
        }
        else {
            attend.setText("不克參加");
            attendPeople.setVisibility(View.GONE);
            foodPreference.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
        }
        if (theGuestDetail.getString("Notation").length() == 0){
            message.setText("無備注");
        }
        else {
            message.setText(theGuestDetail.getString("Notation"));
        }
        return view;
    }



}
