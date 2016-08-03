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
                    attemptLogin(view);
                }
            });
        }

        return(view);
    }

    private void attemptLogin(View v){
        EditText userNameEditText = (EditText) v.findViewById(R.id.login_user_account);
        final EditText userPasswordEditText = (EditText) v.findViewById(R.id.login_user_password);
        ParseUser.logInInBackground(userNameEditText.getText().toString(), userPasswordEditText.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    //Go to admin UI
                    Log.d("Neal","Login success");
                } else {
                    //Show a tip to tell user the failed reason.
                    Log.d("Neal","Login failed with exception"  + e);
                }
            }
        });
    }
}
