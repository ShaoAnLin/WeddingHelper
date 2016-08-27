package com.wedding.weddinghelper.fragements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        Button mGuestListSummaryButton = (Button) view.findViewById(R.id.guest_list_summary_button);
        if (mGuestListSummaryButton != null) {
            mGuestListSummaryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OwnMainActivity)getActivity()).guestListSummaryButtonClicked();
                }
            });
        }

        Button mGuestDetailListButton = (Button) view.findViewById(R.id.guest_detail_list_button);
        if (mGuestDetailListButton != null) {
            mGuestDetailListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OwnMainActivity)getActivity()).guestDetailListButtonClicked();
                }
            });
        }

        Button mEditWeddingInfoButton = (Button) view.findViewById(R.id.edit_wedding_info_button);
        if (mEditWeddingInfoButton != null) {
            mEditWeddingInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Shawn", "Edit wedding info button clicked");
                    ((OwnMainActivity)getActivity()).editWeddingButtonClicked(true);
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
