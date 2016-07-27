package com.wedding.weddinghelper.fragements;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wedding.weddinghelper.R;

public class JoinWeddingInfoFragment extends Fragment {
    public static JoinWeddingInfoFragment newInstance() {
        Log.d("Join wedding fragment", "New Instance");
        JoinWeddingInfoFragment fragment = new JoinWeddingInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_wedding_info, container, false);
        return view;
    }
}
