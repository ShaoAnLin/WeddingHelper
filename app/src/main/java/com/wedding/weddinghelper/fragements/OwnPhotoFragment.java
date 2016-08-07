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

public class OwnPhotoFragment extends Fragment {
    public static OwnPhotoFragment newInstance() {
        Log.d("Own photo", "New Instance");
        return new OwnPhotoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_own_photo, container, false);
        Log.d("Own photo", "create view");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        TextView mText = (TextView) getView().findViewById(R.id.text_own_photo);
        mText.setText(R.string.wedding_photo);
    }
}
