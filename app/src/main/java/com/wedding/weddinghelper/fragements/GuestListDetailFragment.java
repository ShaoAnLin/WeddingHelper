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
    public static GuestListDetailFragment newInstance() {
        return new GuestListDetailFragment();
    }

    public  String weddingInfoObjectId;
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
    public GuestListAdapter guestListAdapter;
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
                guestListAdapter = new GuestListAdapter(getActivity(), list);
                guestListView.setAdapter(guestListAdapter);
                Log.d("Neal","the List Size = "+list.size());
                guestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((GuestListActivity)getActivity()).listItemClicked(list.get(position));
                    }
                });
            }
        });
        return view;
    }
}
