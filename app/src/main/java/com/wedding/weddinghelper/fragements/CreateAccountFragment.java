package com.wedding.weddinghelper.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.wedding.weddinghelper.R;

public class CreateAccountFragment extends Fragment {
    public static CreateAccountFragment newInstance() {
        Log.d("Create account", "New Instance");
        CreateAccountFragment fragment = new CreateAccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        Log.d("Create account", "create view");
        Button createAccountButton = (Button) view.findViewById(R.id.create_account_login_button);

        if (createAccountButton != null) {
            createAccountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptCreateAccount(view);
                }
            });
        }
        return(view);
    }

    private void attemptCreateAccount(View v){
        EditText userName = (EditText) getActivity().findViewById(R.id.create_user_account);
        EditText userPassword = (EditText) getActivity().findViewById(R.id.create_user_password);
        EditText userEmail = (EditText) getActivity().findViewById(R.id.create_user_email);
        ParseUser user = new ParseUser();
        user.setUsername(userName.getText().toString());
        user.setPassword(userPassword.getText().toString());
        user.setEmail(userEmail.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("Neal","Signing up success!!");
                }
                else {
                    Log.d("Neal","Signing up failed..."+e.getCode()+ "     " +e);
                    if (e.getCode() == 202){
                        Log.d("Neal","使用者名稱已被使用");
                    }
                    else if (e.getCode() == 203){
                        Log.d("Neal","E-mail已被使用");
                    }
                    else if (e.getCode() == 125){
                        Log.d("Neal","E-mail格式錯誤");
                    }
                    else {
                        Log.d("Neal","不知名錯誤");
                    }
                }
            }
        });
    }
}
