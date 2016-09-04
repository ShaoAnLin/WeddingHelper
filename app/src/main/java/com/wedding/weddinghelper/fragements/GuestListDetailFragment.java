package com.wedding.weddinghelper.fragements;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wedding.weddinghelper.Adapter.GuestListAdapter;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.activities.GuestListActivity;
import com.wedding.weddinghelper.activities.JoinMainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class GuestListDetailFragment extends Fragment {
    List<ParseObject> newList = new ArrayList<ParseObject>();
    public  String weddingInfoObjectId;
    public GuestListAdapter guestListAdapter;

    public static GuestListDetailFragment newInstance() {
        return new GuestListDetailFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("Neal","activity class = "+activity.getLocalClassName());
        GuestListActivity mMainActivity = (GuestListActivity) activity;
        weddingInfoObjectId = mMainActivity.getWeddingInfoObjectId();
        Log.d("Neal", "guestListDetailFragment = "+weddingInfoObjectId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_guest_list_detail, container, false);
        ParseQuery query = new ParseQuery("AttendantList");
        query.whereEqualTo("weddingObjectId", weddingInfoObjectId);
        query.orderByAscending("AttendingWilling");
        query.addAscendingOrder("Session");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {
                ListView guestListView = (ListView) view.findViewById(R.id.guest_list_view);

                // add header items into the list
                newList = addHeader(list);

                guestListAdapter = new GuestListAdapter(getActivity(), newList);
                guestListView.setAdapter(guestListAdapter);
                guestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position != GuestListAdapter.headerAbsentPos && position != GuestListAdapter.headerEngagePos
                                && position != GuestListAdapter.headerMarryPos) {
                            ((GuestListActivity) getActivity()).listItemClicked(newList.get(position));
                        }
                    }
                });
            }
        });
        return view;
    }

    private List<ParseObject> addHeader(List<ParseObject> list){
        List<ParseObject> newList = new ArrayList<ParseObject>();
        boolean hasData [] = {false, false, false};
        int pos = 0;
        for (ParseObject obj : list){
            if (obj.getNumber("AttendingWilling").intValue() == 0){
                if (obj.getNumber("Session").intValue() == 1) {
                    // 結婚
                    if (!hasData[1]){
                        hasData[1] = true;
                        GuestListAdapter.headerMarryPos = pos;
                        newList.add(obj);
                        pos++;
                    }
                }
                else if (obj.getNumber("Session").intValue() == 0){
                    // 訂婚
                    if (!hasData[0]){
                        hasData[0] = true;
                        GuestListAdapter.headerEngagePos = pos;
                        newList.add(obj);
                        pos++;
                    }
                }
                else if (obj.getNumber("Session").intValue() == -1) {
                    // 參加
                    if (!hasData[0]){
                        hasData[0] = true;
                        GuestListAdapter.headerEngagePos = pos;
                        newList.add(obj);
                        pos++;
                    }
                }
            }
            else {
                // 不參加
                if (!hasData[2]){
                    hasData[2] = true;
                    GuestListAdapter.headerAbsentPos = pos;
                    newList.add(obj);
                    pos++;
                }
            }
            newList.add(obj);
            pos++;
        }
        return newList;
    }
}
