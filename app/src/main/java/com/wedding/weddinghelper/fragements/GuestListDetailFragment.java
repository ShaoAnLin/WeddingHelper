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
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.activities.GuestListActivity;
import com.wedding.weddinghelper.activities.JoinMainActivity;

import java.util.List;

public class GuestListDetailFragment extends Fragment {

    private String[] guestList = {"Alex", "Bob", "Cindy"};

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_guest_list_detail, container, false);

        ParseQuery query = new ParseQuery("AttendantList");
        query.whereEqualTo("weddingObjectId", weddingInfoObjectId);
        query.orderByAscending("AttendingWilling");
        query.orderByAscending("Session");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                String [] guestNameList = new String[list.size()];
                for (int i = 0 ; i<list.size() ; i++) {
                    //ToDo:將下載的賓客資料塞進list view
                    ParseObject formData = list.get(i);
                    guestNameList[i] = formData.getString("Name");
                }
                ListView guestListView = (ListView) view.findViewById(R.id.guest_list_view);
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1,guestNameList);
                guestListView.setAdapter(adapter);
                guestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getActivity().getApplicationContext(), "你選擇的是" + guestList[position], Toast.LENGTH_SHORT).show();
                        ((GuestListActivity)getActivity()).listItemClicked();
                    }
                });
            }
        });





        return view;
    }
}
