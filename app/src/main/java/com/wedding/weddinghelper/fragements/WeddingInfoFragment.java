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

import java.util.Calendar;

public class WeddingInfoFragment extends Fragment {
    public String weddingInfoObjectId;
    private TextView groomAndBrideName, engageTitle, marryTitle;
    private Button engageTime, engagePlace, engageAddress, marryTime, marryPlace, marryAddress;
    private String engageDate, engagePlaceUrl, engageAddressUrl, marryDate, marryPlaceUrl, marryAddressUrl;
    private String[] engageDateSplit, marryDateSplit;

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

                // set date and split the date with ':', '/' or ' '
                engageDate = weddingInformation.getString("engageDate");
                engageDateSplit = engageDate.split("[/: s]+");
                engageTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(engageDate)){
                            Calendar cal = Calendar.getInstance();
                            cal.set(Integer.valueOf(engageDateSplit[0]), Integer.valueOf(engageDateSplit[1]) - 1,
                                    Integer.valueOf(engageDateSplit[2]), Integer.valueOf(engageDateSplit[3]),
                                    Integer.valueOf(engageDateSplit[4]));
                            Intent intent = new Intent(Intent.ACTION_EDIT);
                            intent.setType("vnd.android.cursor.item/event");
                            intent.putExtra("beginTime", cal.getTimeInMillis());
                            intent.putExtra("endTime", cal.getTimeInMillis() + 3*60*60*1000);
                            intent.putExtra("title", groomAndBrideName.getText());
                            startActivity(intent);
                        }
                    }
                });

                // get the Url of restaurants
                engagePlaceUrl = weddingInformation.getString("engagePlaceIntroduce");
                engageAddressUrl = "http://maps.google.co.in/maps?q=" + weddingInformation.getString("engageAddress");

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


                if (!weddingInformation.getBoolean("onlyOneSession")) {
                    // set date and split the date with ':', '/' or ' '
                    marryDate = weddingInformation.getString("marryDate");
                    marryDateSplit = marryDate.split("[/: s]+");
                    marryTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!TextUtils.isEmpty(marryDate)){
                                Calendar cal = Calendar.getInstance();
                                cal.set(Integer.valueOf(marryDateSplit[0]), Integer.valueOf(marryDateSplit[1]) - 1,
                                        Integer.valueOf(marryDateSplit[2]), Integer.valueOf(marryDateSplit[3]),
                                        Integer.valueOf(marryDateSplit[4]));
                                Intent intent = new Intent(Intent.ACTION_EDIT);
                                intent.setType("vnd.android.cursor.item/event");
                                intent.putExtra("beginTime", cal.getTimeInMillis());
                                intent.putExtra("endTime", cal.getTimeInMillis() + 3*60*60*1000);
                                intent.putExtra("title", groomAndBrideName.getText());
                                startActivity(intent);
                            }
                        }
                    });

                    // get the Url of restaurants
                    marryPlaceUrl = weddingInformation.getString("marryPlaceIntroduce");
                    marryAddressUrl = "http://maps.google.co.in/maps?q=" + weddingInformation.getString("marryAddress");
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
            }
        });
    }
}
