package com.wedding.weddinghelper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class JoinWeddingFragment extends Fragment {
    private static final String KEY_PARAM="param";

    public static JoinWeddingFragment newInstance(int param) {
        Log.d("Join wedding fragment", Integer.toString(param));
        JoinWeddingFragment fragment = new JoinWeddingFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_wedding, container, false);
        return(view);
    }
}
