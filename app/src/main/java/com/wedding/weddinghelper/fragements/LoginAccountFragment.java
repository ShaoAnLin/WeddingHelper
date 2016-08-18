package com.wedding.weddinghelper.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ParseInstallation;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.activities.OwnActivity;

public class LoginAccountFragment extends Fragment {

    private Button mLoginButton;

    public static LoginAccountFragment newInstance() {
        Log.d("Login account", "New Instance");
        LoginAccountFragment fragment = new LoginAccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_login_account, container, false);
        Log.d("Login account", "create view");

        mLoginButton = (Button) view.findViewById(R.id.login_account_login_button);
        if (mLoginButton != null) {
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Login account", "button clicked!");
                    attemptLogin(view);
                }
            });
        }

        Button testingLoginButton = (Button) view.findViewById(R.id.testing_login_account_login_button);
        if (testingLoginButton != null) {
            testingLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseUser.logInInBackground("neal", "neal", new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        login();
                    }
                });
                }
            });
        }

        return(view);
    }

    private void login(){
        ((OwnActivity)getActivity()).login();
    }

    private void attemptLogin(View v){
        EditText userNameEditText = (EditText) getActivity().findViewById(R.id.login_user_account);
        EditText userPasswordEditText = (EditText) getActivity().findViewById(R.id.login_user_password);
        ParseUser.logInInBackground(userNameEditText.getText().toString(), userPasswordEditText.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    //Go to admin UI
                    Log.d("Neal","Login success");
                    login();
                } else {
                    //Show a tip to tell user the failed reason.
                    Log.d("Neal","Login failed with exception"  + e);
                }
            }
        });
    }
}
