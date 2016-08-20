package com.wedding.weddinghelper.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.activities.OwnMainActivity;

public class OwnSettingFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_own_setting, container, false);

        Button mGuestListButton = (Button) view.findViewById(R.id.guest_list_button);
        if (mGuestListButton != null) {
            mGuestListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Shawn", "Guest list button clicked");
                    ((OwnMainActivity)getActivity()).guestListButtonClicked();
                }
            });
        }

        Button mEditWeddingInfoButton = (Button) view.findViewById(R.id.edit_wedding_info_button);
        if (mEditWeddingInfoButton != null) {
            mEditWeddingInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Shawn", "Edit wedding info button clicked");
                    ((OwnMainActivity)getActivity()).editWeddingButtonClicked();
                }
            });
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }
}
