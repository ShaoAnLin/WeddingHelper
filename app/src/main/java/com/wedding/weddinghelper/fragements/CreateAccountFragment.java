package com.wedding.weddinghelper.fragements;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.activities.OwnActivity;

public class CreateAccountFragment extends Fragment {

    public static AdView mCreateAccountAdView;
    public static AdRequest mCreateAccountAdRquest;
    private Button mSignInButton;
    public boolean hi = false;

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

        //載入廣告
        mCreateAccountAdView = (AdView) view.findViewById(R.id.createAccoutnAdView);
        mCreateAccountAdRquest = new AdRequest.Builder().build();
        mCreateAccountAdView.loadAd(mCreateAccountAdRquest);

        mSignInButton = (Button) view.findViewById(R.id.create_account_signin_button);

        if (mSignInButton != null) {
            mSignInButton.setOnClickListener(new View.OnClickListener() {
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

        boolean emptyUserName = false, emptyPassword = false;
        if (TextUtils.isEmpty(userName.getText().toString()) || TextUtils.isEmpty(userPassword.getText().toString()) || TextUtils.isEmpty(userEmail.getText().toString())){
            // Check for a valid user name, if the user entered one.
            if (TextUtils.isEmpty(userName.getText().toString())) {
                userName.setError(getString(R.string.error_field_required));
                userName.requestFocus();
                emptyUserName = true;
            }
            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(userPassword.getText().toString())) {
                userPassword.setError(getString(R.string.error_field_required));
                if (!emptyUserName) {
                    userPassword.requestFocus();
                }
                emptyPassword = true;
            }
            // Check for a valid email, if the user entered one.
            if (TextUtils.isEmpty(userEmail.getText().toString())) {
                userEmail.setError(getString(R.string.error_field_required));
                if (!emptyPassword && !emptyUserName) {
                    userEmail.requestFocus();
                }
            }
        }
        else {
            ParseUser user = new ParseUser();
            user.setUsername(userName.getText().toString());
            user.setPassword(userPassword.getText().toString());
            user.setEmail(userEmail.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        ((OwnActivity) getActivity()).login();
                        Log.d("Neal", "Signing up success!!");
                    } else {
                        Log.d("Neal", "Signing up failed..." + e.getCode() + "     " + e);
                        if (e.getCode() == 202) {
                            Log.d("Neal", "使用者名稱已被使用");
                            new AlertDialog.Builder(getContext())
                                    .setTitle("訊息")
                                    .setMessage("此帳號已被使用。")
                                    .setPositiveButton("好！", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else if (e.getCode() == 203) {
                            Log.d("Neal", "E-mail已被使用");
                            new AlertDialog.Builder(getContext())
                                    .setTitle("訊息")
                                    .setMessage("此E-Mail已註冊過！")
                                    .setPositiveButton("好！", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else if (e.getCode() == 125) {
                            Log.d("Neal", "E-mail格式錯誤");
                            new AlertDialog.Builder(getContext())
                                    .setTitle("訊息")
                                    .setMessage("E-Mail格式錯誤！")
                                    .setPositiveButton("好！", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            Log.d("Neal", "不知名錯誤");
                            new AlertDialog.Builder(getContext())
                                    .setTitle("訊息")
                                    .setMessage("不知名的錯誤發生，請稍候再試。")
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
