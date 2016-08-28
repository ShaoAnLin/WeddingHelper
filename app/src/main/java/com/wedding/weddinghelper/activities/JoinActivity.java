package com.wedding.weddinghelper.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wedding.weddinghelper.R;

public class JoinActivity extends AppCompatActivity
    implements View.OnClickListener {

    final public String NAME = "test";
    final public String PASSWORD = "123";

    private EditText mNameView;
    private EditText mPasswordView;

    ProgressDialog progressDialog;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // initiate action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.join_wedding_action_bar);
        setSupportActionBar(actionBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_white);
            getSupportActionBar().setTitle(getString(R.string.join_wedding));
            getSupportActionBar().setShowHideAnimationEnabled(true);
        }

        // set close icon on click listener
        if (actionBar != null) {
            actionBar.setNavigationOnClickListener(this);
        }

        mNameView = (EditText) findViewById(R.id.join_wedding_name);
        mPasswordView = (EditText) findViewById(R.id.join_wedding_password);

        Button mSignInButton = (Button) findViewById(R.id.join_wedding_sign_in_button);
        if (mSignInButton != null) {
            mSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
        }
        Button testingSignInButton = (Button) findViewById(R.id.testing_join_wedding_sign_in_button);
        if (testingSignInButton != null) {
            testingSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseQuery query = ParseQuery.getQuery("Information");
                    query.whereEqualTo("weddingAccount", "Neal&Pallas");
                    query.whereEqualTo("weddingPassword", "nealpallas");
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject information, ParseException e) {
                            login(information.getObjectId());
                        }
                    });
                }
            });
        }


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setMessage("處理中...");
        progressDialog.setTitle(null);
    }


    private void login(String weddingInfoObjectId){
        Intent intent = new Intent();
        intent.setClass(this, JoinMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("weddingInfoObjectId", weddingInfoObjectId);
        intent.putExtras(bundle);

        startActivity(intent);

    }

    private void attemptLogin() {
        String name = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean emptyName = false;

        //先判斷組婚宴名稱及通關密語是否為空，若任一個為空，則在文字框旁顯示error。
        // Check for a valid userName.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            mNameView.requestFocus();
            emptyName = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            if (!emptyName) {
                mPasswordView.requestFocus();
            }
        }

        //若皆不為空，才嘗試以此組婚宴名稱及通關密語搜尋。
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
            progressDialog.show();
            ParseQuery query = ParseQuery.getQuery("Information");
            query.whereEqualTo("weddingAccount", name);
            query.whereEqualTo("weddingPassword", password);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject information, ParseException e) {
                    //有其他錯誤發生，如連線異常、伺服器掛掉等。
                    if (e != null) {
                        progressDialog.dismiss();
                        if (e.getCode() == 101) { //婚宴名稱及通關密語錯誤
                            new AlertDialog.Builder(context)
                                    .setTitle("訊息")
                                    .setMessage("無法取得婚禮資訊，請與新郎/新娘確認婚宴名稱及通關密語！")
                                    .setPositiveButton("好！", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                        else if (e.getCode() == 100) { //未連上網路
                            new AlertDialog.Builder(context)
                                    .setTitle("訊息")
                                    .setMessage("請連上網路！")
                                    .setPositiveButton("好！", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                        else {
                            //其他錯誤。
                            new AlertDialog.Builder(context)
                                    .setTitle("訊息")
                                    .setMessage("未知的錯誤發生，請稍候再試。")
                                    .setPositiveButton("好！", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                    //有搜尋到婚宴資訊，則進入至下一個畫面且夾帶婚宴資訊。
                    else if (information != null) {
                        Log.d("Neal", "WeddingInformation = " + information.get("groomName"));
                        progressDialog.dismiss();
                        login(information.getObjectId());
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
