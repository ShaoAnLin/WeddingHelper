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

public class JoinPhotoFragment extends Fragment {
    public static JoinPhotoFragment newInstance() {
        Log.d("Photo", "New Instance");
        JoinPhotoFragment fragment = new JoinPhotoFragment();
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
        View view = inflater.inflate(R.layout.fragment_join_photo, container, false);
        Log.d("Photo", "create view");
        return(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        TextView mText = (TextView) getView().findViewById(R.id.text_photo);
        mText.setText(R.string.wedding_photo);

        ImageView mImg = (ImageView) getActivity().findViewById(R.id.img_photo);
        mImg.setImageResource(R.drawable.ic_collections_24dp);
    }
}
