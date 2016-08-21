package com.wedding.weddinghelper.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.activities.GuestListActivity;

public class GuestListDetailFragment extends Fragment {

    private String[] guestList = {"Alex", "Bob", "Cindy"};

    public static GuestListDetailFragment newInstance() {
        return new GuestListDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_list_detail, container, false);

        ListView guestListView = (ListView) view.findViewById(R.id.guest_list_view);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1,guestList);
        guestListView.setAdapter(adapter);
        guestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity().getApplicationContext(), "你選擇的是" + guestList[position], Toast.LENGTH_SHORT).show();
                ((GuestListActivity)getActivity()).listItemClicked();
            }
        });

        return view;
    }
}
