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

public class JoinSurveyFragment extends Fragment {
    public static JoinSurveyFragment newInstance() {
        Log.d("Survey", "New Instance");
        JoinSurveyFragment fragment = new JoinSurveyFragment();
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
        View view = inflater.inflate(R.layout.fragment_join_survey, container, false);
        Log.d("Setting", "create view");
        return(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        TextView mText = (TextView) getView().findViewById(R.id.text_survey);
        mText.setText(R.string.attend_survey);

        ImageView mImg = (ImageView) getActivity().findViewById(R.id.img_survey);
        mImg.setImageResource(R.drawable.ic_add_box_black_24dp);
    }
}
