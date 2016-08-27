package com.wedding.weddinghelper.fragements;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.activities.JoinMainActivity;
import com.wedding.weddinghelper.activities.OwnMainActivity;

public class WeddingInfoFragment extends Fragment {
    public String weddingInfoObjectId;
    private TextView groomAndBrideName, engageTitle, marryTitle;
    private Button engageTime, engagePlace, engageAddress, marryTime, marryPlace, marryAddress;
    String engagePlaceUrl, engageAddressUrl, marryPlaceUrl, marryAddressUrl;

    public static WeddingInfoFragment newInstance() {
        Log.d("Own info", "New Instance");
        return new WeddingInfoFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("Neal","activity class = "+activity.getLocalClassName());
        if (activity.getLocalClassName().equals("activities.OwnMainActivity")){
            OwnMainActivity mMainActivity = (OwnMainActivity) activity;
            weddingInfoObjectId = mMainActivity.getWeddingInfoObjectId();
        }
        else {
            JoinMainActivity mMainActivity = (JoinMainActivity) activity;
            weddingInfoObjectId = mMainActivity.getWeddingInfoObjectId();
        }
        Log.d("Neal", weddingInfoObjectId);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wedding_info, container, false);
        Log.d("Own info", "create view");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        engageTitle = (TextView)view.findViewById(R.id.engage_label);
        marryTitle = (TextView)view.findViewById(R.id.marry_label);
        groomAndBrideName = (TextView) view.findViewById(R.id.text_own_info);
        engageTime = (Button) view.findViewById(R.id.engage_time);
        engagePlace = (Button) view.findViewById(R.id.engage_place);
        engageAddress = (Button) view.findViewById(R.id.engage_address);
        marryTime = (Button) view.findViewById(R.id.marry_time);
        marryPlace = (Button) view.findViewById(R.id.marry_place);
        marryAddress = (Button) view.findViewById(R.id.marry_address);

        final ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        ParseQuery query = ParseQuery.getQuery("Information");
        query.getInBackground(weddingInfoObjectId,new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject weddingInformation, ParseException e) {
                groomAndBrideName.setText(weddingInformation.getString("groomName")+"&"+weddingInformation.getString("brideName"));
                engageTime.setText(weddingInformation.getString("engageDate"));
                engagePlace.setText(weddingInformation.getString("engagePlace"));
                engageAddress.setText(weddingInformation.getString("engageAddress"));

                if (!weddingInformation.getBoolean("onlyOneSession")){
                    marryTime.setVisibility(View.VISIBLE);
                    marryPlace.setVisibility(View.VISIBLE);
                    marryAddress.setVisibility(View.VISIBLE);
                    marryTime.setText(weddingInformation.getString("marryDate"));
                    marryPlace.setText(weddingInformation.getString("marryPlace"));
                    marryAddress.setText(weddingInformation.getString("marryAddress"));
                    engageTitle.setText(getString(R.string.engage_string));
                }
                else {
                    engageTitle.setText(getString(R.string.wedding));
                    marryTitle.setVisibility(View.GONE);
                    marryTime.setVisibility(View.GONE);
                    marryPlace.setVisibility(View.GONE);
                    marryAddress.setVisibility(View.GONE);
                }



                // get the Url of restaurants
                engagePlaceUrl = weddingInformation.getString("engagePlaceIntroduce");
                engageAddressUrl = "http://maps.google.co.in/maps?q=" + weddingInformation.getString("engageAddress");
                marryPlaceUrl = weddingInformation.getString("marryPlaceIntroduce");
                marryAddressUrl = "http://maps.google.co.in/maps?q=" + weddingInformation.getString("marryAddress");

                engagePlace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(engagePlaceUrl)){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(engagePlaceUrl));
                            startActivity(browserIntent);
                        }
                    }
                });
                engageAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(engageAddressUrl));
                        startActivity(mapIntent);
                    }
                });
                marryPlace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(marryPlaceUrl)) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(marryPlaceUrl));
                            startActivity(browserIntent);
                        }
                    }
                });
                marryAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(marryAddressUrl));
                        startActivity(mapIntent);
                    }
                });
            }
        });
    }
}
