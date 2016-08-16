package com.wedding.weddinghelper.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wedding.weddinghelper.R;

public class OwnInfoFragment extends Fragment {
    public static OwnInfoFragment newInstance() {
        Log.d("Own info", "New Instance");
        return new OwnInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_own_info, container, false);
        Log.d("Own info", "create view");
        return view;
    }

    TextView groomAndBrideName, engageTime, engagePlace, engageAddress, marryTime, marryPlace, marryAddress;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        groomAndBrideName = (TextView) getView().findViewById(R.id.text_own_info);
        engageTime = (TextView) getView().findViewById(R.id.engage_time);
        engagePlace = (TextView) getView().findViewById(R.id.engage_place);
        engageAddress = (TextView) getView().findViewById(R.id.engage_address);
        marryTime = (TextView) getView().findViewById(R.id.marry_time);
        marryPlace = (TextView) getView().findViewById(R.id.marry_place);
        marryAddress = (TextView) getView().findViewById(R.id.marry_address);

        final ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        ParseQuery query = ParseQuery.getQuery("Information");
        query.getInBackground("XA6hDoxtXo",new GetCallback<ParseObject>() {//ToDo:暫時使用
            @Override
            public void done(ParseObject weddingInformation, ParseException e) {
                groomAndBrideName.setText(weddingInformation.getString("groomName")+"&"+weddingInformation.getString("brideName"));
                engageTime.setText(weddingInformation.getString("engageDate"));
                engagePlace.setText(weddingInformation.getString("engagePlace"));
                engageAddress.setText(weddingInformation.getString("engageAddress"));
                marryTime.setText(weddingInformation.getString("marryDate"));
                marryPlace.setText(weddingInformation.getString("marryPlace"));
                marryAddress.setText(weddingInformation.getString("marryAddress"));
            }
        });
    }
}
