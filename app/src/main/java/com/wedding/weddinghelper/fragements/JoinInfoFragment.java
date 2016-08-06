package com.wedding.weddinghelper.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wedding.weddinghelper.R;

public class JoinInfoFragment extends Fragment {
    public static JoinInfoFragment newInstance() {
        Log.d("Info", "New Instance");
        return new JoinInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_info, container, false);
        Log.d("Info", "create view");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        TextView mText = (TextView) getView().findViewById(R.id.text_info);
        mText.setText(R.string.wedding_info);
        Log.d("Info", mText.getText().toString());

        ImageView mImg = (ImageView) getActivity().findViewById(R.id.img_info);
        mImg.setImageResource(R.drawable.ic_place_24dp);
    }
}
