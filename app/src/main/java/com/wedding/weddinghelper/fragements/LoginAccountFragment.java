package com.wedding.weddinghelper.fragements;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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

    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_account, container, false);
        Log.d("Login account", "create view");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setMessage("處理中...");
        progressDialog.setTitle(null);

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
        if (userNameEditText.getText().toString().length() == 0 || userPasswordEditText.getText().toString().length() == 0){
            boolean emptyUserName = false;
            if (TextUtils.isEmpty(userNameEditText.getText().toString())) {
                userNameEditText.setError(getString(R.string.error_field_required));
                userNameEditText.requestFocus();
                emptyUserName = true;
            }
            if (TextUtils.isEmpty(userPasswordEditText.getText().toString())) {
                userPasswordEditText.setError(getString(R.string.error_field_required));
                if (!emptyUserName) {
                    userPasswordEditText.requestFocus();
                }
            }
        }
        else {
            progressDialog.show();
            ParseUser.logInInBackground(userNameEditText.getText().toString(), userPasswordEditText.getText().toString(), new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        //Go to admin UI
                        progressDialog.dismiss();
                        Log.d("Neal", "Login success");
                        login();
                    }
                    else if (e != null) {
                        //Show a tip to tell user the failed reason.
                        Log.d("Neal", "Login failed with exception" + e + "    " + e.getCode());
                        progressDialog.dismiss();
                        if (e.getCode() == 100) { //未連上網路
                            new AlertDialog.Builder(getContext())
                                    .setTitle("訊息")
                                    .setMessage("請連上網路！")
                                    .setPositiveButton("好！", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        else if (e.getCode() == 101) { //帳號密碼錯誤
                            new AlertDialog.Builder(getContext())
                                    .setTitle("訊息")
                                    .setMessage("帳號或密碼輸入錯誤！")
                                    .setPositiveButton("好！", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        else{ //其餘錯誤
                            new AlertDialog.Builder(getContext())
                                    .setTitle("訊息")
                                    .setMessage("未知的錯誤發生，請稍候再試。")
                                    .setPositiveButton("好！", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                }
            });
        }
    }
}
