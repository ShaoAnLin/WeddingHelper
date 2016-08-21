package com.wedding.weddinghelper.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wedding.weddinghelper.R;

public class GuestDetailFragment extends Fragment {

    public static GuestDetailFragment newInstance() {
        return new GuestDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_detail, container, false);
        return view;
    }
}
